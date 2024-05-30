package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public void updatePlayer(Player player) {
        playerRepository.save(player);
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }
}
