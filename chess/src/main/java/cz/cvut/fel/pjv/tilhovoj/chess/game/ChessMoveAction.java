package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;
import java.util.EnumSet;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;

/**
 * Represents the chess move and allows for reverting to positions before the move was played
 */
public class ChessMoveAction implements Serializable {
	private static final long serialVersionUID = -8514178583575391325L;
	
	private ChessMove move;
	private PlayerColor onTurn;
	private EnumSet<ChessCastlingRight> oldCastlingRights;
	private ChessCoord oldEnPassantCoord;
	private int oldHalfMoveClock;
	
	private boolean isCapture;
	private ChessCoord toBeCaptured;
	private ChessPieces beingCaptured;
	private ChessCoord enPassantCoord;
	private boolean isCastleShort;
	private boolean isCastleLong;
	private boolean isPromotion;
	private ChessPieces promotionPieceKind;
	
	private ChessMoveAction() {
	}
	
	/**
	 * @return the old castling rights
	 */
	public EnumSet<ChessCastlingRight> getOldCastlingRights() {
		return oldCastlingRights;
	}
	
	/**
	 * @return the old en passant coordinate
	 */
	public ChessCoord getOldEnPassantCoord() {
		return oldEnPassantCoord;
	}

	/**
	 * @return true if the move is a promotion, false otherwise
	 */
	public boolean isPromotion() {
		return isPromotion;
	}
	
	/**
	 * @return the promotion piece kind if it is a promotion, null otherwise
	 */
	public ChessPieces getPromotionPieceKind() {
		return promotionPieceKind;
	}
	
	/**
	 * @return the new en passant coordinate
	 */
	public ChessCoord getEnPassantCoord() {
		return enPassantCoord;
	}
	
	/**
	 * @return the old half move clock
	 */
	public int getOldHalfMoveClock() {
		return oldHalfMoveClock;
	}

	/**
	 * @return true if it is a queen side castle move
	 */
	public boolean isCastleLong() {
		return isCastleLong;
	}

	/**
	 * @return true if it is a king side castle move
	 */
	public boolean isCastleShort() {
		return isCastleShort;
	}

	/**
	 * @return true if it is a castle move
	 */
	public boolean isCastle() {
		return isCastleShort || isCastleLong;
	}
	
	/**
	 * @return the coordinate of the piece that will be captured, if it is a capture, null otherwise
	 */
	public ChessCoord getToBeCaptured() {
		return toBeCaptured;
	}

	/**
	 * @return true if it is a capture move, false otherwise
	 */
	public boolean isCapture() {
		return isCapture;
	}
	
	/**
	 * @return the piece kind of the piece being captured, if it is a capture, null otherwise
	 */
	public ChessPieces getBeingCaptured() {
		return beingCaptured;
	}

	/**
	 * @return the underlying base move
	 */
	public ChessMove getMove() {
		return move;
	}

	/**
	 * @return the color of the player playing the move
	 */
	public PlayerColor getOnTurn() {
		return onTurn;
	}
	
	/**
	 * The builder of the ChessMoveAction class for convenience object creation
	 */
	public static class Builder {
		private ChessMove move;
		private PlayerColor onTurn;
		private EnumSet<ChessCastlingRight> oldCastlingRights;
		private ChessCoord oldEnPassantCoord;
		private int oldHalfMoveClock;
		
		private boolean isCapture;
		private ChessCoord toBeCaptured;
		private ChessPieces beingCaptured;
		private ChessCoord enPassantCoord;
		private boolean isCastleShort;
		private boolean isCastleLong;
		private boolean isPromotion;
		private ChessPieces promotionPieceKind;
		
		/**
		 * Creates a new Builder with the mandatory arguments
		 * @param move the underlying move
		 * @param onTurn the color of the player who plays the move
		 * @param board the board position this move happens in
		 */
		public Builder(ChessMove move, PlayerColor onTurn, ChessBoard board) {
			this.move = move;
			this.onTurn = onTurn;
			this.oldCastlingRights = board.getCastlingRights().clone();
			this.oldEnPassantCoord = board.getEnPassantCoord();
			this.oldHalfMoveClock = board.getHalfMoveClock();
		}
		
		/**
		 * Build parameters for a move capture
		 * @param toBeCaptured the coordinate of the piece that will be captured
		 * @param board the board position the move happens in
		 * @return
		 */
		public Builder isCapture(ChessCoord toBeCaptured, ChessBoard board) {
			this.isCapture = true;
			this.toBeCaptured = toBeCaptured;
			this.beingCaptured = board.getTileAt(toBeCaptured).getPiece().getKind();
			return this;
		}
		
		/**
		 * Build parameters for initial double pawn move
		 * @param enPassantCoord the destination coordinate of a capturing pawn in the event of a following en passant capture 
		 * @return
		 */
		public Builder isPawnDoubleMove(ChessCoord enPassantCoord) {
			this.enPassantCoord = enPassantCoord;
			return this;
		}
		
		/**
		 * Build parameters for a castling move
		 * @param isShort flag specifying whether this is king side or queen side castling
		 * @return
		 */
		public Builder isCastle(boolean isShort) {
			this.isCastleShort = isShort;
			this.isCastleLong = !isShort;
			return this;
		}
		
		/**
		 * Build parameters for a promotion move
		 * @param promotionPieceKind the kind of the promoted piece
		 * @return
		 */
		public Builder isPromotion(ChessPieces promotionPieceKind) {
			this.isPromotion = true;
			this.promotionPieceKind = promotionPieceKind;
			return this;
		}
		
		/**
		 * @return the ChessMoveAction built with the specified build parameters
		 */
		public ChessMoveAction build() {
			ChessMoveAction action = new ChessMoveAction();
			action.move = this.move;
			action.onTurn = this.onTurn;
			action.oldCastlingRights = this.oldCastlingRights;
			action.oldEnPassantCoord = this.oldEnPassantCoord;
			action.isCapture = this.isCapture;
			action.toBeCaptured = this.toBeCaptured;
			action.beingCaptured = this.beingCaptured;
			action.enPassantCoord = this.enPassantCoord;
			action.oldHalfMoveClock = this.oldHalfMoveClock;
			action.isCastleShort = this.isCastleShort;
			action.isCastleLong = this.isCastleLong;
			action.isPromotion = this.isPromotion;
			action.promotionPieceKind = this.promotionPieceKind;
			return action;
		}
	}
}
