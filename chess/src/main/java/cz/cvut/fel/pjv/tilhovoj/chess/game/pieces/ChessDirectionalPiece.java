package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.EnumSet;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public abstract class ChessDirectionalPiece extends ChessPiece {

	/**
	 * Constructs a new abstract directional piece, which means it can't jump over
	 * other pieces and moves in a predefined set of directions.
	 * @param board the board this piece will be on.
	 * @param player the owner of this piece.
	 */
	public ChessDirectionalPiece(ChessBoard board, PlayerColor player) {
		super(board, player);
	}

	protected void doMovementDirection(List<ChessMoveAction> moves, List<ChessCoord> direction, ChessCoord coord) {
		for (ChessCoord candidate : direction) {
			if (board.getTileAt(candidate).isEmpty()) {
				ChessMoveAction action = getActionFromMove(new ChessMove(coord, candidate));
				if (isActuallyLegal(action)) {
					moves.add(action);
				}
			} else if (board.getTileAt(candidate).getPiece().player != this.player) {
				ChessMoveAction action = getActionFromMove(new ChessMove(coord, candidate));
				if (isActuallyLegal(action)) {
					moves.add(action);
				}
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
		ChessMoveAction.Builder builder = new ChessMoveAction.Builder(move, player, board);
		if (!board.getTileAt(move.getTo()).isEmpty()) {
			builder.isCapture(move.getTo(), board);
		}

		ChessMoveAction action = builder.build();
		return action;
	}
}
