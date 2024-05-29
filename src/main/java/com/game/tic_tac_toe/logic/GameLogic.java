package com.game.tic_tac_toe.logic;

public interface GameLogic {
    void startGame();

    boolean makeMove(int row, int col, char symbol);

    boolean checkWin();

    boolean checkDraw();

    boolean isValidMove(int row, int col);

    int[] determineEasyMove();

    int[] determineMediumMove();

    int[] determineBestMove();
}

