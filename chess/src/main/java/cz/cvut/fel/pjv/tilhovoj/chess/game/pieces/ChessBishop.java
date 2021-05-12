package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessBishop extends ChessDirectionalPiece {

	public ChessBishop(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_BISHOP;
	}

	@Override
	public List<ChessMove> generateLegalMoves(ChessCoord coord) {
		List<ChessMove> moves = new ArrayList<>();

		doMovementDirection(moves, coord.getAllUpLeft(), coord);
		doMovementDirection(moves, coord.getAllUpRight(), coord);
		doMovementDirection(moves, coord.getAllDownLeft(), coord);
		doMovementDirection(moves, coord.getAllDownRight(), coord);
		
		return moves;
	}

	@Override
	public List<ChessCoord> generateAllControlledCoords(ChessCoord coord) {
		List<ChessCoord> coords = new ArrayList<>();
		
		doControllDirection(coords, coord.getAllUpLeft(), coord);
		doControllDirection(coords, coord.getAllUpRight(), coord);
		doControllDirection(coords, coord.getAllDownLeft(), coord);
		doControllDirection(coords, coord.getAllDownRight(), coord);
		
		return coords;
	}
}
