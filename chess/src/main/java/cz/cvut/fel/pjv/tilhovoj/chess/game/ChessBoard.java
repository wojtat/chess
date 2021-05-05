package cz.cvut.fel.pjv.tilhovoj.chess.game;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;

public class ChessBoard {
	public static final int NUM_RANKS = 8;
	public static final int NUM_FILES = 8;
	
	private Tile[][] tiles;
	private PlayerColor toMove;
	
	public ChessBoard() {
		this.tiles = new Tile[NUM_RANKS][NUM_FILES];
		this.toMove = PlayerColor.getFirst();
		
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
						tile.setPiece(new ChessRook(this, coord, color));
						break;
					case 2:
					case 7:
						tile.setPiece(new ChessKnight(this, coord, color));
						break;
					case 3:
					case 6:
						tile.setPiece(new ChessBishop(this, coord, color));
						break;
					case 4:
						tile.setPiece(new ChessQueen(this, coord, color));
						break;
					case 5:
						tile.setPiece(new ChessKing(this, coord, color));
						break;
					}
				} else if (rank == 2 || rank == 7) {
					// Put a pawn here
					tile.setPiece(new ChessPawn(this, coord, rank == 2 ? PlayerColor.COLOR_WHITE : PlayerColor.COLOR_BLACK));
				}
				tiles[rank - 1][file - 1] = tile;
			}
		}
	}
	
	public Tile getTileAt(ChessCoord coord) {
		return tiles[coord.getRank() - 1][coord.getFile() - 1];
	}
	
	public Tile getTileAt(int rank, int file) {
		return tiles[rank - 1][file - 1];
	}
}
