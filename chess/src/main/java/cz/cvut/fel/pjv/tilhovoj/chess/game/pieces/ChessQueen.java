package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessQueen extends ChessDirectionalPiece {

	public ChessQueen(ChessBoard board, ChessCoord coord, PlayerColor player) {
		super(board, coord, player);
	}
	
	@Override
	public List<ChessMove> generatePossibleMoves() {
		List<ChessMove> moves = new ArrayList<>();

		doDirection(moves, coord.getAllUp());
		doDirection(moves, coord.getAllDown());
		doDirection(moves, coord.getAllLeft());
		doDirection(moves, coord.getAllRight());
		doDirection(moves, coord.getAllUpLeft());
		doDirection(moves, coord.getAllUpRight());
		doDirection(moves, coord.getAllDownLeft());
		doDirection(moves, coord.getAllDownRight());
		
		return moves;
	}
}
