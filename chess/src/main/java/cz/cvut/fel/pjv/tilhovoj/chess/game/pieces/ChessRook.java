package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

/**
 * Represents a standard chess rook piece
 */
public class ChessRook extends ChessDirectionalPiece {

	/**
	 * Constructs a new rook piece
	 * @param board the board this piece will be on
	 * @param player the owner of this piece
	 */
	public ChessRook(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_ROOK;
	}

	@Override
	public List<ChessMoveAction> generateLegalMoves(ChessCoord coord) {
		List<ChessMoveAction> moves = new ArrayList<>();

		doMovementDirection(moves, coord.getAllUp(), coord);
		doMovementDirection(moves, coord.getAllDown(), coord);
		doMovementDirection(moves, coord.getAllLeft(), coord);
		doMovementDirection(moves, coord.getAllRight(), coord);
		
		return moves;
	}

	@Override
	public List<ChessCoord> generateAllControlledCoords(ChessCoord coord) {
		List<ChessCoord> coords = new ArrayList<>();

		doControllDirection(coords, coord.getAllUp(), coord);
		doControllDirection(coords, coord.getAllDown(), coord);
		doControllDirection(coords, coord.getAllLeft(), coord);
		doControllDirection(coords, coord.getAllRight(), coord);
		
		return coords;
	}
}
