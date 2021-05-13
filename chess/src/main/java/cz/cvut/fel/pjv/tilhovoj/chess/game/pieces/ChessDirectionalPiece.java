package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public abstract class ChessDirectionalPiece extends ChessPiece {

	public ChessDirectionalPiece(ChessBoard board, PlayerColor player) {
		super(board, player);
	}

	protected void doMovementDirection(List<ChessMove> moves, List<ChessCoord> direction, ChessCoord coord) {
		// TODO: Add actual movement restrictions
		for (ChessCoord candidate : direction) {
			if (board.getTileAt(candidate).isEmpty()) {
				moves.add(new ChessMove(coord, candidate));
			} else if (board.getTileAt(candidate).getPiece().player != this.player) {
				moves.add(new ChessMove(coord, candidate));
				break;
			} else {
				break;
			}
		}
	}
	
	protected void doControllDirection(List<ChessCoord> coords, List<ChessCoord> direction, ChessCoord coord) {
		for (ChessCoord candidate : direction) {
			coords.add(candidate);
			if (!board.getTileAt(candidate).isEmpty()) {
				break;
			}
		}
	}

	@Override
	public ChessMoveAction getActionFromMove(ChessMove move) {
		ChessMoveAction.Builder builder = new ChessMoveAction.Builder(move, player);
		if (!board.getTileAt(move.getTo()).isEmpty()) {
			builder.isCapture(move.getTo(), board);
		}

		ChessMoveAction action = builder.build();
		return action;
	}
}
