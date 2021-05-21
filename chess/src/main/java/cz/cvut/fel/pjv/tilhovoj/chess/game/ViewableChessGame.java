package cz.cvut.fel.pjv.tilhovoj.chess.game;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;

/**
 * A convenience wrapper class around a ChessGame.
 */
public class ViewableChessGame extends ChessGame {

	/**
	 * Constructs a new chess game with no specified time controls.
	 * @param board the starting board of this chess game.
	 */
	public ViewableChessGame(ChessBoard board) {
		super(0.0, 0.0, board);
	}
	
	private void insertMove(ChessMoveAction action) {
		board.playMove(action);
		++currentMove;
		moveList.add(action);
	}
	
	private ChessMoveAction getCorrectMove(ChessPiece piece, ChessCoord coord, ChessCoord destination, ChessPieces promoteTo) {
		if (promoteTo != null) {
			for (ChessMoveAction action : piece.generateLegalMoves(coord)) {
				if (action.getMove().getTo().equals(destination) && action.isPromotion() && action.getPromotionPieceKind() == promoteTo) {
					return action;
				}
			}	
		} else {
			for (ChessMoveAction action : piece.generateLegalMoves(coord)) {
				if (action.getMove().getTo().equals(destination)) {
					return action;
				}
			}
		}
		return null;
	}
	
	private ChessMoveAction findMovingPieceAction(ChessPieces pieceKind, ChessCoord destination, int rankSpec, int fileSpec, ChessPieces promoteTo) {
		if (rankSpec != 0 && fileSpec != 0) {
			ChessPiece piece = board.getTileAt(rankSpec, fileSpec).getPiece();
			return getCorrectMove(piece, new ChessCoord(rankSpec, fileSpec), destination, promoteTo);
		} else if (fileSpec != 0) {
			for (int rank = 1; rank <= ChessBoard.NUM_RANKS; ++rank) {
				if (!board.getTileAt(rank, fileSpec).isEmpty()) {
					ChessPiece piece = board.getTileAt(rank, fileSpec).getPiece();
					if (piece.getKind() == pieceKind && piece.getColor() == board.getOnTurn()) {
						ChessMoveAction action = getCorrectMove(piece, new ChessCoord(rank, fileSpec), destination, promoteTo);
						if (action != null) {
							return action;
						}
					}
				}
			}
		} else if (rankSpec != 0) {
			for (int file = 1; file <= ChessBoard.NUM_FILES; ++file) {
				if (!board.getTileAt(rankSpec, file).isEmpty()) {
					ChessPiece piece = board.getTileAt(rankSpec, file).getPiece();
					if (piece.getKind() == pieceKind && piece.getColor() == board.getOnTurn()) {
						ChessMoveAction action = getCorrectMove(piece, new ChessCoord(rankSpec, file), destination, promoteTo);
						if (action != null) {
							return action;
						}
					}
				}
			}
		} else {
			for (int rank = 1; rank <= ChessBoard.NUM_RANKS; ++rank) {
				for (int file = 1; file <= ChessBoard.NUM_FILES; ++file) {
					if (!board.getTileAt(rank, file).isEmpty()) {
						ChessPiece piece = board.getTileAt(rank, file).getPiece();
						if (piece.getKind() == pieceKind && piece.getColor() == board.getOnTurn()) {
							ChessMoveAction action = getCorrectMove(piece, new ChessCoord(rank, file), destination, promoteTo);
							if (action != null) {
								return action;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private ChessMoveAction findCastleAction(boolean isShort) {
		// NOTE: This could be optimised because the king must either be on e1 or e8
		for (int rank = 1; rank <= ChessBoard.NUM_RANKS; ++rank) {
			for (int file = 1; file <= ChessBoard.NUM_FILES; ++file) {
				if (!board.getTileAt(rank, file).isEmpty()) {
					ChessPiece piece = board.getTileAt(rank, file).getPiece();
					if (piece.getKind() == ChessPieces.PIECE_KING && piece.getColor() == board.getOnTurn()) {
						for (ChessMoveAction action : piece.generateLegalMoves(new ChessCoord(rank, file))) {
							if ((action.isCastleShort() && isShort) || (action.isCastleLong() && !isShort)) {
								return action;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Decodes the given string representation of the move and plays it on its chess board.
	 * @param move the standard algebraic notation of the move to interpret and play.
	 */
	public void decodeAndPlayMove(String move) {
		// Store the move as string
		sanMoveList.add(move);
		// Remove all uninteresting characters
		move = move.replaceAll("[\\-\\+x#]", "");
		ChessPieces pieceKind;
		ChessCoord destination;
		ChessPieces promoteTo = null;
		int rankSpec = 0;
		int fileSpec = 0;
		
		if (move.charAt(0) == 'O') {
			// Castling
			ChessMoveAction action = findCastleAction(move.length() == 2);
			insertMove(action);
			return;
		} else if (Character.isUpperCase(move.charAt(0))) {
			// It's a piece move
			pieceKind = ChessPieces.fromSANCharacter(move.charAt(0));
			move = move.substring(1);
		} else {
			// It's a pawn move
			pieceKind = ChessPieces.PIECE_PAWN;
		}
		int equalsIndex = move.indexOf('=');
		if (equalsIndex != -1) {
			// It's a promotion
			promoteTo = ChessPieces.fromSANCharacter(move.charAt(equalsIndex+1));
			move = move.substring(0, equalsIndex);
		}

		if (move.length() == 3) {
			// Either file spec or rank spec
			if (move.charAt(0) >= '0' && move.charAt(0) <= '9') {
				// Rank spec
				rankSpec = move.charAt(0) - '0';
			} else {
				// File spec
				fileSpec = move.charAt(0) - 'a' + 1;
			}
			move = move.substring(1);
		} else if (move.length() == 4) {
			// Both file spec and rank spec
			fileSpec = move.charAt(0) - 'a' + 1;
			rankSpec = move.charAt(1) - '0';
			move = move.substring(2);
		}
		// Now we only have 2 characters that specify the destination
		destination = ChessCoord.fromString(move);
		LOG.fine("MOVING " + pieceKind + " TO " + destination);
		ChessMoveAction action = findMovingPieceAction(pieceKind, destination, rankSpec, fileSpec, promoteTo);
		insertMove(action);
	}
}
