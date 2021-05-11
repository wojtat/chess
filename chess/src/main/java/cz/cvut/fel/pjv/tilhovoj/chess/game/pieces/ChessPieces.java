package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

public enum ChessPieces {
	PIECE_KING,
	PIECE_QUEEN,
	PIECE_ROOK,
	PIECE_KNIGHT,
	PIECE_BISHOP,
	PIECE_PAWN;
	
	public static final ChessPieces[] PROMOTABLE_PIECES = new ChessPieces[] { PIECE_QUEEN, PIECE_ROOK, PIECE_KNIGHT, PIECE_BISHOP };
}
