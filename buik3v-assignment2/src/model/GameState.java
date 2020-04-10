/*
 * TCSS 435 - Assignment 2
 * Pentago
 */
package model;


/**
 * This is the current state of the pentago game board.
 * Players are able 
 * @author Kevin Bui
 * @version December 12, 2019 
 */
public class GameState {

	/**Pentago game board*/
	private char[][] myBoard;
	
	/**
	 * Constructor
	 */
	public GameState() {
		myBoard = initBoard();
		
	}
	
	/**
	 * Initializes myBoard to all dashess first.
	 * @return
	 */
	public char[][] initBoard() {
		char[][] newBoard = new char[6][6];
		for(int i = 0; i < newBoard.length; i++) {
			for(int j = 0; j < newBoard[i].length; j++) {
				newBoard[i][j] = '-';
			}
		}
		return newBoard;
	}
	
	/**
	 * Returns myBoard;
	 * @return
	 */
	public char[][] getBoard() {
		return myBoard;
	}
	
	/**
	 * Sets myBoard equal to board.
	 * @param board
	 */
	public void setBoard(char[][] board) {
		myBoard = board;
	}
	
	
	/**
	 * Rotates a quadrant right by 90 degrees.
	 * @param board 
	 * @param quadrant 
	 * @return char[][] a new rotated board.
	 */
	public char[][] rotateBlockRight(char[][] board, int quadrant) {
		char[][] block = copy2DArray(board, board.length, board.length);
		if(quadrant == 1) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					block[i][j] = board[3 - j - 1][i];
				}
			}
		}
		else if(quadrant == 2) {
			for(int i = 0; i < 3; i++) {
				for(int j = 3; j < 6; j++) {
					block[i][j] = board[6 - j - 1][i + 3];
				}
			}
		}
		else if(quadrant == 3) {
			for(int i = 3; i < 6; i++) {
				for(int j = 0; j < 3; j++) {
					block[i][j] = board[3 - j - 1 + 3][i - 3];
				}
			}
		}
		else {
			for(int i = 3; i < 6; i++) {
				for(int j = 3; j < 6; j++) {
					block[i][j] = board[6 - j - 1 + 3][i];
				}
			}
		}

		return block;
	}
	
	/**
	 * Rotates a quadrant left by 90 degrees.
	 * @param board
	 * @param quadrant
	 * @return char[][] a new rotated board.
	 */
	public char[][] rotateBlockLeft(char[][] board, int quadrant) {
		char[][] block = copy2DArray(board, board.length, board.length);
		if(quadrant == 1) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					block[i][j] = board[j][3 - i - 1];
				}
			}
		}
		else if(quadrant == 2) {
			for(int i = 0; i < 3; i++) {
				for(int j = 3; j < 6; j++) {
					block[i][j] = board[j - 3][3 - i - 1 + 3];
				}
			}
		}
		else if(quadrant == 3) {
			for(int i = 0; i < 6; i++) {
				for(int j = 0; j < 3; j++) {
					block[i][j] = board[j + 3][3 - i - 1 + 3];
				}
			}
		}
		else {
			for(int i = 3; i < 6; i++) {
				for(int j = 3; j < 6; j++) {
					block[i][j] = board[j][3 - i - 1 + 6];
				}
			}
		}

		return block;
	}
	
	/**
	 * Copies the contents of an array, and returns a new copy.
	 * @param board
	 * @param n
	 * @param m
	 * @return char[][] new copy of an array.
	 */
	public char[][] copy2DArray(char[][] board, int n, int m) {
		char[][] newBoard = new char[n][m];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}
	
	/**
	 * Marks a specific cell of the game board with a player/AI token.
	 * @param quadrant
	 * @param position
	 * @param mark
	 */
	public void markCell(int quadrant, int position, char mark ) {
		if(quadrant == 1) {
			myBoard[(position - 1) / 3][(position - 1) % 3 ] = mark;
		}
		else if(quadrant == 2) {
			myBoard[(position - 1) / 3][(position - 1) % 3 + 3] = mark;
		}
		else if(quadrant == 3) {
			myBoard[(position - 1) / 3 + 3][(position - 1) % 3] = mark;

		}
		else {
			myBoard[(position - 1) / 3 + 3][(position - 1) % 3 + 3] = mark;

		}
	}
	
	/**
	 * Marks a specific cell in the game board with a player/AI token
	 * This uses exact coordinates.
	 * @param row
	 * @param col
	 * @param mark
	 */
	public void markCellDirectly(int row, int col, char mark) {
		myBoard[row][col] = mark;
	}
	
	/**
	 * Prints out the gameboard and locations of player / AI tokens.
	 */
	public void print2DArray() {
		for(int i = 0; i < myBoard.length; i++) {
			for(int j = 0; j < myBoard[i].length; j++) {
				if(myBoard[i][j] == '-') {
					System.out.print("- ");
				}
				else {
					System.out.print(myBoard[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Checks if the board is full of tokens.
	 * Used to see if the GameState results in a draw.
	 * @return
	 */
	public boolean checkFullBoard() {
		for(int i = 0; i < myBoard.length; i++) {
			for(int j = 0; j < myBoard.length; j++) {
				if(myBoard[i][j] == '-') {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if a winning state has been reached.
	 * @param playerToken
	 * @return
	 */
	public boolean checkWin(char playerToken) {
		return checkRowWin(playerToken) ||
				checkColWin(playerToken) ||
				checkDiagonalWin(playerToken);
	}
	
	/**
	 * Checks if there are 5 consecutive tokens for each row.
	 * @param playerToken
	 * @return
	 */
	public boolean checkRowWin(char playerToken) {
		int playerTokenCount = 0;

		//Two checks for if rows contains a win
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 5; j++) {
				if(playerToken == myBoard[i][j]) {
					playerTokenCount++;
				}
				if(playerTokenCount == 5) {
					return true;
				}
				else if(playerToken != myBoard[i][j]) {
					break;
				}
			}
			playerTokenCount = 0;
			for(int j = 1; j < 6; j++) {
				if(playerToken == myBoard[i][j]) {
					playerTokenCount++;
				}
				if(playerTokenCount == 5) {
					return true;
				}
				else if(playerToken != myBoard[i][j]) {
					break;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if there are 5 consecutive tokens in a column.
	 * @param playerToken
	 * @return
	 */
	public boolean checkColWin(char playerToken) {
		int playerTokenCount = 0;
		//check column wins
		//First check from y index 0 to 4
		for(int j = 0; j < 6; j++) {
			for(int i = 0; i < 5; i++) {
				if(playerToken == myBoard[i][j]) {
					playerTokenCount++;
				}
				if(playerTokenCount == 5) {
					return true;
				}
				else if(playerToken != myBoard[i][j]) {
					break;
				}
			}
			playerTokenCount = 0;
			//Second check from y index 1 to 5
			for(int i = 1; i < 6; i++) {
				if(playerToken == myBoard[i][j]) {
					playerTokenCount++;
				}
				if(playerTokenCount == 5) {
					return true;
				}
				else if(playerToken != myBoard[i][j]) {
					break;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if there are 5 consecutive tokens for each diagonal 5 cells long.
	 * @param playerToken
	 * @return
	 */
	public boolean checkDiagonalWin(char playerToken) {
		int playerTokenCount = 0;

		//check diagonal from (0, 0) to (4, 4)
		for(int i = 0; i < 5; i++) {
			if(playerToken == myBoard[i][i]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][i])  {
				break;
			}
		}
		//check diagonal from (1, 1) to (5, 5)
		playerTokenCount = 0;
		for(int i = 1; i < 6; i++) {
			if(playerToken == myBoard[i][i]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][i])  {
				break;
			}
		}
		//check diagonal from (4, 1) to (0, 5)
		playerTokenCount = 0;
		for(int i = 4; i >= 0; i--) {
			if(playerToken == myBoard[i][Math.abs(i - 5)]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][Math.abs(i - 5)])  {
				break;
			}
		}
		//check diagonal from (5, 0) to (1, 4)
		playerTokenCount = 0;
		for(int i = 5; i >= 1; i--) {
			if(playerToken == myBoard[i][Math.abs(i - 5)]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][Math.abs(i - 5)])  {
				break;
			}
		}
		//check diagonal from (0, 1) to (4, 5)
		playerTokenCount = 0;
		for(int i = 0; i < 5; i++) {
			if(playerToken == myBoard[i][i + 1]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][i + 1])  {
				break;
			}
		}
		
		//check diagonal from (1, 0), to (5, 4)
		playerTokenCount = 0;
		for(int i = 1; i < 6; i++) {
			if(playerToken == myBoard[i][i - 1]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][i - 1])  {
				break;
			}
		}
		
		//check diagonal from (4, 0) to (0, 4)
		playerTokenCount = 0;
		for(int i = 4; i >= 0; i--) {
			if(playerToken == myBoard[i][Math.abs(i - 4)]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][Math.abs(i - 4)])  {
				break;
			}
		}
		
		//check diagonal from (5, 1) to (1, 5)
		playerTokenCount = 0;
		for(int i = 5; i >= 1; i--) {
			if(playerToken == myBoard[i][Math.abs(i - 6)]) {
				playerTokenCount++;
			}
			if(playerTokenCount == 5) {
				return true;
			}
			else if(playerToken != myBoard[i][Math.abs(i - 6)])  {
				break;
			}
		}
		return false;
	}

}
