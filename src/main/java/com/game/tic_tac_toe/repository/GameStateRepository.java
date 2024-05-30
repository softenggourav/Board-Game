package com.game.tic_tac_toe.repository;

import com.game.tic_tac_toe.model.GameState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameStateRepository extends MongoRepository<GameState, String> {
}

