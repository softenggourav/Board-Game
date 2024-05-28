package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.factory.GameFactory;
import com.game.tic_tac_toe.logic.GameLogic;
import com.game.tic_tac_toe.logic.TicTacToeLogic;
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

    public Game startNewGame(String gameType, Player player1, Player player2) {
        Game game = new Game();
        game.setStatus("IN_PROGRESS");
        game = gameRepository.save(game);

        GameLogic gameLogic = gameFactory.createGameLogic(gameType);
        gameLogic.startGame();
        games.put(game.getId(), gameLogic);

        return game;
    }

    public Move makeMove(Long gameId, Long playerId, int row, int col) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        // Check if the game is already finished
        if (!game.getStatus().equals("IN_PROGRESS")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already finished.");
        }

        Player player = playerRepository.findById(playerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
        GameLogic gameLogic = games.get(gameId);

        if (!gameLogic.isValidMove(row, col)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid move");
        }

        boolean win = gameLogic.makeMove(row, col, player.getSymbol());

        Move move = new Move();
        move.setGame(game);
        move.setPlayer(player);
        move.setRow(row);
        move.setCol(col);
        move = moveRepository.save(move);

        if (win) {
            game.setStatus("WINNER_" + player.getSymbol());
        } else if (gameLogic.checkDraw()) {
            game.setStatus("DRAW");
        }

        gameRepository.save(game);
        if (gameLogic instanceof TicTacToeLogic) {
            String boardState = ((TicTacToeLogic) gameLogic).getBoardState();
            logger.info("Board state after move:\n{}", boardState);
        }
        return move;
    }

    public Game endGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        game.setStatus("ENDED");
        return gameRepository.save(game);
    }
}
