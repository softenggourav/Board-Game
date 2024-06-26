package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameStatus;
import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.factory.GameFactory;
import com.game.tic_tac_toe.logic.GameLogic;
import com.game.tic_tac_toe.model.Game;
import com.game.tic_tac_toe.model.GameState;
import com.game.tic_tac_toe.model.Move;
import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.repository.GameRepository;
import com.game.tic_tac_toe.repository.GameStateRepository;
import com.game.tic_tac_toe.repository.MoveRepository;
import com.game.tic_tac_toe.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final Map<Long, GameLogic> games = new HashMap<>();

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameFactory gameFactory;

    @Autowired
    private GameStateRepository gameStateRepository;

    public Game startNewGame(GameType gameType, GameLevel gameLevel, Player player1, Player player2) {
        Game game = new Game();
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setLevel(gameLevel);
        game = gameRepository.save(game);

        GameLogic gameLogic = gameFactory.createGameLogic(gameType);
        gameLogic.startGame();
        games.put(game.getId(), gameLogic);

        GameState gameState = new GameState(game.getId().toString(), gameLogic.getBoard(), 'X');
        gameStateRepository.save(gameState);
        logBoardState(game.getId(), gameLogic);

        return game;
    }

    public Move makeMove(Long gameId, Long playerId, int row, int col) {
        Game game = getGameById(gameId);
        checkGameInProgress(game);
        Player player = getPlayerById(playerId);

        GameState gameState = getGameStateByGameId(gameId);
        GameLogic gameLogic = games.get(gameId);
        validateMove(gameLogic, row, col);

        boolean win = gameLogic.makeMove(row, col, player.getSymbol());
        updateGameCurrentPlayer(game, playerId);

        Move move = createAndSaveMove(game, player, row, col);
        updateGameStatus(game, gameLogic, win);
        clearGamesMap(game);

        // Update game state in MongoDB
        gameState.setBoard(gameLogic.getBoard());
        gameState.setCurrentPlayer(gameLogic.getCurrentPlayer());
        gameStateRepository.save(gameState);

        logBoardState(gameId, gameLogic);
        logWin(player, gameLogic, win);

        return move;
    }

    public Game endGame(Long gameId) {
        Game game = getGameById(gameId);
        game.setStatus(GameStatus.ENDED);
        gameRepository.save(game);
        clearGamesMap(game);
        return game;
    }

    public Game checkGameStatus(Long gameId) {
        return getGameById(gameId);
    }

    public Move makeMoveComputer(Long gameId, Long playerId) {
        Game game = getGameById(gameId);
        checkGameInProgress(game);
        Player player = getPlayerById(playerId);

        GameState gameState = getGameStateByGameId(gameId);
        GameLogic gameLogic = games.get(gameId);
        updateGameCurrentPlayer(game, playerId);

        int[] computerMove = determineComputerMove(gameLogic, game.getLevel(), player.getSymbol());
        int row = computerMove[0];
        int col = computerMove[1];

        boolean win = gameLogic.makeMove(row, col, player.getSymbol());
        Move move = createAndSaveMove(game, player, row, col);
        updateGameStatus(game, gameLogic, win);


        // Update game state in MongoDB
        gameState.setBoard(gameLogic.getBoard());
        gameState.setCurrentPlayer(gameLogic.getCurrentPlayer());
        gameStateRepository.save(gameState);

        logBoardState(gameId, gameLogic);
        logWin(player, gameLogic, win);

        return move;
    }

    private Game getGameById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    private void checkGameInProgress(Game game) {
        if (!game.getStatus().equals(GameStatus.IN_PROGRESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already finished.");
        }
    }

    private void validateMove(GameLogic gameLogic, int row, int col) {
        if (!gameLogic.isValidMove(row, col)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid move");
        }
    }

    private void updateGameCurrentPlayer(Game game, Long playerId) {
        if (game.getCurrentPlayerId() == null) {
            game.setCurrentPlayerId(playerId);
        } else if (game.getCurrentPlayerId().equals(playerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's not your turn.");
        } else {
            game.setCurrentPlayerId(playerId);
        }
    }

    private Move createAndSaveMove(Game game, Player player, int row, int col) {
        Move move = new Move();
        move.setGame(game);
        move.setPlayer(player);
        move.setRow(row);
        move.setCol(col);
        return moveRepository.save(move);
    }

    private void updateGameStatus(Game game, GameLogic gameLogic, boolean win) {
        if (win) {
            game.setStatus(GameStatus.WINNER);
        } else if (gameLogic.checkDraw()) {
            game.setStatus(GameStatus.DRAW);
        }
        gameRepository.save(game);
    }

    private void logBoardState(Long gameId, GameLogic gameLogic) {
        String boardState = gameLogic.getBoardState();
        logger.info("Board state for game {}:\n{}", gameId, boardState);
    }

    private void logWin(Player player, GameLogic gameLogic, boolean win) {
        if (win) {
            logger.info("******************** Player {} win **********************", player.getName());
        }
        else if (gameLogic.checkDraw()) {
            logger.info("******************** Game Draw **********************");
        }
    }

    private int[] determineComputerMove(GameLogic gameLogic, GameLevel gameLevel, char symbol) {
        if (gameLevel == GameLevel.EASY) {
            return gameLogic.determineEasyMove(symbol);
        } else if (gameLevel == GameLevel.MEDIUM) {
            return gameLogic.determineMediumMove(symbol);
        } else if (gameLevel == GameLevel.HARD) {
            return gameLogic.determineBestMove(symbol);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid game level");
    }

    private void clearGamesMap(Game game) {
        if (game.getStatus().equals(GameStatus.ENDED) || game.getStatus().equals(GameStatus.DRAW) || game.getStatus().equals(GameStatus.WINNER)) {
            games.remove(game.getId());
        }
    }

    private GameState getGameStateByGameId(Long gameId) {
        return gameStateRepository.findById(gameId.toString()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game state not found"));
    }
}
