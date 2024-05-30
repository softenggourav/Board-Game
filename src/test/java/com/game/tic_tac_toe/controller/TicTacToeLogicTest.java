package com.game.tic_tac_toe.controller;

import com.game.tic_tac_toe.logic.TicTacToeLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeLogicTest {

    private TicTacToeLogic ticTacToeLogic;

    @BeforeEach
    public void setUp() {
        ticTacToeLogic = new TicTacToeLogic();
        ticTacToeLogic.startGame();
    }

    @Test
    public void testStartGame() {
        char[][] board = ticTacToeLogic.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(' ', board[i][j]);
            }
        }
        assertEquals('X', ticTacToeLogic.getCurrentPlayer());
    }

    @Test
    public void testIsValidMove() {
        assertTrue(ticTacToeLogic.isValidMove(0, 0));
        ticTacToeLogic.makeMove(0, 0, 'X');
        assertFalse(ticTacToeLogic.isValidMove(0, 0));
    }

    @Test
    public void testCheckWinHorizontal() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(0, 1, 'X');
        ticTacToeLogic.makeMove(0, 2, 'X');
        assertTrue(ticTacToeLogic.checkWin());
    }

    @Test
    public void testCheckWinVertical() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(1, 0, 'X');
        ticTacToeLogic.makeMove(2, 0, 'X');
        assertTrue(ticTacToeLogic.checkWin());
    }

    @Test
    public void testCheckWinDiagonal() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(1, 1, 'X');
        ticTacToeLogic.makeMove(2, 2, 'X');
        assertTrue(ticTacToeLogic.checkWin());
    }

    @Test
    public void testCheckDraw() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(0, 1, 'O');
        ticTacToeLogic.makeMove(0, 2, 'X');
        ticTacToeLogic.makeMove(1, 0, 'X');
        ticTacToeLogic.makeMove(1, 1, 'O');
        ticTacToeLogic.makeMove(1, 2, 'X');
        ticTacToeLogic.makeMove(2, 0, 'O');
        ticTacToeLogic.makeMove(2, 1, 'X');
        ticTacToeLogic.makeMove(2, 2, 'O');
        assertTrue(ticTacToeLogic.checkDraw());
    }

    @Test
    public void testDetermineEasyMove() {
        int[] move = ticTacToeLogic.determineEasyMove('X');
        assertTrue(ticTacToeLogic.isValidMove(move[0], move[1]));
    }

    @Test
    public void testDetermineMediumMove() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(0, 1, 'X');
        int[] move = ticTacToeLogic.determineMediumMove('X');
        assertArrayEquals(new int[]{0, 2}, move);
    }

    @Test
    public void testDetermineBestMove() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(0, 1, 'X');
        int[] move = ticTacToeLogic.determineBestMove('X');
        assertArrayEquals(new int[]{0, 2}, move);
    }

    @Test
    public void testMinimaxWin() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(0, 1, 'X');
        ticTacToeLogic.getBoard()[0][2] = ' ';
        int score = ticTacToeLogic.minimax(ticTacToeLogic.getBoard(), 0, true, 'X');
        assertEquals(1, score);
    }

    @Test
    public void testMinimaxDraw() {
        ticTacToeLogic.makeMove(0, 0, 'X');
        ticTacToeLogic.makeMove(0, 1, 'O');
        ticTacToeLogic.makeMove(0, 2, 'X');
        ticTacToeLogic.makeMove(1, 0, 'X');
        ticTacToeLogic.makeMove(1, 1, 'O');
        ticTacToeLogic.makeMove(1, 2, 'X');
        ticTacToeLogic.makeMove(2, 0, 'O');
        ticTacToeLogic.makeMove(2, 1, 'X');
        ticTacToeLogic.makeMove(2, 2, 'O');
        int score = ticTacToeLogic.minimax(ticTacToeLogic.getBoard(), 0, true, 'X');
        assertEquals(0, score);
    }
}
