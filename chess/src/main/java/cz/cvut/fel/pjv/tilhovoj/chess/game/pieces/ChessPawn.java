package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

/**
 * Represents a standard chess pawn piece.
 */
public class ChessPawn extends ChessPiece {
	private final int PROMOTION_RANK;
	private final int MOVE_DIRECTION;
	private final int START_RANK;

	/**
	 * Constructs a new pawn piece.
	 * @param board the board this piece will be on.
	 * @param player the owner of this piece.
	 */
	public ChessPawn(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_PAWN;
		this.PROMOTION_RANK = this.player == PlayerColor.COLOR_WHITE ? 8 : 1;
		this.MOVE_DIRECTION = this.player == PlayerColor.COLOR_WHITE ? 1 : -1;
		this.START_RANK = this.player == PlayerColor.COLOR_WHITE ? 2 : 7;
	}

	@Override
	public List<ChessMoveAction> generateLegalMoves(ChessCoord coord) {
		List<ChessMoveAction> moves = new ArrayList<>();
		
		ChessCoord forward = new ChessCoord(coord.getRank() + MOVE_DIRECTION, coord.getFile());
		ChessCoord forwardLeft = new ChessCoord(coord.getRank() + MOVE_DIRECTION, coord.getFile() - 1);
		ChessCoord forwardRight = new ChessCoord(coord.getRank() + MOVE_DIRECTION, coord.getFile() + 1);
		if (forward.isValid() && board.getTileAt(forward).isEmpty()) {
			if (forward.getRank() == PROMOTION_RANK) {
				for (ChessPieces promoteToKind : ChessPieces.PROMOTABLE_PIECES) {
					ChessMoveAction action = getActionFromMove(new ChessMove(coord, forward, promoteToKind));
					if (isActuallyLegal(action)) {
						moves.add(action);
					}
				}
			} else {
				ChessMoveAction action = getActionFromMove(new ChessMove(coord, forward));
				if (isActuallyLegal(action)) {
					moves.add(action);
				}
			}
			
			if (coord.getRank() == START_RANK) {
				// Can also move two squares up
				ChessCoord forwardTwo = new ChessCoord(coord.getRank() + 2*MOVE_DIRECTION, coord.getFile());
				if (board.getTileAt(forwardTwo).isEmpty()) {
					ChessMoveAction action = getActionFromMove(new ChessMove(coord, forwardTwo));
					if (isActuallyLegal(action)) {
						moves.add(action);
					}
				}
			}
		}
		// if (is valid && (is en passant coord || is enemy piece)):
		if (forwardLeft.isValid() 
			&& (forwardLeft.equals(board.getEnPassantCoord())
			|| (!board.getTileAt(forwardLeft).isEmpty() && board.getTileAt(forwardLeft).getPiece().player != this.player))) {
			
			if (forwardLeft.getRank() == PROMOTION_RANK) {
				for (ChessPieces promoteToKind : ChessPieces.PROMOTABLE_PIECES) {
					ChessMoveAction action = getActionFromMove(new ChessMove(coord, forwardLeft, promoteToKind));
					if (isActuallyLegal(action)) {
						moves.add(action);
					}
				}
			} else {
				ChessMoveAction action = getActionFromMove(new ChessMove(coord, forwardLeft));
				if (isActuallyLegal(action)) {
					moves.add(action);
				}
			}
		}
		// if (is valid && (is en passant coord || is enemy piece)):
		if (forwardRight.isValid() 
			&& (forwardRight.equals(board.getEnPassantCoord())
			|| (!board.getTileAt(forwardRight).isEmpty() && board.getTileAt(forwardRight).getPiece().player != this.player))) {

			if (forwardRight.getRank() == PROMOTION_RANK) {
				for (ChessPieces promoteToKind : ChessPieces.PROMOTABLE_PIECES) {
					ChessMoveAction action = getActionFromMove(new ChessMove(coord, forwardRight, promoteToKind));
					if (isActuallyLegal(action)) {
						moves.add(action);
					}
				}
			} else {
				ChessMoveAction action = getActionFromMove(new ChessMove(coord, forwardRight));
				if (isActuallyLegal(action)) {
					moves.add(action);
				}				
			}
		}
		
		return Collections.unmodifiableList(moves);
	}

	@Override
	public List<ChessCoord> generateAllControlledCoords(ChessCoord coord) {
		List<ChessCoord> coords = new ArrayList<>();
		
		ChessCoord forwardLeft = new ChessCoord(coord.getRank() + MOVE_DIRECTION, coord.getFile() - 1);
		ChessCoord forwardRight = new ChessCoord(coord.getRank() + MOVE_DIRECTION, coord.getFile() + 1);
		if (forwardLeft.isValid()) {
			coords.add(forwardLeft);
		}
		if (forwardRight.isValid()) {
			coords.add(forwardRight);
		}
		
		return Collections.unmodifiableList(coords);
	}

	@Override
	public ChessMoveAction getActionFromMove(ChessMove move) {
		ChessMoveAction.Builder builder = new ChessMoveAction.Builder(move, player, board);
		
		int rankDifference = move.getTo().getRank() - move.getFrom().getRank();
		int fileDifference = move.getTo().getFile() - move.getFrom().getFile();
		if (!board.getTileAt(move.getTo()).isEmpty()) {
			builder.isCapture(move.getTo(), board);
		} else if (fileDifference != 0) {
			// Must be en passant
			ChessCoord toBeCaptured = new ChessCoord(board.getEnPassantCoord().getRank() - MOVE_DIRECTION, board.getEnPassantCoord().getFile());
			builder.isCapture(toBeCaptured, board);
		}
		if (Math.abs(rankDifference) > 1) {
			// Is a double tile move
			ChessCoord enPassantCoord = new ChessCoord(move.getFrom().getRank() + MOVE_DIRECTION, move.getFrom().getFile());
			builder.isPawnDoubleMove(enPassantCoord);
		}
		if (move.getTo().getRank() == PROMOTION_RANK) {
			// TODO: Add the concept of promotion to different pieces
			builder.isPromotion(move.getPromoteToKind());
		}
		
		ChessMoveAction action = builder.build();
		return action;
	}
}
