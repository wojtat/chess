package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;

public class ChessBoard {
	public static final int NUM_RANKS = 8;
	public static final int NUM_FILES = 8;
	
	private Tile[][] tiles;
	private ChessCoord enPassantCoord;
	
	private PlayerColor toMove;
	
	private boolean whiteCanCastleShort;
	private boolean whiteCanCastleLong;
	private boolean blackCanCastleShort;
	private boolean blackCanCastleLong;
	
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
		whiteCanCastleShort = whiteCanCastleLong = blackCanCastleShort = blackCanCastleLong = true;
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
				board.blackCanCastleShort = true;
				break;
			case 'K':
				board.whiteCanCastleShort = true;
				break;
			case 'q':
				board.blackCanCastleLong = true;
				break;
			case 'Q':
				board.whiteCanCastleLong = true;
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
		setEmptyBoard();
	}
	
	private boolean listContainsChessCoord(List<ChessCoord> list, ChessCoord coord) {
		for (ChessCoord element : list) {
			if (element.equals(coord)) {
				return true;
			}
		}
		return false;
	}
	
	public PlayerColor getOnTurn() {
		return toMove;
	}
	
	public void setOnTurn(PlayerColor player) {
		toMove = player;
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
	
	public void setEnPassantCoord(ChessCoord coord) {
		enPassantCoord = coord;
	}
	
	public ChessCoord getEnPassantCoord() {
		return enPassantCoord;
	}
	
	public void setWhiteCanCastleShort(boolean b) {
		this.whiteCanCastleShort = b;
	}
	
	public boolean whiteCanCastleShort() {
		return whiteCanCastleShort;
	}
	
	public void setWhiteCanCastleLong(boolean b) {
		this.whiteCanCastleLong = b;
	}
	
	public boolean whiteCanCastleLong() {
		return whiteCanCastleLong;
	}

	public void setBlackCanCastleShort(boolean b) {
		this.blackCanCastleShort = b;
	}
	
	public boolean blackCanCastleShort() {
		return blackCanCastleShort;
	}

	public void setBlackCanCastleLong(boolean b) {
		this.blackCanCastleLong = b;
	}
	
	public boolean blackCanCastleLong() {
		return blackCanCastleLong;
	}
	
	public void movePieceAndReplace(ChessMove move) {
		if (getTileAt(move.getFrom()).isEmpty()) {
			return;
		}
		ChessPiece piece = getTileAt(move.getFrom()).getPiece();
		getTileAt(move.getTo()).setPiece(piece);
		getTileAt(move.getFrom()).setPiece(null);
	}
	
	public Tile getTileAt(ChessCoord coord) {
		return tiles[coord.getRank() - 1][coord.getFile() - 1];
	}
	
	public Tile getTileAt(int rank, int file) {
		return tiles[rank - 1][file - 1];
	}
}
