package cz.cvut.fel.pjv.tilhovoj.chess.game;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;

public class ChessMoveAction {
	private ChessMove move;
	private boolean isCapture;
	private ChessCoord toBeCaptured;
	private ChessPiece beingCaptured;
	private ChessCoord enPassantCoord;
	private boolean isCastleShort;
	private boolean isCastleLong;
	private boolean isPromotion;
	private ChessPieces promotionPieceKind;
	
	private ChessMoveAction() {
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

	public ChessMove getMove() {
		return move;
	}
	
	public static class Builder {
		private ChessMove move;
		private boolean isCapture;
		private ChessCoord toBeCaptured;
		private ChessPiece beingCaptured;
		private ChessCoord enPassantCoord;
		private boolean isCastleShort;
		private boolean isCastleLong;
		private boolean isPromotion;
		private ChessPieces promotionPieceKind;
		
		public Builder(ChessMove move) {
			this.move = move;
		}
		
		public Builder isCapture(ChessCoord toBeCaptured, ChessBoard board) {
			this.isCapture = true;
			this.toBeCaptured = toBeCaptured;
			this.beingCaptured = board.getTileAt(toBeCaptured).getPiece();
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