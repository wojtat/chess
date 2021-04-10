package cz.cvut.fel.pjv.tilhovoj.chess.game;

public class ChessGame {
	
	private ChessClock clock;
	
	public ChessGame(ChessClock clock) {
		this.clock = clock;
	}
	
	public ChessClock getClock() {
		return clock;
	}
}
