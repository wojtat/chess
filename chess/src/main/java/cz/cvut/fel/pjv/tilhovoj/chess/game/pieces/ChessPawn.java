package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessPawn extends ChessPiece {

	public ChessPawn(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_PAWN;
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
