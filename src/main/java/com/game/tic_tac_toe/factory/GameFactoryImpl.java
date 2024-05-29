package com.game.tic_tac_toe.factory;

import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.logic.GameLogic;
import com.game.tic_tac_toe.logic.TicTacToeLogic;
import org.springframework.stereotype.Component;

@Component
public class GameFactoryImpl implements GameFactory {

    @Override
    public GameLogic createGameLogic(GameType gameType) {
        switch (gameType) {
            case GameType.TIC_TAC_TOE:
                return new TicTacToeLogic();
            // Add cases for other games here
            default:
                throw new IllegalArgumentException("Unknown game type: " + gameType);
        }
    }
}

