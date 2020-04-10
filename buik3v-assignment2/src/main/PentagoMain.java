package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.AIPlayer;
import model.GameState;

public class PentagoMain {

	public static void main(String[] args) {
		List<String> moveHistory = new ArrayList<String>();
		GameState gs = new GameState();
		Scanner scan = new Scanner(System.in);
		
 		Random rand = new Random();
 		int firstMove = rand.nextInt(2);
 		boolean player1Turn = false;
 		boolean player2Turn = false;
 		if(firstMove == 0) {
 			player1Turn = true;
 		}
 		else {
 			player2Turn = true;
 		}
 		if(player1Turn) {
 	 		System.out.println("Player 1 Name (player who moves first)");
 		}
 		else {
 	 		System.out.println("Player 1 Name");
 		}
 		String player1 = scan.next();
 		
 		if(player2Turn) {
 	 		System.out.println("Player 2 Name (player who moves first)");

 		}
 		else {
 	 		System.out.println("Player 2 Name");

 		}
 		String player2 = scan.next();

 		System.out.println("Player 1 Token Color (B or W)");
 		String player1Token = scan.next().toLowerCase();
 		char aiPlayerToken = 0;
 		if(player1Token.equals("b")) {
 			aiPlayerToken = 'w';
 		}
 		else {
 			aiPlayerToken = 'b';
 		}
 		System.out.println("Player 2 Token Color (B or W) " + aiPlayerToken);
 		AIPlayer aiPlayer = new AIPlayer(gs, aiPlayerToken);

 		boolean player1Won = false;
 		boolean player2Won = false;
 		while(!player1Won && !player2Won && !gs.checkFullBoard()) {
 			gs.print2DArray();
 			System.out.println(moveHistory.toString());
 			
 			if(player1Turn) {
 				System.out.println("Enter a move");
 				String posQuad = scan.next();
 				String[] placement = posQuad.split("/");
 				int quadrant = Integer.parseInt(placement[0]);
 				int position = Integer.parseInt(placement[1]);
 				
 				String quadRotate = scan.next();
 				int quadrantRotation = Integer.parseInt(quadRotate.charAt(0) + "");
 				char rotation = quadRotate.charAt(1);
 				
 				gs.markCell(quadrant, position,  aiPlayer.getOppToken());
 				aiPlayer.setBoard(gs.getBoard());
 				player1Won = gs.checkWin(aiPlayer.getOppToken());
 				
 				if(rotation == 'L' || rotation == 'l') {
 					char[][] copy = gs.copy2DArray(gs.getBoard(), 6, 6);
 					char[][] rotated = gs.rotateBlockLeft(copy, quadrantRotation);
 					gs.setBoard(rotated);
 				}
 				else {
 					char[][] copy = gs.copy2DArray(gs.getBoard(), 6, 6);
 					char[][] rotated = gs.rotateBlockRight(copy, quadrantRotation);
 					gs.setBoard(rotated);
 				}
 				moveHistory.add(player1 + ": " + posQuad +
 						" " + quadRotate);
 				
 				aiPlayer.setBoard(gs.getBoard());
 				player1Won = gs.checkWin(aiPlayer.getOppToken());
 				player2Won = gs.checkWin(aiPlayer.getAIToken());
 				player1Turn = false;
 				player2Turn = true;
 			}
 			else {
 				int[] aiMove = aiPlayer.bestPositionQuadrant();
 				gs.markCell(aiMove[1], aiMove[0], aiPlayer.getAIToken());
 				aiPlayer.setBoard(gs.getBoard());
 				player2Won = gs.checkWin(aiPlayer.getAIToken());
 				
 				int quadRotate = rand.nextInt(4) + 1;
 				int rotateDir = rand.nextInt(2);
 				String rotation = "";
 				if(rotateDir == 0) {
 					rotation = "R";
 					char[][] copy = gs.copy2DArray(gs.getBoard(), 6, 6);
 					char[][] rotated = gs.rotateBlockRight(copy, quadRotate);
 					gs.setBoard(rotated);
 				}
 				else {
 					rotation = "L";
 					char[][] copy = gs.copy2DArray(gs.getBoard(), 6, 6);
 					char[][] rotated = gs.rotateBlockLeft(copy, quadRotate);
 					gs.setBoard(rotated);

 				}
 				aiPlayer.setBoard(gs.getBoard());
 				moveHistory.add(player2 + ": " + aiMove[1] + "/" + aiMove[0] +
 						" " + quadRotate + rotation);
 				player2Won = gs.checkWin(aiPlayer.getAIToken());
 				player1Won = gs.checkWin(aiPlayer.getOppToken());
 				player1Turn = true;
 				player2Turn = false;

 			}
 		}
 		
 		if(player1Won && !player2Won) {
 			System.out.println("Player 1 has Won!");
 			System.out.println("Here is the resulting board!");
 			gs.print2DArray();
 			System.out.println("Also the past history of all moves made!");
 			System.out.println(moveHistory.toString());
 		}
 		else if((player1Won && player2Won) || (!player1Won && !player2Won)) {
 			System.out.println("Draw!");
 			System.out.println("Here is the resulting board!");
 			gs.print2DArray();
 			System.out.println("Also the past history of all moves made!");
 			System.out.println(moveHistory.toString());
 		}
 		else if(!player1Won && player2Won) {
 			System.out.println("Player 2 has Won!");
 			System.out.println("Here is the resulting board!");
 			gs.print2DArray();
 			System.out.println("Also the past history of all moves made!");
 			System.out.println(moveHistory.toString());
 		}
			System.out.println(aiPlayer.nodesExpanded);

 		scan.close();
	}

}
