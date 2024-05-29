package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameStatus;
import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.factory.GameFactory;
import com.game.tic_tac_toe.logic.GameLogic;
import com.game.tic_tac_toe.model.Game;
import com.game.tic_tac_toe.model.Move;
import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.repository.GameRepository;
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

    public Game startNewGame(GameType gameType, GameLevel gameLevel, Player player1, Player player2) {
        Game game = new Game();
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setLevel(gameLevel);
        game = gameRepository.save(game);

        GameLogic gameLogic = gameFactory.createGameLogic(gameType);
        gameLogic.startGame();
        games.put(game.getId(), gameLogic);
        String boardState = gameLogic.getBoardState();
        logger.info("Board state for game {}:\n{}", game.getId(), boardState);

        return game;
    }

    public Move makeMove(Long gameId, Long playerId, int row, int col) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        // Check if the game is already finished
        if (!game.getStatus().equals(GameStatus.IN_PROGRESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already finished.");
        }

        Player player = playerRepository.findById(playerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
        GameLogic gameLogic = games.get(gameId);

        if (!gameLogic.isValidMove(row, col)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid move");
        }

        boolean win = gameLogic.makeMove(row, col, player.getSymbol());

        // Check if it's the player's turn
        if (game.getCurrentPlayerId() == null) {
            game.setCurrentPlayerId(playerId);
        } else if (game.getCurrentPlayerId().equals(playerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's not your turn.");
        } else {
            game.setCurrentPlayerId(playerId);
        }

        Move move = new Move();
        move.setGame(game);
        move.setPlayer(player);
        move.setRow(row);
        move.setCol(col);
        move = moveRepository.save(move);

        if (win) {
            game.setStatus(GameStatus.WINNER);
        } else if (gameLogic.checkDraw()) {
            game.setStatus(GameStatus.DRAW);
        }

        gameRepository.save(game);
        String boardState = gameLogic.getBoardState();
        logger.info("Board state for game {}:\n{}", game.getId(), boardState);
        if (win) {
            logger.info("******************** Player {} win **********************", player.getSymbol());
        }

        return move;
    }

    public Game endGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        game.setStatus(GameStatus.ENDED);
        return gameRepository.save(game);
    }

    public Game checkGameStatus(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }

    public Move makeMoveComputer(Long gameId, Long playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        // Check if the game is already finished
        if (!game.getStatus().equals(GameStatus.IN_PROGRESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already finished.");
        }

        Player player = playerRepository.findById(playerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
        GameLogic gameLogic = games.get(gameId);

        // Check if it's the player's turn
        if (game.getCurrentPlayerId() == null) {
            game.setCurrentPlayerId(playerId);
        } else if (game.getCurrentPlayerId().equals(playerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's not your turn.");
        } else {
            game.setCurrentPlayerId(playerId);
        }
        int[] computerMove = new int[2];
        if(game.getLevel()==GameLevel.EASY){
            computerMove = gameLogic.determineEasyMove();
        } else if (game.getLevel()==GameLevel.MEDIUM) {
            computerMove = gameLogic.determineMediumMove();
        }
        else if (game.getLevel()==GameLevel.HARD){
            computerMove = gameLogic.determineBestMove();
        }
        int row = computerMove[0];
        int col = computerMove[1];

        boolean win = gameLogic.makeMove(row, col, player.getSymbol());


        Move move = new Move();
        move.setGame(game);
        move.setPlayer(player);
        move.setRow(row);
        move.setCol(col);
        move = moveRepository.save(move);

        if (win) {
            game.setStatus(GameStatus.WINNER);
        } else if (gameLogic.checkDraw()) {
            game.setStatus(GameStatus.DRAW);
        }

        gameRepository.save(game);
        String boardState = gameLogic.getBoardState();
        logger.info("Board state for game {}:\n{}", game.getId(), boardState);
        if (win) {
            logger.info("******************** Player {} win **********************", player.getSymbol());
        }
        return move;
    }
}
