package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public abstract class ChessPiece {
	protected ChessBoard board;
	protected PlayerColor player;
	protected ChessPieces kind;
	
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
	
	public abstract List<ChessMove> generatePossibleMoves(ChessCoord coord);
}
