package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;

public class ChessGame {

	private boolean beforeStartGame;
	private ChessClock clock;
	private ChessBoard board;
	private int currentMove;
	private List<ChessMoveAction> moveList;
	
	public ChessGame(ChessClock clock, ChessBoard board) {
		this.clock = clock;
		this.board = board;
		this.currentMove = 0;
		this.moveList = new ArrayList<>();
		this.beforeStartGame = true;
	}
	
	public void startGame() {
		beforeStartGame = false;
		clock.start();
	}
	
	public boolean beforeStartGame() {
		return beforeStartGame;
	}
	
	public ChessBoard getBoard() {
		return board;
	}
	
	public ChessClock getClock() {
		return clock;
	}
	
	public boolean isUpdated() {
		return currentMove == moveList.size();
	}
	
	public boolean goToPreviousMove() {
		if (currentMove <= 0) {
			return false;
		}
		board.unplayMove(moveList.get(--currentMove));
		return true;
	}
	
	public boolean goToNextMove() {
		if (isUpdated()) {
			return false;
		}
		board.playMove(moveList.get(currentMove++));
		return true;
	}
	
	public void playMove(ChessMove move) {
		if (beforeStartGame || currentMove != moveList.size()) {
			return;
		}
		ChessMoveAction action = board.getTileAt(move.getFrom()).getPiece().getActionFromMove(move);
		board.playMove(action);
		clock.hit();
		
		++currentMove;
		moveList.add(action);
		
		int numLegalMoves = board.getNumLegalMoves();
		if (numLegalMoves == 0) {
			// This is the end of the game
			beforeStartGame = true;
			clock.stop();
			if (board.isKingUnderAttack(board.getOnTurn())) {
				// Checkmate
				System.out.println("CHECKMATE, " + PlayerColor.getPrevious(board.getOnTurn()) + " wins.");
			} else {
				// Stalemate
				System.out.println("STALEMATE.");
			}
		}
	}
}
