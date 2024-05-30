package com.game.tic_tac_toe.controller;

import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.model.Game;
import com.game.tic_tac_toe.model.Move;
import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.service.GameService;
import com.game.tic_tac_toe.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired
    private GameService gameService;
    @Autowired
    PlayerService playerService;

    @PostMapping("/start")
    public ResponseEntity<Game> startGame(@RequestParam GameType gameType, @RequestParam GameLevel gameLevel, @RequestBody List<Player> players) {
        if (players.size() != 2) {
            return ResponseEntity.badRequest().build();
        }
        Game game = gameService.startNewGame(gameType, gameLevel, players.get(0), players.get(1));
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody Move move) {
        try {
            Move newMove = gameService.makeMove(gameId, move.getPlayer().getId(), move.getRow(), move.getCol());
            return ResponseEntity.ok(newMove);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{gameId}/{playerId}/moveComputer")
    public ResponseEntity<?> makeMoveComputer(@PathVariable Long gameId, @PathVariable Long playerId) {
        try {
            Move newMove = gameService.makeMoveComputer(gameId, playerId);
            return ResponseEntity.ok(newMove);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{gameId}/end")
    public ResponseEntity<Game> endGame(@PathVariable Long gameId) {
        Game endedGame = gameService.endGame(gameId);
        return ResponseEntity.ok(endedGame);
    }

    @GetMapping("/{gameId}/status")
    public ResponseEntity<Game> checkGameStatus(@PathVariable Long gameId) {
        Game game = gameService.checkGameStatus(gameId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/toss")
    public ResponseEntity<List<Player>> makeToss(@RequestBody List<Player> players) {
        if (players.size() != 2) {
            return ResponseEntity.badRequest().build();
        }
        Player player1 = playerService.getPlayerById(players.get(0).getId());
        Player player2 = playerService.getPlayerById(players.get(1).getId());

        // Shuffle the players
        Random random = new Random();
        if (random.nextBoolean()) {
            Player temp = player1;
            player1 = player2;
            player2 = temp;
        }

        player1.setSymbol('X');
        player2.setSymbol('O');

        playerService.updatePlayer(player1); // Update player with symbol
        playerService.updatePlayer(player2); // Update player with symbol

        return ResponseEntity.ok(Arrays.asList(player1, player2));
    }
}
