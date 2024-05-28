package com.game.tic_tac_toe.repository;

import com.game.tic_tac_toe.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}

