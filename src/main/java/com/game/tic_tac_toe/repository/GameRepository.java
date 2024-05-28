package com.game.tic_tac_toe.repository;

import com.game.tic_tac_toe.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}

