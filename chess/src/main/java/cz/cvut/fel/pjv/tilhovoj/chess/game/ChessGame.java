package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;

public class ChessGame {

	private boolean beforeStartGame;
	private ChessClock clock;
	private ChessBoard board;
	private List<ChessMoveAction> moveList;
	
	public ChessGame(ChessClock clock, ChessBoard board) {
		this.clock = clock;
		this.board = board;
		this.moveList = new ArrayList<>();
		this.beforeStartGame = true;
	}
	
	public void startGame() {
		beforeStartGame = false;
	}
	
	public boolean beforeStartGame() {
		return beforeStartGame;
	}
	
	public ChessBoard getBoard() {
		return board;
	}
	
	public ChessClock getClock() {
		return clock;
	}
	
	public void playMove(ChessMove move) {
		if (beforeStartGame) {
			return;
		}
		ChessMoveAction action = board.getTileAt(move.getFrom()).getPiece().getActionFromMove(move);
		// If king, then can no longer castle
		if (board.getTileAt(move.getFrom()).getPiece().getKind() == ChessPieces.PIECE_KING) {
			switch (board.getTileAt(move.getFrom()).getPiece().getColor()) {
			case COLOR_WHITE:
				board.setWhiteCanCastleShort(false);
				board.setWhiteCanCastleLong(false);
				break;
			case COLOR_BLACK:
				board.setBlackCanCastleShort(false);
				board.setBlackCanCastleLong(false);
				break;
			}
		}
		if (action.isCapture()) {
			// If rook, then can no longer castle this side
			if (board.getTileAt(action.getToBeCaptured()).getPiece().getKind() == ChessPieces.PIECE_ROOK) {
				switch (board.getTileAt(action.getToBeCaptured()).getPiece().getColor()) {
				case COLOR_WHITE:
					if (action.getToBeCaptured().equals(ChessCoord.WHITE_KING_ROOK)) {
						board.setWhiteCanCastleShort(false);
					} else if (action.getToBeCaptured().equals(ChessCoord.WHITE_QUEEN_ROOK)) {
						board.setWhiteCanCastleLong(false);
					}
					break;
				case COLOR_BLACK:
					if (action.getToBeCaptured().equals(ChessCoord.BLACK_KING_ROOK)) {
						board.setBlackCanCastleShort(false);
					} else if (action.getToBeCaptured().equals(ChessCoord.BLACK_QUEEN_ROOK)) {
						board.setBlackCanCastleLong(false);
					}
					break;
				}
			}
			// Replace the captured piece with null
			board.getTileAt(action.getToBeCaptured()).setPiece(null);
		}
		board.movePieceAndReplace(move);
		if (action.isPromotion()) {
			// If promotion, then replace the pawn with the promoted piece
			ChessPiece promotedPiece;
			switch (action.getPromotionPieceKind()) {
			case PIECE_BISHOP:
				promotedPiece = new ChessBishop(board, board.getOnTurn());
				break;
			case PIECE_KNIGHT:
				promotedPiece = new ChessKnight(board, board.getOnTurn());
				break;
			case PIECE_ROOK:
				promotedPiece = new ChessRook(board, board.getOnTurn());
				break;
			case PIECE_QUEEN:
				promotedPiece = new ChessQueen(board, board.getOnTurn());
				break;
			default:
				promotedPiece = null;
				break;
			}
			board.getTileAt(move.getTo()).setPiece(promotedPiece);
		}
		if (action.isCastle()) {
			// Move the rook as well
			ChessMove rookMove;
			if (board.getOnTurn() == PlayerColor.COLOR_WHITE) {
				if (action.isCastleShort()) {
					rookMove = new ChessMove(ChessCoord.WHITE_KING_ROOK, ChessCoord.WHITE_KING_ROOK_DESTINATION);
				} else {
					rookMove = new ChessMove(ChessCoord.WHITE_QUEEN_ROOK, ChessCoord.WHITE_QUEEN_ROOK_DESTINATION);
				}
			} else {
				if (action.isCastleShort()) {
					rookMove = new ChessMove(ChessCoord.BLACK_KING_ROOK, ChessCoord.BLACK_KING_ROOK_DESTINATION);
				} else {
					rookMove = new ChessMove(ChessCoord.BLACK_QUEEN_ROOK, ChessCoord.BLACK_QUEEN_ROOK_DESTINATION);
				}
			}
			board.movePieceAndReplace(rookMove);
		}
		
		board.setEnPassantCoord(action.getEnPassantCoord());
		System.out.println("En passant coord is " + action.getEnPassantCoord());
		
		moveList.add(action);
		board.setOnTurn(PlayerColor.getNext(board.getOnTurn()));
	}
}
