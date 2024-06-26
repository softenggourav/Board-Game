package com.game.tic_tac_toe.logic;

public interface GameLogic {
    void startGame();

    boolean makeMove(int row, int col, char symbol);

    boolean checkWin();

    boolean checkDraw();

    String getBoardState();

    boolean isValidMove(int row, int col);

    int[] determineEasyMove(char symbol);

    int[] determineMediumMove(char symbol);

    int[] determineBestMove(char symbol);

    char[][] getBoard();

    char getCurrentPlayer();
}

