package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public class ChessKing extends ChessPiece {
	
	public ChessKing(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_KING;
	}

	@Override
	public List<ChessMove> generatePossibleMoves(ChessCoord coord) {
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
