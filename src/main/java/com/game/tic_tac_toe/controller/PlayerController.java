package com.game.tic_tac_toe.controller;

import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player createdPlayer = playerService.createPlayer(player);
        return ResponseEntity.ok(createdPlayer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        return ResponseEntity.ok(player);
    }
}
