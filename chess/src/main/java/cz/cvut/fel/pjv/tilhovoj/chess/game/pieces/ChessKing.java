package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction.Builder;

public class ChessKing extends ChessPiece {
	
	public ChessKing(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_KING;
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
		
		return moves;
	}

	@Override
	public ChessMoveAction getActionFromMove(ChessMove move) {
		ChessMoveAction.Builder builder = new ChessMoveAction.Builder(move);
		
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
