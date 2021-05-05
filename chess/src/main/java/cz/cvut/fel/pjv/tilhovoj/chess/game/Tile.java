package cz.cvut.fel.pjv.tilhovoj.chess.game;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;

public class Tile {
	private ChessCoord coord;
	private ChessPiece piece;
	
	public Tile(ChessCoord coord) {
		this.coord = coord;
		this.piece = null;
	}
	
	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}
	
	public ChessPiece getPiece() {
		return piece;
	}
	
	public boolean isEmpty() {
		return piece == null;
	}
}
