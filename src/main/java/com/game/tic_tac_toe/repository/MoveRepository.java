package com.game.tic_tac_toe.repository;

import com.game.tic_tac_toe.model.Move;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveRepository extends JpaRepository<Move, Long> {
}
