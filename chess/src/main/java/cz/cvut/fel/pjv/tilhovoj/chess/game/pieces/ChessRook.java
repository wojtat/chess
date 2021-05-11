package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessRook extends ChessDirectionalPiece {

	public ChessRook(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_ROOK;
	}

	@Override
	public List<ChessMove> generateLegalMoves(ChessCoord coord) {
		List<ChessMove> moves = new ArrayList<>();

		doDirection(moves, coord.getAllUp(), coord);
		doDirection(moves, coord.getAllDown(), coord);
		doDirection(moves, coord.getAllLeft(), coord);
		doDirection(moves, coord.getAllRight(), coord);
		
		return moves;
	}
}
