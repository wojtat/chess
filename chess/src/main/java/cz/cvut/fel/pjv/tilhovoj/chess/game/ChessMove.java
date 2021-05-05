package cz.cvut.fel.pjv.tilhovoj.chess.game;

public class ChessMove {
	private ChessCoord from;
	private ChessCoord to;
	
	public ChessMove(ChessCoord from, ChessCoord to) {
		this.from = from;
		this.to = to;
	}
}
