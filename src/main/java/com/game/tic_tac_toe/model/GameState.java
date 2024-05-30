package com.game.tic_tac_toe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "game_states")
@Data
public class GameState {
    @Id
    private String gameId;
    private char[][] board;
    private char currentPlayer;

    public GameState(String gameId, char[][] board, char currentPlayer) {
        this.gameId = gameId;
        this.board = board;
        this.currentPlayer = currentPlayer;
    }
}

