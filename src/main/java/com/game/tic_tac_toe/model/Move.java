package com.game.tic_tac_toe.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Data
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private int row;
    private int col;
}
