package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;

public class Tile implements Serializable {
	private static final long serialVersionUID = 1021081503421776885L;
	
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
	
	public ChessCoord getCoord() {
		return coord;
	}
	
	public boolean isEmpty() {
		return piece == null;
	}
}
