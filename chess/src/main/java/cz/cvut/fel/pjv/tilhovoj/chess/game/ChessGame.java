package cz.cvut.fel.pjv.tilhovoj.chess.game;

public class ChessGame {
	
	private ChessClock clock;
	private ChessBoard board;
	
	public ChessGame(ChessClock clock, ChessBoard board) {
		this.clock = clock;
		this.board = board;
	}
	
	public ChessBoard getBoard() {
		return board;
	}
	
	public ChessClock getClock() {
		return clock;
	}
}
