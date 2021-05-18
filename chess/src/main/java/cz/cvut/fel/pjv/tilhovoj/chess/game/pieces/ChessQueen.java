package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

/**
 * Represents a standard chess queen piece
 */
public class ChessQueen extends ChessDirectionalPiece {

	/**
	 * Constructs a new queen piece
	 * @param board the board this piece will be on
	 * @param player the owner of this piece
	 */
	public ChessQueen(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_QUEEN;
	}
	
	@Override
	public List<ChessMoveAction> generateLegalMoves(ChessCoord coord) {
		List<ChessMoveAction> moves = new ArrayList<>();

		doMovementDirection(moves, coord.getAllUp(), coord);
		doMovementDirection(moves, coord.getAllDown(), coord);
		doMovementDirection(moves, coord.getAllLeft(), coord);
		doMovementDirection(moves, coord.getAllRight(), coord);
		doMovementDirection(moves, coord.getAllUpLeft(), coord);
		doMovementDirection(moves, coord.getAllUpRight(), coord);
		doMovementDirection(moves, coord.getAllDownLeft(), coord);
		doMovementDirection(moves, coord.getAllDownRight(), coord);
		
		return moves;
	}

	@Override
	public List<ChessCoord> generateAllControlledCoords(ChessCoord coord) {
		List<ChessCoord> coords = new ArrayList<>();

		doControllDirection(coords, coord.getAllUp(), coord);
		doControllDirection(coords, coord.getAllDown(), coord);
		doControllDirection(coords, coord.getAllLeft(), coord);
		doControllDirection(coords, coord.getAllRight(), coord);
		doControllDirection(coords, coord.getAllUpLeft(), coord);
		doControllDirection(coords, coord.getAllUpRight(), coord);
		doControllDirection(coords, coord.getAllDownLeft(), coord);
		doControllDirection(coords, coord.getAllDownRight(), coord);
		
		return coords;
	}
}
