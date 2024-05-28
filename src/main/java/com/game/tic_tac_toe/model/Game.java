package com.game.tic_tac_toe.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; // e.g., "IN_PROGRESS", "DRAW", "WINNER_X", "WINNER_O"

    @Column(nullable = true)
    private Long currentPlayerId;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Move> moves;
}
