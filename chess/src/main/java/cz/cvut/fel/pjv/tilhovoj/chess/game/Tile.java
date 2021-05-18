package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;

/**
 * Holds a piece present on a coordinate. Represents a single square in a chess board.
 */
public class Tile implements Serializable {
	private static final long serialVersionUID = 1021081503421776885L;
	
	private ChessCoord coord;
	private ChessPiece piece;
	
	/**
	 * Constructs an empty square with a coordinate
	 * @param coord the coordinate of the tile
	 */
	public Tile(ChessCoord coord) {
		this.coord = coord;
		this.piece = null;
	}
	
	/**
	 * @param piece the new piece that will reside on this tile, or null
	 */
	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}
	
	/**
	 * @return the piece that resides on this tile, or null if empty
	 */
	public ChessPiece getPiece() {
		return piece;
	}
	
	/**
	 * @return the coordinate of this tile
	 */
	public ChessCoord getCoord() {
		return coord;
	}
	
	/**
	 * @return true if the tile is empty, false otherwise
	 */
	public boolean isEmpty() {
		return piece == null;
	}
}
