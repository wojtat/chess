package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

/**
 * Represents the kind of a chess piece
 */
public enum ChessPieces {
	PIECE_KING,
	PIECE_QUEEN,
	PIECE_ROOK,
	PIECE_KNIGHT,
	PIECE_BISHOP,
	PIECE_PAWN;
	
	/**
	 * @param c the character representing a piece in standard algebraic notation
	 * @return the chess piece kind that corresponds to the given character
	 */
	public static ChessPieces fromSANCharacter(char c) {
		switch (c) {
		case 'P':
			return PIECE_PAWN;
		case 'N':
			return PIECE_KNIGHT;
		case 'B':
			return PIECE_BISHOP;
		case 'R':
			return PIECE_ROOK;
		case 'Q':
			return PIECE_QUEEN;
		case 'K':
			return PIECE_KING;
		default:
			return null;
		}
	}
	
	/**
	 * @param kind the kind of the chess piece
	 * @return the character representing this kind of chess piece in standard algebraic notation
	 */
	public static char toSANCharacter(ChessPieces kind) {
		switch (kind) {
		case PIECE_PAWN:
			return 'P';
		case PIECE_KNIGHT:
			return 'N';
		case PIECE_BISHOP:
			return 'B';
		case PIECE_ROOK:
			return 'R';
		case PIECE_QUEEN:
			return 'Q';
		case PIECE_KING:
			return 'K';
		}
		return 0;
	}
	
	/**
	 * An array of all the pieces that can be promoted
	 */
	public static final ChessPieces[] PROMOTABLE_PIECES = new ChessPieces[] { PIECE_QUEEN, PIECE_ROOK, PIECE_KNIGHT, PIECE_BISHOP };
}
