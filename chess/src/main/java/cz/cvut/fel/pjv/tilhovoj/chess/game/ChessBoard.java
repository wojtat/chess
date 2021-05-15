package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;

public class ChessBoard implements Serializable {
	private static final long serialVersionUID = -8455258181430213061L;
	
	public static final int NUM_RANKS = 8;
	public static final int NUM_FILES = 8;
	
	private Tile[][] tiles;
	private ChessCoord enPassantCoord;
	
	private PlayerColor toMove;
	private EnumSet<ChessCastlingRight> castlingRights;
	
	private void setEmptyBoard() {
		for (int rank = 1; rank <= NUM_RANKS; ++rank) {
			for (int file = 1; file <= NUM_FILES; ++file) {
				ChessCoord coord = new ChessCoord(rank, file);
				Tile tile = new Tile(coord);
				tiles[rank - 1][file - 1] = tile;
			}
		}
	}
	
	private void setInitialBoard() {
		castlingRights = EnumSet.allOf(ChessCastlingRight.class);
		for (int rank = 1; rank <= NUM_RANKS; ++rank) {
			for (int file = 1; file <= NUM_FILES; ++file) {
				ChessCoord coord = new ChessCoord(rank, file);
				Tile tile = new Tile(coord);
				
				if (rank == 1 || rank == 8) {
					// Put a piece here
					PlayerColor color = rank == 1 ? PlayerColor.COLOR_WHITE : PlayerColor.COLOR_BLACK;
					switch (file) {
					case 1:
					case 8:
						tile.setPiece(new ChessRook(this, color));
						break;
					case 2:
					case 7:
						tile.setPiece(new ChessKnight(this, color));
						break;
					case 3:
					case 6:
						tile.setPiece(new ChessBishop(this, color));
						break;
					case 4:
						tile.setPiece(new ChessQueen(this, color));
						break;
					case 5:
						tile.setPiece(new ChessKing(this, color));
						break;
					}
				} else if (rank == 2 || rank == 7) {
					// Put a pawn here
					tile.setPiece(new ChessPawn(this, rank == 2 ? PlayerColor.COLOR_WHITE : PlayerColor.COLOR_BLACK));
				}
				tiles[rank - 1][file - 1] = tile;
			}
		}
	}
	
	public static ChessBoard fromFEN(String fenString)  {
		ChessBoard board = new ChessBoard();
		
		String[] fields = fenString.split(" ");
		if (fields.length != 6) {
			return board;
		}
		
		String[] piecePlacement = fields[0].split("/");
		if (piecePlacement.length != NUM_RANKS) {
			return board;
		}
		for (int rank = NUM_RANKS; rank >= 1; --rank) {
			String rankString = piecePlacement[NUM_RANKS - rank];
			int readIndex = 0;
			int file = 1;
			while (file <= NUM_FILES) {
				char c = rankString.charAt(readIndex++);
				if (Character.isDigit(c)) {
					file += Character.getNumericValue(c);
				} else {
					board.getTileAt(rank, file++).setPiece(ChessPiece.fromFENCharacter(board, c));
				}
			}
		}
		
		board.toMove = fields[1].charAt(0) == 'w' ? PlayerColor.COLOR_WHITE : PlayerColor.COLOR_BLACK;
		String castlingRights = fields[2];
		for (char c : castlingRights.toCharArray()) {
			switch (c) {
			case 'k':
				board.castlingRights.add(ChessCastlingRight.BLACK_KINGSIDE);
				break;
			case 'K':
				board.castlingRights.add(ChessCastlingRight.WHITE_KINGSIDE);
				break;
			case 'q':
				board.castlingRights.add(ChessCastlingRight.BLACK_QUEENSIDE);
				break;
			case 'Q':
				board.castlingRights.add(ChessCastlingRight.WHITE_QUEENSIDE);
				break;
			}
		}
		
		// NOTE: If en passant coord is '-', then ChessCoord.fromString() returns null
		board.enPassantCoord = ChessCoord.fromString(fields[3]);
		
		return board;
	}

	public ChessBoard() {
		this.tiles = new Tile[NUM_RANKS][NUM_FILES];
		this.enPassantCoord = null;
		this.toMove = PlayerColor.getFirst();
		this.castlingRights = EnumSet.noneOf(ChessCastlingRight.class);
		setEmptyBoard();
	}
	
	public int perft(int depth) {
		if (depth == 0) {
			return 1;
		}
		int total = 0;
		for (int rank = 1; rank <= NUM_RANKS; ++rank) {
			for (int file = 1; file <= NUM_FILES; ++file) {
				if (!getTileAt(rank, file).isEmpty() && getTileAt(rank, file).getPiece().getColor() == toMove) {
					List<ChessMoveAction> allMoves = getTileAt(rank, file).getPiece().generateLegalMoves(new ChessCoord(rank, file));
					for (ChessMoveAction action : allMoves) {
						playMove(action);
						total += perft(depth - 1);
						unplayMove(action);
					}
				}
			}
		}
		return total;
	}
	
	public int getNumLegalMoves() {
		// For every piece, get its number of possible moves
		int total = 0;
		for (int rank = 1; rank <= NUM_RANKS; ++rank) {
			for (int file = 1; file <= NUM_FILES; ++file) {
				if (!getTileAt(rank, file).isEmpty() && getTileAt(rank, file).getPiece().getColor() == toMove) {
					total += getTileAt(rank, file).getPiece().generateLegalMoves(new ChessCoord(rank, file)).size();
				}
			}
		}
		return total;
	}
	
	public void playMove(ChessMoveAction action) {
		ChessMove move = action.getMove();
		// If king, then can no longer castle
		if (getTileAt(move.getFrom()).getPiece().getKind() == ChessPieces.PIECE_KING) {
			switch (getTileAt(move.getFrom()).getPiece().getColor()) {
			case COLOR_WHITE:
				castlingRights.remove(ChessCastlingRight.WHITE_KINGSIDE);
				castlingRights.remove(ChessCastlingRight.WHITE_QUEENSIDE);
				break;
			case COLOR_BLACK:
				castlingRights.remove(ChessCastlingRight.BLACK_KINGSIDE);
				castlingRights.remove(ChessCastlingRight.BLACK_QUEENSIDE);
				break;
			}
		}
		if (action.isCapture()) {
			// If rook, then can no longer castle this side
			if (getTileAt(action.getToBeCaptured()).getPiece().getKind() == ChessPieces.PIECE_ROOK) {
				switch (getTileAt(action.getToBeCaptured()).getPiece().getColor()) {
				case COLOR_WHITE:
					if (action.getToBeCaptured().equals(ChessCoord.WHITE_KING_ROOK)) {
						castlingRights.remove(ChessCastlingRight.WHITE_KINGSIDE);
					} else if (action.getToBeCaptured().equals(ChessCoord.WHITE_QUEEN_ROOK)) {
						castlingRights.remove(ChessCastlingRight.WHITE_QUEENSIDE);
					}
					break;
				case COLOR_BLACK:
					if (action.getToBeCaptured().equals(ChessCoord.BLACK_KING_ROOK)) {
						castlingRights.remove(ChessCastlingRight.BLACK_KINGSIDE);
					} else if (action.getToBeCaptured().equals(ChessCoord.BLACK_QUEEN_ROOK)) {
						castlingRights.remove(ChessCastlingRight.BLACK_QUEENSIDE);
					}
					break;
				}
			}
			// Replace the captured piece with null
			getTileAt(action.getToBeCaptured()).setPiece(null);
		}
		movePieceAndReplace(move.getFrom(), move.getTo());
		if (action.isPromotion()) {
			// If promotion, then replace the pawn with the promoted piece
			ChessPiece promotedPiece = ChessPiece.fromKind(this, action.getOnTurn(), action.getPromotionPieceKind());
			getTileAt(move.getTo()).setPiece(promotedPiece);
		}
		if (action.isCastle()) {
			// Move the rook as well
			ChessMove rookMove;
			if (action.getOnTurn() == PlayerColor.COLOR_WHITE) {
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
			movePieceAndReplace(rookMove.getFrom(), rookMove.getTo());
		}
		enPassantCoord = action.getEnPassantCoord();
		toMove = PlayerColor.getNext(action.getOnTurn());
	}
	
	public void unplayMove(ChessMoveAction action) {
		ChessMove move = action.getMove();
		
		castlingRights = action.getOldCastlingRights();
		enPassantCoord = action.getOldEnPassantCoord();
		
		if (action.isCastle()) {
			// Move the rook back as well
			ChessMove rookMove;
			if (action.getOnTurn() == PlayerColor.COLOR_WHITE) {
				if (action.isCastleShort()) {
					rookMove = new ChessMove(ChessCoord.WHITE_KING_ROOK_DESTINATION, ChessCoord.WHITE_KING_ROOK);
				} else {
					rookMove = new ChessMove(ChessCoord.WHITE_QUEEN_ROOK_DESTINATION, ChessCoord.WHITE_QUEEN_ROOK);
				}
			} else {
				if (action.isCastleShort()) {
					rookMove = new ChessMove(ChessCoord.BLACK_KING_ROOK_DESTINATION, ChessCoord.BLACK_KING_ROOK);
				} else {
					rookMove = new ChessMove(ChessCoord.BLACK_QUEEN_ROOK_DESTINATION, ChessCoord.BLACK_QUEEN_ROOK);
				}
			}
			movePieceAndReplace(rookMove.getFrom(), rookMove.getTo());
		}
		if (action.isPromotion()) {
			// Replace the promoted piece with a pawn first
			getTileAt(move.getTo()).setPiece(new ChessPawn(this, action.getOnTurn()));
		}
		movePieceAndReplace(move.getTo(), move.getFrom());
		if (action.isCapture()) {
			// If a capture happened, put the captured piece on the board
			ChessPiece capturedPiece = ChessPiece.fromKind(this, PlayerColor.getPrevious(action.getOnTurn()), action.getBeingCaptured());
			getTileAt(action.getToBeCaptured()).setPiece(capturedPiece);
		}
		toMove = action.getOnTurn();
	}
	
	private boolean listContainsChessCoord(List<ChessCoord> list, ChessCoord coord) {
		for (ChessCoord element : list) {
			if (element.equals(coord)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isUnderAttackBy(ChessCoord coord, PlayerColor player) {
		// For every piece, see if it can move to this square pseudo-legally
		for (int rank = 1; rank <= NUM_RANKS; ++rank) {
			for (int file = 1; file <= NUM_FILES; ++file) {
				if (!getTileAt(rank, file).isEmpty() && getTileAt(rank, file).getPiece().getColor() == player) {
					List<ChessCoord> controlledSquares = getTileAt(rank, file).getPiece().generateAllControlledCoords(new ChessCoord(rank, file));
					if (listContainsChessCoord(controlledSquares, coord)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isKingUnderAttack(PlayerColor color) {
		for (int rank = 1; rank <= NUM_RANKS; ++rank) {
			for (int file = 1; file <= NUM_FILES; ++file) {
				if (!getTileAt(rank, file).isEmpty() && getTileAt(rank, file).getPiece().getKind() == ChessPieces.PIECE_KING && getTileAt(rank, file).getPiece().getColor() == color) {
					return isUnderAttackBy(new ChessCoord(rank, file), PlayerColor.getNext(color));
				}
			}
		}
		return false;
	}
	
	public PlayerColor getOnTurn() {
		return toMove;
	}
	
	public void setEnPassantCoord(ChessCoord coord) {
		enPassantCoord = coord;
	}
	
	public ChessCoord getEnPassantCoord() {
		return enPassantCoord;
	}
	
	public EnumSet<ChessCastlingRight> getCastlingRights() {
		return castlingRights;
	}
	
	public boolean whiteCanCastleShort() {
		return castlingRights.contains(ChessCastlingRight.WHITE_KINGSIDE);
	}
	
	public boolean whiteCanCastleLong() {
		return castlingRights.contains(ChessCastlingRight.WHITE_QUEENSIDE);
	}
	
	public boolean blackCanCastleShort() {
		return castlingRights.contains(ChessCastlingRight.BLACK_KINGSIDE);
	}
	
	public boolean blackCanCastleLong() {
		return castlingRights.contains(ChessCastlingRight.BLACK_QUEENSIDE);
	}
	
	public Tile getTileAt(ChessCoord coord) {
		return tiles[coord.getRank() - 1][coord.getFile() - 1];
	}
	
	public Tile getTileAt(int rank, int file) {
		return tiles[rank - 1][file - 1];
	}
	
	private void movePieceAndReplace(ChessCoord from, ChessCoord to) {
		if (getTileAt(from).isEmpty()) {
			return;
		}
		ChessPiece piece = getTileAt(from).getPiece();
		getTileAt(to).setPiece(piece);
		getTileAt(from).setPiece(null);
	}

}
