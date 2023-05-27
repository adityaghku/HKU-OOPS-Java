/**
 * This class handles all the gameplay for the TicTacToe game
 * 
 * @author Aditya
 * 
 */
public class Gameplay {
    /**
     * A constant array of integer values representing the possible game states
     */
    public static final int[] checkConditions = {0, 1, 2, 3};

    private boolean isTurn;
    private int[][] board;
    private int count;

    /**
     * Constructs a new Gameplay object with an empty board and the turn of the first player
     */
    public Gameplay() {
        this.isTurn = true;
        this.board = new int[3][3];
        this.count = 0;
    }

    /**
     * Checks the current board state to determine if the game has ended, and if so, who has won
     * 
     * @return An integer value representing the game state:
     *         0 - the game is ongoing
     *         1 - player 1 has won
     *         2 - player 2 has won
     *         3 - the game is a draw
     */
    public int checkEndGame() {
        int row, col;

        // Check rows for a win
        for (int i = 0; i < 3; i++) {
            row = board[i][0] + board[i][1] + board[i][2];

            if (row == 3){
                return Gameplay.checkConditions[1];
            }

            if (row == -3){
                return Gameplay.checkConditions[2];
            }
        }

        // Check columns for a win
        for (int j = 0; j < 3; j++) {
            col = board[0][j] + board[1][j] + board[2][j];

            if (col == 3){
                return Gameplay.checkConditions[1];
            }

            if (col == -3){
                return Gameplay.checkConditions[2];
            }
        }

        // Check diagonals for a win
        int diag1 = board[0][0] + board[1][1] + board[2][2];

        if (diag1 == 3){
            return Gameplay.checkConditions[1];
        }

        if (diag1 == -3){
            return Gameplay.checkConditions[2];
        }

        int diag2 = board[2][0] + board[1][1] + board[0][2];

        if (diag2 == 3){
            return Gameplay.checkConditions[1];
        }

        if (diag2 == -3){
            return Gameplay.checkConditions[2];
        }

        // If the board is full and there is no winner, the game is a draw
        if (count == 9){
            return Gameplay.checkConditions[3];
        }

        // If the game is still ongoing, return normal status
        return Gameplay.checkConditions[0];
    }

    /**
     * Returns a comma-separated string of the current board state
     * 
     * @return A string consisting of 9 comma-separated integer values representing the current board state
     */
    public String getBoardString() {
        String boardStr = "";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardStr += Integer.toString(board[i][j]) + ",";
            }
        }

        return boardStr.substring(0, boardStr.length() - 1);
    }

    /**
     * Attempts to make a move on the board and updates the board state if the move is valid
     * 
     * @param isPlayer1 A boolean value representing whether the moving player is player 1
     * @param row       An integer value representing the row of the move
     * @param col       An integer value representing the column of the move
     * @return A boolean value indicating whether the move was valid and registered
     */
    public synchronized boolean makeMove(boolean isPlayer1, int row, int col) {
        // Check if it is the player's turn and if the move is valid
        if (!isPlayerTurn(isPlayer1) || board[col][row] != 0){
            return false;
        }

        // Update the board with the player's move
        if (isPlayer1){
            board[col][row] = 1;
        } else {
            board[col][row] = -1;
        }

        count++;

        // Update whose turn it is
        isTurn = !isTurn;

        return true;
    }

    /**
     * Checks if it is the specified player's turn
     * 
     * @param isPlayer1 A boolean value representing whether the player being checked is player 1
     * @return A boolean value indicating whether it is the specified player's turn
     */
    private boolean isPlayerTurn(boolean isPlayer1) {
        return (isPlayer1 && isTurn) || (!isPlayer1 && !isTurn);
    }
}