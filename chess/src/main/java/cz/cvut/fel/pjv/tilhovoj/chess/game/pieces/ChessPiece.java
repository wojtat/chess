package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public abstract class ChessPiece {
	protected ChessBoard board;
	protected PlayerColor player;
	protected ChessPieces kind;
	
	public static ChessPiece fromFENCharacter(ChessBoard board, char c) {
		PlayerColor color = Character.isLowerCase(c) ? PlayerColor.COLOR_BLACK : PlayerColor.COLOR_WHITE;
		c = Character.toLowerCase(c);
		switch (c) {
		case 'p':
			return new ChessPawn(board, color);
		case 'n':
			return new ChessKnight(board, color);
		case 'b':
			return new ChessBishop(board, color);
		case 'r':
			return new ChessRook(board, color);
		case 'q':
			return new ChessQueen(board, color);
		case 'k':
			return new ChessKing(board, color);
		default:
			return null;
		}
	}
	
	public static ChessPiece fromKind(ChessBoard board, PlayerColor color, ChessPieces kind) {
		switch (kind) {
		case PIECE_BISHOP:
			return new ChessBishop(board, color);
		case PIECE_KNIGHT:
			return new ChessKnight(board, color);
		case PIECE_ROOK:
			return new ChessRook(board, color);
		case PIECE_QUEEN:
			return new ChessQueen(board, color);
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
	
	public abstract ChessMoveAction getActionFromMove(ChessMove move);
	
	public abstract List<ChessCoord> generateAllControlledCoords(ChessCoord coord);
	
	public abstract List<ChessMove> generateLegalMoves(ChessCoord coord);
}
