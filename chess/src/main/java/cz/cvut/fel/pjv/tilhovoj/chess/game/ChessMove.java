package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;

/**
 * Represents a chess move as a source and destination squares alongside a promotion piece,
 * if the move is a pawn promotion.
 */
public class ChessMove implements Serializable {
	private static final long serialVersionUID = 6750787645427082709L;
	
	private ChessCoord from;
	private ChessCoord to;
	private ChessPieces promoteToKind;
	
	/**
	 * Constructs a new chess move from the source coordinate, destination coordinate and the promotion piece kind.
	 * @param from the source coordinate.
	 * @param to the destination coordinate.
	 * @param promoteToKind the promotion piece kind.
	 */
	public ChessMove(ChessCoord from, ChessCoord to, ChessPieces promoteToKind) {
		this.from = from;
		this.to = to;
		this.promoteToKind = promoteToKind;
	}

	/**
	 * Constructs a new chess move from the source coordinate and destination coordinate.
	 * @param from the source coordinate.
	 * @param to the destination coordinate.
	 */
	public ChessMove(ChessCoord from, ChessCoord to) {
		this(from, to, null);
	}

	/**
	 * Get the source coordinate.
	 * @return the source coordinate.
	 */
	public ChessCoord getFrom() {
		return from;
	}

	/**
	 * Get the destination coordinate.
	 * @return the destination coordinate.
	 */
	public ChessCoord getTo() {
		return to;
	}
	
	/**
	 * Get the promotion piece kind.
	 * @return the promotion piece kind, if it is a promotion, null otherwise.
	 */
	public ChessPieces getPromoteToKind() {
		return promoteToKind;
	}
}
