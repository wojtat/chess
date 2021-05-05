package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public class ChessKing extends ChessPiece {
	
	public ChessKing(ChessBoard board, ChessCoord coord, PlayerColor player) {
		super(board, coord, player);
	}

	@Override
	public List<ChessMove> generatePossibleMoves() {
		List<ChessMove> moves = new ArrayList<>();
		
		for (ChessCoord candidate : coord.getNeighbours()) {
			// TODO: Add actual movement restrictions
			if (board.getTileAt(candidate).isEmpty()) {
				moves.add(new ChessMove(coord, candidate));
			}
		}
		
		return moves;
	}
}
