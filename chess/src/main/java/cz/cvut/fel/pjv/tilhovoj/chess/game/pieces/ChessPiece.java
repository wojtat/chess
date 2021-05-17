package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.io.Serializable;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public abstract class ChessPiece implements Serializable {
	private static final long serialVersionUID = -4325746427193672289L;
	
	protected ChessBoard board;
	protected PlayerColor player;
	protected ChessPieces kind;
	
	public static ChessPiece fromFENCharacter(ChessBoard board, char c) {
		PlayerColor color = Character.isLowerCase(c) ? PlayerColor.COLOR_BLACK : PlayerColor.COLOR_WHITE;
		c = Character.toUpperCase(c);
		return fromKind(board, color, ChessPieces.fromSANCharacter(c));
	}
	
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
	
	public ChessPiece(ChessBoard board, PlayerColor player) {
		this.board = board;
		this.player = player;
	}
	
	public PlayerColor getColor() {
		return player;
	}
	
	public ChessPieces getKind() {
		return kind;
	}
	
	protected boolean isActuallyLegal(ChessMoveAction action) {
		board.playMove(action);
		boolean inCheck = board.isKingUnderAttack(player);
		board.unplayMove(action);
		return !inCheck;
	}
	
	public abstract ChessMoveAction getActionFromMove(ChessMove move);
	
	public abstract List<ChessCoord> generateAllControlledCoords(ChessCoord coord);
	
	public abstract List<ChessMoveAction> generateLegalMoves(ChessCoord coord);
}
