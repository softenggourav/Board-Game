package com.game.tic_tac_toe.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GameStatus status;


    private GameLevel level;

    @Column(nullable = true)
    private Long currentPlayerId;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Move> moves;
}
