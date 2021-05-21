package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;

/**
 * The main class for holding the tile grid, chess pieces, and the general state
 * that is specific to a position on the board.
 */
public class ChessBoard implements Serializable {
	private static final long serialVersionUID = -8455258181430213061L;
	
	private static Logger LOG = Logger.getLogger(ChessBoard.class.getName());
	
	/**
	 * Number of ranks of a chess board (i.e. the height).
	 */
	public static final int NUM_RANKS = 8;

	/**
	 * Number of files of a chess board (i.e. the width).
	 */
	public static final int NUM_FILES = 8;
	
	private Tile[][] tiles;
	private Optional<ChessCoord> enPassantCoord;
	
	private int halfMoveClock;
	
	private PlayerColor toMove;
	private EnumSet<ChessCastlingRight> castlingRights;
	
	private String startingFEN;
	
	private void setEmptyBoard() {
		for (int rank = 1; rank <= NUM_RANKS; ++rank) {
			for (int file = 1; file <= NUM_FILES; ++file) {
				ChessCoord coord = new ChessCoord(rank, file);
				Tile tile = new Tile(coord);
				tiles[rank - 1][file - 1] = tile;
			}
		}
	}
	
	/**
	 * Constructs a new ChessBoard given a FEN position.
	 * @param fenString the FEN position represented as a string.
	 * @return the resulting chess board.
	 */
	public static ChessBoard fromFEN(String fenString)  {
		ChessBoard board = new ChessBoard();
		board.startingFEN = fenString;
		
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
					try {
						board.getTileAt(rank, file++).setPiece(ChessPiece.fromFENCharacter(board, c).get());
					} catch (NoSuchElementException e) {
						LOG.severe("Invalid chess piece '" + c + "'");
					}
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
		
		// NOTE: If en passant coord is '-', then ChessCoord.fromString() returns no value
		board.enPassantCoord = ChessCoord.fromString(fields[3]);
		
		return board;
	}

	private ChessBoard() {
		this.tiles = new Tile[NUM_RANKS][NUM_FILES];
		this.enPassantCoord = Optional.empty();
		this.toMove = PlayerColor.getFirst();
		this.castlingRights = EnumSet.noneOf(ChessCastlingRight.class);
		setEmptyBoard();
	}

	/**
	 * The recursive 'performance test' method computes how many choices of moves there are
	 * for a specified depth.
	 * @param depth the maximum depth of the calculation.
	 * @return the number of choices of moves after a maximum of depth moves.
	 */
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
	
	/**
	 * The number of legal moves.
	 * @return the number of legal moves that can be played from this position.
	 */
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
	
	/**
	 * Plays the specified move and updates the board state accordingly.
	 * @param action the move action that is to be executed on the board.
	 */
	public void playMove(ChessMoveAction action) {
		ChessMove move = action.getMove();
		
		if (action.isCapture() || getTileAt(move.getFrom()).getPiece().getKind() == ChessPieces.PIECE_PAWN) {
			// Reset the half move clock
			halfMoveClock = 0;
		} else {
			// Icrement the half move clock
			++halfMoveClock;
		}
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
			ChessPiece promotedPiece = ChessPiece.fromKind(this, action.getOnTurn(), action.getPromotionPieceKind()).get();
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
	
	/**
	 * Recovers the board to the state before the specified move action was executed,
	 * assumes the action is valid.
	 * @param action the move action that will be undone.
	 */
	public void unplayMove(ChessMoveAction action) {
		ChessMove move = action.getMove();
		
		castlingRights = action.getOldCastlingRights();
		enPassantCoord = action.getOldEnPassantCoord();
		halfMoveClock = action.getOldHalfMoveClock();
		
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
			ChessPiece capturedPiece = ChessPiece.fromKind(this, PlayerColor.getPrevious(action.getOnTurn()), action.getBeingCaptured()).get();
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
	
	/**
	 * Checks whether the coordinate is under attack by a player.
	 * @param coord the coordinate on the board that will be examined.
	 * @param player a player.
	 * @return true if any single player's piece controls (i.e. could pseudo legally move there) the coordinate,
	 * false otherwise.
	 */
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
	
	/**
	 * Checks whether the king of a player is under attack (i.e. in check).
	 * @param color the color of the king.
	 * @return true if the king of the given color is under attack, false otherwise.
	 */
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
	
	/**
	 * Get the starting FEN.
	 * @return the starting FEN position string of the chess board.
	 */
	public String getStartingFEN() {
		return startingFEN;
	}
	
	/**
	 * Get the half move clock.
	 * @return the half move clock (i.e. the number of moves since the last capture or pawn move).
	 */
	public int getHalfMoveClock() {
		return halfMoveClock;
	}
	
	/**
	 * Set the half move clock.
	 * @param halfMoveClock the new value for the half move clock counter.
	 */
	public void setHalfMoveClock(int halfMoveClock) {
		this.halfMoveClock = halfMoveClock;
	}
	
	/**
	 * Get the player on turn.
	 * @return the color of the player whose turn it currently is.
	 */
	public PlayerColor getOnTurn() {
		return toMove;
	}
	
	/**
	 * Set the en passant coordinate.
	 * @param coord the new coordinate of the en passant square (the square the capturing pawn moves to
	 * when it captures en passant.
	 */
	public void setEnPassantCoord(Optional<ChessCoord> coord) {
		enPassantCoord = coord;
	}
	
	/**
	 * Get the en passant coordinate.
	 * @return the new coordinate of the en passant square (the square the capturing pawn moves to
	 * when it captures en passant.
	 */
	public Optional<ChessCoord> getEnPassantCoord() {
		return enPassantCoord;
	}
	
	/**
	 * Get the castling rights.
	 * @return the castling rights in the position on the board.
	 */
	public EnumSet<ChessCastlingRight> getCastlingRights() {
		return castlingRights;
	}
	
	/**
	 * Get the white short castling right.
	 * @return true if white can castle king side, false otherwise.
	 */
	public boolean whiteCanCastleShort() {
		return castlingRights.contains(ChessCastlingRight.WHITE_KINGSIDE);
	}

	/**
	 * Get the white long castling right.
	 * @return true if white can castle queen side, false otherwise.
	 */
	public boolean whiteCanCastleLong() {
		return castlingRights.contains(ChessCastlingRight.WHITE_QUEENSIDE);
	}

	/**
	 * Get the black short castling right.
	 * @return true if black can castle king side, false otherwise.
	 */
	public boolean blackCanCastleShort() {
		return castlingRights.contains(ChessCastlingRight.BLACK_KINGSIDE);
	}
	
	/**
	 * Get the black long castling right.
	 * @return true if black can castle queen side, false otherwise.
	 */
	public boolean blackCanCastleLong() {
		return castlingRights.contains(ChessCastlingRight.BLACK_QUEENSIDE);
	}
	
	/**
	 * Returns the tile that the coordinate specifies.
	 * @param coord the coordinate of the tile.
	 * @return the tile that the coordinate specifies.
	 */
	public Tile getTileAt(ChessCoord coord) {
		return tiles[coord.getRank() - 1][coord.getFile() - 1];
	}

	/**
	 * Returns the tile that the coordinate specifies.
	 * @param rank rank of the coordinate of the tile.
	 * @param file file of the coordinate of the tile.
	 * @return the tile that the coordinate specifies.
	 */
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
