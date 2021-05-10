package cz.cvut.fel.pjv.tilhovoj.chess.game;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;

public class ChessMoveAction {
	private ChessMove move;
	private boolean isCapture;
	private Tile toBeCaptured;
	private ChessPiece beingCaptured;
	private boolean isCastleShort;
	private boolean isCastleLong;
	private boolean isPromotion;
	private ChessPiece promotionPiece;
	
	public ChessMoveAction(ChessMove move, ChessGame game) {
		this.move = move;
		
	}

	public boolean isPromotion() {
		return isPromotion;
	}
	
	public ChessPiece getPromotionPiece() {
		return promotionPiece;
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
	
	public Tile getToBeCaptured() {
		return toBeCaptured;
	}

	public boolean isCapture() {
		return isCapture;
	}

	public ChessMove getMove() {
		return move;
	}
}
