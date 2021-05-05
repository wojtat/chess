package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public abstract class ChessPiece {
	protected ChessBoard board;
	protected ChessCoord coord;
	protected PlayerColor player;
	
	public ChessPiece(ChessBoard board, ChessCoord coord, PlayerColor player) {
		this.board = board;
		this.coord = coord;
		this.player = player;
	}
	
	public abstract List<ChessMove> generatePossibleMoves();
}
