package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;
import java.util.EnumSet;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;

public class ChessMoveAction implements Serializable {
	private static final long serialVersionUID = -8514178583575391325L;
	
	private ChessMove move;
	private PlayerColor onTurn;
	private EnumSet<ChessCastlingRight> oldCastlingRights;
	private ChessCoord oldEnPassantCoord;
	
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
	
	public EnumSet<ChessCastlingRight> getOldCastlingRights() {
		return oldCastlingRights;
	}
	
	public ChessCoord getOldEnPassantCoord() {
		return oldEnPassantCoord;
	}

	public boolean isPromotion() {
		return isPromotion;
	}
	
	public ChessPieces getPromotionPieceKind() {
		return promotionPieceKind;
	}
	
	public ChessCoord getEnPassantCoord() {
		return enPassantCoord;
	}

	public boolean isCastleLong() {
		return isCastleLong;
	}

	public boolean isCastleShort() {
		return isCastleShort;
	}

	public boolean isCastle() {
		return isCastleShort || isCastleLong;
	}
	
	public ChessCoord getToBeCaptured() {
		return toBeCaptured;
	}

	public boolean isCapture() {
		return isCapture;
	}
	
	public ChessPieces getBeingCaptured() {
		return beingCaptured;
	}

	public ChessMove getMove() {
		return move;
	}

	public PlayerColor getOnTurn() {
		return onTurn;
	}
	
	public static class Builder {
		private ChessMove move;
		private PlayerColor onTurn;
		private EnumSet<ChessCastlingRight> oldCastlingRights;
		private ChessCoord oldEnPassantCoord;
		
		private boolean isCapture;
		private ChessCoord toBeCaptured;
		private ChessPieces beingCaptured;
		private ChessCoord enPassantCoord;
		private boolean isCastleShort;
		private boolean isCastleLong;
		private boolean isPromotion;
		private ChessPieces promotionPieceKind;
		
		public Builder(ChessMove move, PlayerColor onTurn, ChessBoard board) {
			this.move = move;
			this.onTurn = onTurn;
			this.oldCastlingRights = board.getCastlingRights().clone();
			this.oldEnPassantCoord = board.getEnPassantCoord();
		}
		
		public Builder isCapture(ChessCoord toBeCaptured, ChessBoard board) {
			this.isCapture = true;
			this.toBeCaptured = toBeCaptured;
			this.beingCaptured = board.getTileAt(toBeCaptured).getPiece().getKind();
			return this;
		}
		
		public Builder isPawnDoubleMove(ChessCoord enPassantCoord) {
			this.enPassantCoord = enPassantCoord;
			return this;
		}
		
		public Builder isCastle(boolean isShort) {
			this.isCastleShort = isShort;
			this.isCastleLong = !isShort;
			return this;
		}
		
		public Builder isPromotion(ChessPieces promotionPieceKind) {
			this.isPromotion = true;
			this.promotionPieceKind = promotionPieceKind;
			return this;
		}
		
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
			action.isCastleShort = this.isCastleShort;
			action.isCastleLong = this.isCastleLong;
			action.isPromotion = this.isPromotion;
			action.promotionPieceKind = this.promotionPieceKind;
			return action;
		}
	}
}
