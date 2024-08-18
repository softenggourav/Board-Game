package com.game.tic_tac_toe.controller;

import lombok.Data;

@Data
class AuthenticationRequest {
    private String username;
    private String password;

    // getters and setters
}
