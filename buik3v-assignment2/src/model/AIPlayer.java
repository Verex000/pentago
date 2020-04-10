/*
 * TCSS 435 - Assignment 2
 * Pentago
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates an AI Player that uses minimax to play Pentago.
 * @author Kevin Bui
 * @version December 12, 2019
 */
public class AIPlayer {

	/**Pentago game board 
	 * Synchronized with the GameState board*/
	private char[][] myBoard;
	
	public int nodesExpanded;
	
	/**Token of the AI can be b or w*/
	char myToken;
	
	/**Token of the player opponent*/
	char myOppToken;
	
	/**
	 * Constructor for an AI Player.
	 * @param state
	 * @param token
	 */
	public AIPlayer(GameState state, char token) {
		myBoard = state.getBoard();
		myToken = token;
		myOppToken = myToken == 'b' ? 'w' : 'b';
		//myDiagScore = new int[6];
		
	}
	
	/**
	 * Gets the current board.
	 * @return
	 */
	public char[][] getBoard() {
		return myBoard;
	}
	
	/**
	 * Gets the AI Token.
	 * @return
	 */
	public char getAIToken() {
		return myToken;
	}
	
	/**
	 * Gets the opponent token.
	 * @return
	 */
	public char getOppToken() {
		return myOppToken;
	}
	
	/**
	 * Sets the AI board.
	 * @param board
	 */
	public void setBoard(char[][] board) {
		myBoard = board;
	}
	
	
	/**
	 * Returns a list of all the possible moves.
	 * @return
	 */
	public List<int[]> getChildrenMoves() {
		List<int[]> moves = new ArrayList<int[]>();
		
		if(checkWin(myToken) || checkWin(myOppToken)) {
			return moves;
		}
		
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				if(myBoard[i][j] == '-') {
					moves.add(new int[] {i, j});
				}
			}
		}
		return moves;
	}
	
	/**
	 * Returns the best quadrant and position to place a token.
	 * @return
	 */
	private int[] playerMove() {
		int[] result = minimax(2, myToken);
		int[] move = {result[1], result[2]};
		return move;
	}
	
	private int[] playerMoveAlphaBeta() {
		int[] result = alphaBeta(1, Integer.MIN_VALUE, Integer.MAX_VALUE, myToken);
		int[] move = {result[1], result[2]};
		return move;
	}
	
	/**
	 * Returns the best quadrant and position to place a token.
	 * @return
	 */
	public int[] bestPositionQuadrant() {
		int[] move = playerMoveAlphaBeta();
		int quadrant = 0;
		int row = 0;
		int col = 0;
		int[] posQuad = new int[] { 0, quadrant};
		if(move[0] > 2) {
			//4th Quadrant
			if(move[1] > 2) {
				quadrant = 4;
				row = move[0] % 3;
				col = move[1] % 3;
			}
			//3rd Quadrant
			else {
				quadrant = 3;
				row = move[0] % 3;
				col = move[1];
			}
		}
		else {
			//2nd Quadrant
			if(move[1] > 2) {
				quadrant = 2;
				row = move[0];
				col = move[1] % 3;
			}
			//1st Quadrant
			else {
				quadrant = 1;
				row = move[0];
				col = move[1];
			}
		}
		posQuad[0] = (row * 3) + col + 1;
		posQuad[1] = quadrant;
		return posQuad;
		
	}
	
	/**
	 * Calculates the highest scoring move for the current board configuration.
	 * Using minimax algorithm.
	 * Returns the best coordinates to place a token.
	 * @param depth
	 * @param playerToken
	 * @return
	 */
	private int[] minimax(int depth, char playerToken) {
		List<int[]> availMoves = getChildrenMoves();
		int score = 0;
		if(playerToken == myToken) {
			score = Integer.MIN_VALUE;
		}
		else {
			score = Integer.MAX_VALUE;
		}
		int currentScore = 0;
		int rowMove = 0;
		int colMove = 0;
		
		if(availMoves.isEmpty() || depth == 0) {
			score = getScore();
		}
		else {
			for(int[] move : availMoves) {
				nodesExpanded++;
				int currentRowMove = move[0];
				int currentColMove = move[1];
				myBoard[currentRowMove][currentColMove] = playerToken;
				if(playerToken == myToken) {
					currentScore = minimax(depth - 1, myOppToken)[0];
					if(currentScore > score) {
						score = currentScore;
						rowMove = currentRowMove;
						colMove = currentColMove;
					}
				}
				else {
					currentScore = minimax(depth - 1, myToken)[0];
					if(currentScore < score) {
						score = currentScore;
						rowMove = currentRowMove;
						colMove = currentColMove;
					}
				}
				myBoard[currentRowMove][currentColMove] = '-';
			}
		}
		return new int[] {score, rowMove, colMove};
	}
	
	/**
	 * Calculates the highest scoring move for the current board configuration.
	 * Using minimax with alpha beta pruning.
	 * Returns the best coordinates to place a token.
	 * @param depth
	 * @param playerToken
	 * @return
	 */
	private int[] alphaBeta(int depth, int alpha, int beta, char playerToken) {
		List<int[]> availMoves = getChildrenMoves();
		int score = 0;
		int rowMove = 0;
		int colMove = 0;
		
		if(availMoves.isEmpty() || depth == 0) {
			score = getScore();
		}
		else {
			for(int[] move : availMoves) {
				nodesExpanded++;
				int currentRowMove = move[0];
				int currentColMove = move[1];
				myBoard[currentRowMove][currentColMove] = playerToken;
				if(playerToken == myToken) {
					score = alphaBeta(depth - 1, alpha, beta, myOppToken)[0];
					if(score > alpha) {
						alpha = score;
						rowMove = currentRowMove;
						colMove = currentColMove;
					}
				}
				else {
					score = alphaBeta(depth - 1, alpha, beta, myToken)[0];
					if(score < beta) {
						beta = score;
						rowMove = currentRowMove;
						colMove = currentColMove;
					}
				}
				myBoard[currentRowMove][currentColMove] = '-';
				
				if(alpha >= beta) {
					return new int[] {alpha, rowMove, colMove};
				}
			}
		}
		if(playerToken == myToken) {
			return new int[] {alpha, rowMove, colMove};
		}
		else {
			return new int[] {beta, rowMove, colMove};
		}
	}
	
	/**
	 * Returns the score for the current configuration of the board.
	 * @return
	 */
	public int getScore() {
		int score = 0;
		for(int i = 0; i < 6; i++) {
			score += getRowScore(i);
			score += getColScore(i);
		}
		score += getDiagScore();
		return score;
	}
	
	/**
	 * Returns the cumulative score for each row.
	 * @param row
	 * @return
	 */
	public int getRowScore(int row) {
		int rowScore = 0;
		for(int j = 0; j < 6; j++) {
			if(myBoard[row][j] == myToken) {
				if(rowScore < 0) {
					return 0;
				}
				else {
					rowScore += 20;
				}
			}
			else if(myBoard[row][j] != myToken &&
					myBoard[row][j] != '-') {
				if(rowScore > 0) {
					return 0;
				}
				else {
					rowScore -= 20;
				}
			}
		}
		return rowScore;
	}
	
	/**
	 * Returns the cumulative score for each column.
	 * @param col
	 * @return
	 */
	public int getColScore(int col) {
		int colScore = 0;
		for(int i = 0; i < 6; i++) {
			if(myBoard[i][col] == myToken) {
				if(colScore < 0) {
					return 0;
				}
				else {
					colScore += 20;
				}
			}
			else if(myBoard[i][col] != myToken &&
					myBoard[i][col] != '-') {
				if(colScore > 0) {
					return 0;
				}
				else {
					colScore -= 20;
				}
			}
		}
		return colScore;
	}
	
	/**
	 * Returns the cumulative score for each diagonal.
	 * @return
	 */
	public int getDiagScore() {
		int decreasingDiagScore1 = 0;
		//Main decreasing diagonal
		for(int i = 0; i < 6; i++) {
			if(myBoard[i][i] == myToken) {
				if(decreasingDiagScore1 < 0) {
					return 0;
				}
				else {
					decreasingDiagScore1 += 20;
				}
			}
			else if(myBoard[i][i] != myToken &&
					myBoard[i][i] != '-') {
				if(decreasingDiagScore1 > 0) {
					return 0;
				}
				else {
					decreasingDiagScore1 -= 20;
				}
			}
		}
		int decreasingDiagScore2 = 0;
		for(int i = 1; i < 6; i++) {
			if(myBoard[i][i - 1] == myToken) {
				if(decreasingDiagScore2 < 0) {
					return 0;
				}
				else {
					decreasingDiagScore2 += 20;
				}
			}
			else if(myBoard[i][i - 1] != myToken &&
					myBoard[i][i - 1] != '-') {
				if(decreasingDiagScore2 > 0) {
					return 0;
				}
				else {
					decreasingDiagScore2 -= 20;
				}
			}
		}
		int decreasingDiagScore3 = 0;
		for(int i = 0; i < 5; i++) {
			if(myBoard[i][i + 1] == myToken) {
				if(decreasingDiagScore3 < 0) {
					return 0;
				}
				else {
					decreasingDiagScore3 += 20;
				}
			}
			else if(myBoard[i][i + 1] != myToken &&
					myBoard[i][i + 1] != '-') {
				if(decreasingDiagScore3 > 0) {
					return 0;
				}
				else {
					decreasingDiagScore3 -= 20;
				}
			}
		}
		int increasingDiagScore1 = 0;
		for(int i = 5; i >= 0; i--) {
			if(myBoard[i][Math.abs(i - 5)] == myToken) {
				if(increasingDiagScore1 < 0) {
					return 0;
				}
				else {
					increasingDiagScore1 += 20;
				}
			}
			else if(myBoard[i][Math.abs(i - 5)] != myToken &&
					myBoard[i][Math.abs(i - 5)] != '-') {
				if(increasingDiagScore1 > 0) {
					return 0;
				}
				else {
					increasingDiagScore1 -= 20;
				}
			}
		}
		int increasingDiagScore2 = 0;
		for(int i = 5; i >= 1; i--) {
			if(myBoard[i][Math.abs(i - 6)] == myToken) {
				if(increasingDiagScore2 < 0) {
					return 0;
				}
				else {
					increasingDiagScore2 += 20;
				}
			}
			else if(myBoard[i][Math.abs(i - 6)] != myToken &&
					myBoard[i][Math.abs(i - 6)] != '-') {
				if(increasingDiagScore2 > 0) {
					return 0;
				}
				else {
					increasingDiagScore2 -= 20;
				}
			}
		}
		int increasingDiagScore3 = 0; 
		for(int i = 4; i >= 0; i--) {
			if(myBoard[i][Math.abs(i - 4)] == myToken) {
				if(increasingDiagScore3 < 0) {
					return 0;
				}
				else {
					increasingDiagScore3 += 20;
				}
			}
			else if(myBoard[i][Math.abs(i - 4)] != myToken &&
					myBoard[i][Math.abs(i - 4)] != '-') {
				if(increasingDiagScore3 > 0) {
					return 0;
				}
				else {
					increasingDiagScore3 -= 20;
				}
			}
		}
		
		return decreasingDiagScore1 + decreasingDiagScore2 + 
				decreasingDiagScore3 + increasingDiagScore1 +
				increasingDiagScore2 + increasingDiagScore3;
		
	}
	
	/**
	 * Checks if a Win state has been reached.
	 * @param playerToken
	 * @return boolean if game has been won.
	 */
	public boolean checkWin(char playerToken) {
		return checkRowWin(playerToken) ||
				checkColWin(playerToken) ||
				checkDiagonalWin(playerToken);
	}
	
	/**
	 * Checks if there are 5 consecutive tokens in a row.
	 * @param playerToken
	 * @return If the player has won.
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
	 * Checks if there are 5 tokens in a row in a column.
	 * @param playerToken
	 * @return If the player has won
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
	 * Checks if there is a token 5 in a row in a diagonal
	 * @param playerToken
	 * @return if the player has won
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
