package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.Optional;

/**
 * Represents the kind of a chess piece.
 */
public enum ChessPieces {
	PIECE_KING,
	PIECE_QUEEN,
	PIECE_ROOK,
	PIECE_KNIGHT,
	PIECE_BISHOP,
	PIECE_PAWN;
	
	/**
	 * Get the kind of the piece from a SAN character.
	 * @param c the character representing a piece in standard algebraic notation.
	 * @return the chess piece kind that corresponds to the given character.
	 */
	public static Optional<ChessPieces> fromSANCharacter(char c) {
		switch (c) {
		case 'P':
			return Optional.of(PIECE_PAWN);
		case 'N':
			return Optional.of(PIECE_KNIGHT);
		case 'B':
			return Optional.of(PIECE_BISHOP);
		case 'R':
			return Optional.of(PIECE_ROOK);
		case 'Q':
			return Optional.of(PIECE_QUEEN);
		case 'K':
			return Optional.of(PIECE_KING);
		default:
			return Optional.empty();
		}
	}
	
	/**
	 * Get a character representing this piece kind.
	 * @param kind the kind of the chess piece.
	 * @return the character representing this kind of chess piece in standard algebraic notation.
	 */
	public static Optional<Character> toSANCharacter(ChessPieces kind) {
		switch (kind) {
		case PIECE_PAWN:
			return Optional.of('P');
		case PIECE_KNIGHT:
			return Optional.of('N');
		case PIECE_BISHOP:
			return Optional.of('B');
		case PIECE_ROOK:
			return Optional.of('R');
		case PIECE_QUEEN:
			return Optional.of('Q');
		case PIECE_KING:
			return Optional.of('K');
		default:
			return Optional.empty();
		}
	}
	
	/**
	 * An array of all the pieces that can be promoted.
	 */
	public static final ChessPieces[] PROMOTABLE_PIECES = new ChessPieces[] { PIECE_QUEEN, PIECE_ROOK, PIECE_KNIGHT, PIECE_BISHOP };
}
