package cz.cvut.fel.pjv.tilhovoj.chess.game;

/**
 * The castling right specifies whether the given castling move
 * can be played in a future position, it doesn't mean that castling
 * in the given position is a legal move
 */
public enum ChessCastlingRight {
	WHITE_KINGSIDE,
	WHITE_QUEENSIDE,
	BLACK_KINGSIDE,
	BLACK_QUEENSIDE;
}
