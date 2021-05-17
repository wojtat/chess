package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

public enum ChessPieces {
	PIECE_KING,
	PIECE_QUEEN,
	PIECE_ROOK,
	PIECE_KNIGHT,
	PIECE_BISHOP,
	PIECE_PAWN;
	
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
	
	public static final ChessPieces[] PROMOTABLE_PIECES = new ChessPieces[] { PIECE_QUEEN, PIECE_ROOK, PIECE_KNIGHT, PIECE_BISHOP };
}
