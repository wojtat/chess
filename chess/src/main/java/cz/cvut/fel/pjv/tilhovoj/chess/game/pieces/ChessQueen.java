package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessQueen extends ChessDirectionalPiece {

	public ChessQueen(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_QUEEN;
	}
	
	@Override
	public List<ChessMove> generatePossibleMoves(ChessCoord coord) {
		List<ChessMove> moves = new ArrayList<>();

		doDirection(moves, coord.getAllUp(), coord);
		doDirection(moves, coord.getAllDown(), coord);
		doDirection(moves, coord.getAllLeft(), coord);
		doDirection(moves, coord.getAllRight(), coord);
		doDirection(moves, coord.getAllUpLeft(), coord);
		doDirection(moves, coord.getAllUpRight(), coord);
		doDirection(moves, coord.getAllDownLeft(), coord);
		doDirection(moves, coord.getAllDownRight(), coord);
		
		return moves;
	}
}
