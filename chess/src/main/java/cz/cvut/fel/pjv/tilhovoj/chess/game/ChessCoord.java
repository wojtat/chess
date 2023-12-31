package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Bundles together the rank and the file that make a chess board coordinate.
 */
public class ChessCoord implements Serializable {
	private static final long serialVersionUID = -3612093434784417646L;
	
	public static final ChessCoord WHITE_KING_ROOK = new ChessCoord(1, 8);
	public static final ChessCoord WHITE_QUEEN_ROOK = new ChessCoord(1, 1);
	public static final ChessCoord BLACK_KING_ROOK = new ChessCoord(8, 8);
	public static final ChessCoord BLACK_QUEEN_ROOK = new ChessCoord(8, 1);
	public static final ChessCoord WHITE_KING_ROOK_DESTINATION = new ChessCoord(1, 6);
	public static final ChessCoord WHITE_QUEEN_ROOK_DESTINATION = new ChessCoord(1, 4);
	public static final ChessCoord BLACK_KING_ROOK_DESTINATION = new ChessCoord(8, 6);
	public static final ChessCoord BLACK_QUEEN_ROOK_DESTINATION = new ChessCoord(8, 4);
	public static final ChessCoord WHITE_KING = new ChessCoord(1, 5);
	public static final ChessCoord BLACK_KING = new ChessCoord(8, 5);
	
	private Integer rank;
	private Integer file;
	
	/**
	 * Constructs a new coordinate from the given string representation.
	 * @param notation must be a 2 character string where the first character
	 * specifies the file and the second one the rank.
	 * @return the coordinate that corresponds to the string.
	 */
	public static Optional<ChessCoord> fromString(String notation) {
		if (notation.length() != 2) {
			return Optional.empty();
		}
		char fileChar = Character.toLowerCase(notation.charAt(0));
		char rankChar = notation.charAt(1);
		if (!Character.isDigit(rankChar)) {
			return Optional.empty();
		}
		
		int file = fileChar - 'a' + 1;
		int rank = rankChar - '0';
		return Optional.of(new ChessCoord(rank, file));
	}
	
	/**
	 * Construct a coordinate from the rank and the file.
	 * @param rank the rank.
	 * @param file the file.
	 */
	public ChessCoord(Integer rank, Integer file) {
		this.rank = rank;
		this.file = file;
	}
	
	/**
	 * Get the rank.
	 * @return the rank.
	 */
	public Integer getRank() {
		return rank;
	}
	
	/**
	 * Get the file.
	 * @return the file.
	 */
	public Integer getFile() {
		return file;
	}
	
	/**
	 * Get the coordinates in the increasing rank direction.
	 * @return a list of coordinates in the increasing rank direction.
	 */
	public List<ChessCoord> getAllUp() {
		List<ChessCoord> coords = new ArrayList<>();
		
		for (int y = 1; rank + y <= ChessBoard.NUM_RANKS; ++y) {
			coords.add(new ChessCoord(rank + y, file));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates in the decreasing rank direction.
	 * @return a list of coordinates in the decreasing rank direction.
	 */
	public List<ChessCoord> getAllDown() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int y = -1; rank + y >= 1; --y) {
			coords.add(new ChessCoord(rank + y, file));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates in the decreasing file direction.
	 * @return a list of coordinates in the decreasing file direction.
	 */
	public List<ChessCoord> getAllLeft() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int x = -1; file + x >= 1; --x) {
			coords.add(new ChessCoord(rank, file + x));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates in the increasing file direction.
	 * @return a list of coordinates in the increasing file direction.
	 */
	public List<ChessCoord> getAllRight() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int x = 1; file + x <= ChessBoard.NUM_FILES; ++x) {
			coords.add(new ChessCoord(rank, file + x));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates in the increasing rank and decreasing file direction.
	 * @return a list of coordinates in the increasing rank and decreasing file direction.
	 */
	public List<ChessCoord> getAllUpLeft() {
		List<ChessCoord> coords = new ArrayList<>();
		
		for (int newRank = rank + 1, newFile = file - 1; newRank <= ChessBoard.NUM_RANKS && newFile >= 1; ++newRank, --newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates in the increasing rank and increasing file direction.
	 * @return a list of coordinates in the increasing rank and increasing file direction.
	 */
	public List<ChessCoord> getAllUpRight() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int newRank = rank + 1, newFile = file + 1; newRank <= ChessBoard.NUM_RANKS && newFile <= ChessBoard.NUM_FILES; ++newRank, ++newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates in the decreasing rank and decreasing file direction.
	 * @return a list of coordinates in the decreasing rank and decreasing file direction.
	 */
	public List<ChessCoord> getAllDownLeft() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int newRank = rank - 1, newFile = file - 1; newRank >= 1 && newFile >= 1; --newRank, --newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates in the decreasing rank and increasing file direction.
	 * @return a list of coordinates in the decreasing rank and increasing file direction.
	 */
	public List<ChessCoord> getAllDownRight() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int newRank = rank - 1, newFile = file + 1; newRank >= 1 && newFile <= ChessBoard.NUM_FILES; --newRank, ++newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}

	/**
	 * Get the coordinates that directly neighbor this coordinate.
	 * @return a list of coordinates that directly neighbor this coordinate (a king's move away).
	 */
	public List<ChessCoord> getNeighbours() {
		List<ChessCoord> neighbours = new ArrayList<>();
		
		for (int y = -1; y < 2; ++y) {
			if (rank + y < 1 || rank + y > ChessBoard.NUM_RANKS) {
				// Out of bounds
				continue;
			}
			for (int x = -1; x < 2; ++x) {
				if (y == x && y == 0) {
					// This is the middle coord. Skip it
					continue;
				}
				if (file + x < 1 || file + x > ChessBoard.NUM_FILES) {
					// Out of bounds
					continue;
				}
				
				neighbours.add(new ChessCoord(rank + y, file + x));
			}
		}
		
		return neighbours;
	}

	/**
	 * Get the coordinates that are a knight's move away.
	 * @return a list of coordinates that are a knight's move away.
	 */
	public List<ChessCoord> getKnightMoves() {
		List<ChessCoord> knights = new ArrayList<>();
		
		final int SHORT = 1, LONG = 2;
		int y = rank, x = file;
		ChessCoord coord;
		
		coord = new ChessCoord(y + SHORT, x + LONG);
		if (coord.isValid())
			knights.add(coord);
		coord = new ChessCoord(y + SHORT, x - LONG);
		if (coord.isValid()) 
			knights.add(coord);
		coord = new ChessCoord(y - SHORT, x + LONG);
		if (coord.isValid())
			knights.add(coord);
		coord = new ChessCoord(y - SHORT, x - LONG);
		if (coord.isValid())
			knights.add(coord);
		coord = new ChessCoord(y + LONG, x + SHORT);
		if (coord.isValid())
			knights.add(coord);
		coord = new ChessCoord(y + LONG, x - SHORT);
		if (coord.isValid())
			knights.add(coord);
		coord = new ChessCoord(y - LONG, x + SHORT);
		if (coord.isValid())
			knights.add(coord);
		coord = new ChessCoord(y - LONG, x - SHORT);
		if (coord.isValid())
			knights.add(coord);
		
		return knights;
	}
	
	/**
	 * Get whether the coordinate represents a valid coordinate on the chess board.
	 * @return true if it represents a valid coordinate on the chess board.
	 */
	public boolean isValid() {
		return rank > 0 && rank <= ChessBoard.NUM_RANKS && file > 0 && file <= ChessBoard.NUM_FILES;
	}
	
	@Override
	public String toString() {
		Character fileAsChar = Character.valueOf((char)('a' + file - 1));
		return fileAsChar.toString() + rank.toString(); 
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(this.getClass())) {
			return false;
		}
		ChessCoord otherCoord = (ChessCoord)other; 
		return otherCoord.file.equals(file) && otherCoord.rank.equals(rank);
	}
}
