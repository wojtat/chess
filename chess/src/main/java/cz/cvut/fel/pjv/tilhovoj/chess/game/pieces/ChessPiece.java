package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.io.Serializable;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

/**
 * Represents an abstract piece that lives on a chess board.
 */
public abstract class ChessPiece implements Serializable {
	private static final long serialVersionUID = -4325746427193672289L;
	
	protected ChessBoard board;
	protected PlayerColor player;
	protected ChessPieces kind;
	
	/**
	 * Interprets the character as a piece.
	 * @param board the board this piece will live on.
	 * @param c the character to be interpreted.
	 * @return the piece that corresponds to the specified character that will live on the chess board.
	 */
	public static ChessPiece fromFENCharacter(ChessBoard board, char c) {
		PlayerColor color = Character.isLowerCase(c) ? PlayerColor.COLOR_BLACK : PlayerColor.COLOR_WHITE;
		c = Character.toUpperCase(c);
		return fromKind(board, color, ChessPieces.fromSANCharacter(c));
	}
	
	/**
	 * Get a new piece from the pice kind and player color.
	 * @param board the board this piece will live on.
	 * @param color the owner of the piece.
	 * @param kind the kind of the piece.
	 * @return a concrete piece represented by the piece kind.
	 */
	public static ChessPiece fromKind(ChessBoard board, PlayerColor color, ChessPieces kind) {
		switch (kind) {
		case PIECE_PAWN:
			return new ChessPawn(board, color);
		case PIECE_BISHOP:
			return new ChessBishop(board, color);
		case PIECE_KNIGHT:
			return new ChessKnight(board, color);
		case PIECE_ROOK:
			return new ChessRook(board, color);
		case PIECE_QUEEN:
			return new ChessQueen(board, color);
		case PIECE_KING:
			return new ChessKing(board, color);
		default:
			return null;
		}
	}
	
	/**
	 * Constructs a new abstract chess piece.
	 * @param board the board this piece will be on.
	 * @param player the owner of this piece.
	 */
	public ChessPiece(ChessBoard board, PlayerColor player) {
		this.board = board;
		this.player = player;
	}
	
	/**
	 * Get the owner of the piece.
	 * @return the owner of the piece.
	 */
	public PlayerColor getColor() {
		return player;
	}
	
	/**
	 * Get the kind of the piece.
	 * @return the kind of the piece.
	 */
	public ChessPieces getKind() {
		return kind;
	}
	
	protected boolean isActuallyLegal(ChessMoveAction action) {
		board.playMove(action);
		boolean inCheck = board.isKingUnderAttack(player);
		board.unplayMove(action);
		return !inCheck;
	}
	
	/**
	 * Generate a more informative chess move action object representing the move.
	 * @param move the move specification.
	 * @return a more informative chess move action object representing the move.
	 */
	public abstract ChessMoveAction getActionFromMove(ChessMove move);
	
	/**
	 * Generate a list of all coordinates controlled by the piece.
	 * @param coord the position of the piece on the chess board.
	 * @return an unmodifiable list of all coordinates controlled by this piece. 
	 */
	public abstract List<ChessCoord> generateAllControlledCoords(ChessCoord coord);
	
	/**
	 * Generate a list of all the legal moves of this piece.
	 * @param coord the position of the piece on the chess board.
	 * @return an unmodifiable list of all legal chess moves that this piece has.
	 */
	public abstract List<ChessMoveAction> generateLegalMoves(ChessCoord coord);
}
