package com.game.tic_tac_toe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = true)
    private Character symbol;
}
