package cz.cvut.fel.pjv.tilhovoj.chess.game.pieces;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCastlingRight;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;

public class ChessKnight extends ChessPiece {

	public ChessKnight(ChessBoard board, PlayerColor player) {
		super(board, player);
		this.kind = ChessPieces.PIECE_KNIGHT;
	}

	@Override
	public List<ChessMoveAction> generateLegalMoves(ChessCoord coord) {
		List<ChessMoveAction> moves = new ArrayList<>();
		
		for (ChessCoord candidate : coord.getKnightMoves()) {
			if (board.getTileAt(candidate).isEmpty() || board.getTileAt(candidate).getPiece().player != this.player) {
				ChessMoveAction action = getActionFromMove(new ChessMove(coord, candidate));
				if (isActuallyLegal(action)) {
					moves.add(action);
				}
			}
		}
		
		return moves;
	}

	@Override
	public List<ChessCoord> generateAllControlledCoords(ChessCoord coord) {
		return coord.getKnightMoves();
	}

	@Override
	public ChessMoveAction getActionFromMove(ChessMove move) {
		ChessMoveAction.Builder builder = new ChessMoveAction.Builder(move, player, board);
		if (!board.getTileAt(move.getTo()).isEmpty()) {
			builder.isCapture(move.getTo(), board);
		}
		
		ChessMoveAction action = builder.build();
		return action;
	}
}
