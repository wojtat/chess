package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessKnight extends ChessPiece {

	public ChessKnight(ChessBoard board, ChessCoord coord, PlayerColor player) {
		super(board, coord, player);
		this.kind = ChessPieces.PIECE_KNIGHT;
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
