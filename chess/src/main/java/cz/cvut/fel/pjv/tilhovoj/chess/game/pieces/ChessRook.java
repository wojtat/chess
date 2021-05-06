package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessRook extends ChessDirectionalPiece {

	public ChessRook(ChessBoard board, ChessCoord coord, PlayerColor player) {
		super(board, coord, player);
		this.kind = ChessPieces.PIECE_ROOK;
	}

	@Override
	public List<ChessMove> generatePossibleMoves() {
		List<ChessMove> moves = new ArrayList<>();

		doDirection(moves, coord.getAllUp());
		doDirection(moves, coord.getAllDown());
		doDirection(moves, coord.getAllLeft());
		doDirection(moves, coord.getAllRight());
		
		return moves;
	}
}
