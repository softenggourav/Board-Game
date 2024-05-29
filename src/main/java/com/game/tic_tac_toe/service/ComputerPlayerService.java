package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.logic.GameLogic;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ComputerPlayerService {
    private final Random random = new Random();

    public int[] determineMove(GameLogic gameLogic) {
        return gameLogic.determineEasyMove();
    }

    public int[] determineMove2(GameLogic gameLogic) {
        return gameLogic.determineMediumMove();
    }

    public int[] determineMove3(GameLogic gameLogic) {
        return gameLogic.determineBestMove();
    }

}

