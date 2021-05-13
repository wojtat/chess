package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction.Builder;

public class ChessKing extends ChessPiece {
	private final ChessCoord LONG_CASTLE_DESTINATION;
	private final ChessCoord SHORT_CASTLE_DESTINATION;
	
	public ChessKing(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_KING;
		this.SHORT_CASTLE_DESTINATION = new ChessCoord(player == PlayerColor.COLOR_WHITE ? 1 : 8, 7);
		this.LONG_CASTLE_DESTINATION = new ChessCoord(player == PlayerColor.COLOR_WHITE ? 1 : 8, 3);
	}
	
	private boolean canCastleShort() {
		if (player == PlayerColor.COLOR_WHITE) {
			return board.whiteCanCastleShort();
		} else {
			return board.blackCanCastleShort();
		}
	}

	private boolean canCastleLong() {
		if (player == PlayerColor.COLOR_WHITE) {
			return board.whiteCanCastleLong();
		} else {
			return board.blackCanCastleLong();
		}
	}

	@Override
	public List<ChessMove> generateLegalMoves(ChessCoord coord) {
		List<ChessMove> moves = new ArrayList<>();
		
		for (ChessCoord candidate : coord.getNeighbours()) {
			// TODO: Add actual movement restrictions
			if (board.getTileAt(candidate).isEmpty() || board.getTileAt(candidate).getPiece().player != this.player) {
				moves.add(new ChessMove(coord, candidate));
			}
		}
		
		if (canCastleShort()) {
			ChessCoord firstOccupiedSquare = null;
			List<ChessCoord> right = coord.getAllRight();
			
			PlayerColor otherPlayer = PlayerColor.getNext(player);
			if (!board.isUnderAttackBy(coord, otherPlayer)
					&& !board.isUnderAttackBy(right.get(0), otherPlayer)
					&& !board.isUnderAttackBy(right.get(1), otherPlayer)) {
				
				// King's path is not attacked by enemy pieces
				for (ChessCoord castlingLine : right) {
					if (!board.getTileAt(castlingLine).isEmpty()) {
						firstOccupiedSquare = castlingLine;
						break;
					}
				}
				if (firstOccupiedSquare != null && firstOccupiedSquare.getFile() == 8) {
					moves.add(new ChessMove(coord, SHORT_CASTLE_DESTINATION));
				}
			}
		}
		if (canCastleLong()) {
			ChessCoord firstOccupiedSquare = null;
			List<ChessCoord> left = coord.getAllLeft();
			
			PlayerColor otherPlayer = PlayerColor.getNext(player);
			if (!board.isUnderAttackBy(coord, otherPlayer)
					&& !board.isUnderAttackBy(left.get(0), otherPlayer)
					&& !board.isUnderAttackBy(left.get(1), otherPlayer)) {
				
				// King's path is not attacked by enemy pieces
				for (ChessCoord castlingLine : left) {
					if (!board.getTileAt(castlingLine).isEmpty()) {
						firstOccupiedSquare = castlingLine;
						break;
					}
				}
				if (firstOccupiedSquare != null && firstOccupiedSquare.getFile() == 1) {
					moves.add(new ChessMove(coord, LONG_CASTLE_DESTINATION));
				}
			}
		}
		
		return moves;
	}

	@Override
	public List<ChessCoord> generateAllControlledCoords(ChessCoord coord) {
		return coord.getNeighbours();
	}

	@Override
	public ChessMoveAction getActionFromMove(ChessMove move) {
		ChessMoveAction.Builder builder = new ChessMoveAction.Builder(move, player, board);
		
		int fileDifference = move.getTo().getFile() - move.getFrom().getFile();
		if (!board.getTileAt(move.getTo()).isEmpty()) {
			builder.isCapture(move.getTo(), board);
		} else if (Math.abs(fileDifference) > 1) {
			// Moved more than 1 tile, so it must be castling
			builder.isCastle(fileDifference > 0);
		}
		
		ChessMoveAction action = builder.build();
		return action;
	}
}
