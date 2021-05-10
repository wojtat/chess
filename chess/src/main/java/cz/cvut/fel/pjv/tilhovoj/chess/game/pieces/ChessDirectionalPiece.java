package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public abstract class ChessDirectionalPiece extends ChessPiece {

	public ChessDirectionalPiece(ChessBoard board, PlayerColor player) {
		super(board, player);
	}

	protected void doDirection(List<ChessMove> moves, List<ChessCoord> direction, ChessCoord coord) {
		// TODO: Add actual movement restrictions
		for (ChessCoord candidate : direction) {
			if (board.getTileAt(candidate).isEmpty()) {
				moves.add(new ChessMove(coord, candidate));
			}
			else {
				break;
			}
		}
	}
}
