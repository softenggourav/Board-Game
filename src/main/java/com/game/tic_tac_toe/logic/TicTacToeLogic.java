package com.game.tic_tac_toe.logic;

public class TicTacToeLogic implements GameLogic {
    private char[][] board;
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

    public String getBoardState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]);
                if (j < 2) sb.append(" | ");
            }
            sb.append("\n");
            if (i < 2) sb.append("-----\n");
        }
        return sb.toString();
    }
}
