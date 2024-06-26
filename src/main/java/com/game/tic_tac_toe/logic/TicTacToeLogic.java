package com.game.tic_tac_toe.logic;

import lombok.Getter;

import java.util.Random;

public class TicTacToeLogic implements GameLogic {
    private final Random random = new Random();
    @Getter
    private char[][] board;
    @Getter
    private char currentPlayer;

    public TicTacToeLogic() {
        board = new char[3][3];
        currentPlayer = 'X';
    }

    @Override
    public void startGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'X';
    }

    @Override
    public boolean makeMove(int row, int col, char symbol) {
        if (board[row][col] == ' ') {
            board[row][col] = symbol;
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            return checkWin();
        }
        return false;
    }

    @Override
    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    @Override
    public boolean checkWin() {
        // Horizontal, vertical, and diagonal checks
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true;
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true;
        }
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return true;
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return true;
        return false;
    }

    @Override
    public boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getBoardState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(" | ");
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]);
                if (j < 2) sb.append(" | ");
            }
            sb.append(" | ");
            sb.append("\n");
            sb.append("---------------\n");
        }
        return sb.toString();
    }

    @Override
    public int[] determineEasyMove(char symbol) {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!isValidMove(row, col));
        return new int[]{row, col};
    }

    @Override
    public int[] determineMediumMove(char symbol) {
        char opponentSymbol = (symbol == 'X') ? 'O' : 'X';

        // Check if computer can win in the next move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isValidMove(i, j)) {
                    board[i][j] = symbol;
                    if (checkWin()) {
                        board[i][j] = ' '; // Reset after checking
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }

        // Check if player can win in the next move and block them
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isValidMove(i, j)) {
                    board[i][j] = opponentSymbol;
                    if (checkWin()) {
                        board[i][j] = ' '; // Reset after checking
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }

        // Otherwise, make a random move
        return determineEasyMove(symbol);
    }

    @Override
    public int[] determineBestMove(char symbol) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = symbol;
                    int score = minimax(board, 0, false, symbol);
                    board[i][j] = ' ';
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    public int minimax(char[][] board, int depth, boolean isMaximizing, char symbol) {
        char opponentSymbol = (symbol == 'X') ? 'O' : 'X';

        if (checkWin()) {
            return isMaximizing ? -1 : 1;
        }
        if (checkDraw()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = symbol;
                        int score = minimax(board, depth + 1, false, symbol);
                        board[i][j] = ' ';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = opponentSymbol;
                        int score = minimax(board, depth + 1, true, symbol);
                        board[i][j] = ' ';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}
