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
	
	public void playMove(ChessMove move) {
		if (beforeStartGame || currentMove != moveList.size()) {
			return;
		}
		ChessMoveAction action = board.getTileAt(move.getFrom()).getPiece().getActionFromMove(move);
		board.playMove(action);
		
		++currentMove;
		moveList.add(action);
	}
}
