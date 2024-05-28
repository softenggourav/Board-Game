package com.game.tic_tac_toe.factory;


import com.game.tic_tac_toe.logic.GameLogic;

public interface GameFactory {
    GameLogic createGameLogic(String gameType);
}

