package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.util.ArrayList;
import java.util.List;

public class ChessCoord {
	private Integer rank;
	private Integer file;
	
	public ChessCoord(Integer rank, Integer file) {
		this.rank = rank;
		this.file = file;
	}
	
	public Integer getRank() {
		return rank;
	}
	
	public Integer getFile() {
		return file;
	}
	
	public List<ChessCoord> getAllUp() {
		List<ChessCoord> coords = new ArrayList<>();
		
		for (int y = 1; rank + y <= ChessBoard.NUM_RANKS; ++y) {
			coords.add(new ChessCoord(rank + y, file));
		}
		
		return coords;
	}

	public List<ChessCoord> getAllDown() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int y = -1; rank + y >= 1; --y) {
			coords.add(new ChessCoord(rank + y, file));
		}
		
		return coords;
	}
	
	public List<ChessCoord> getAllLeft() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int x = -1; file + x >= 1; --x) {
			coords.add(new ChessCoord(rank, file + x));
		}
		
		return coords;
	}

	public List<ChessCoord> getAllRight() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int x = 1; file + x <= ChessBoard.NUM_FILES; ++x) {
			coords.add(new ChessCoord(rank, file + x));
		}
		
		return coords;
	}
	
	public List<ChessCoord> getAllUpLeft() {
		List<ChessCoord> coords = new ArrayList<>();
		
		for (int newRank = rank + 1, newFile = file - 1; newRank <= ChessBoard.NUM_RANKS && newFile >= 1; ++newRank, --newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}
	
	public List<ChessCoord> getAllUpRight() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int newRank = rank + 1, newFile = file + 1; newRank <= ChessBoard.NUM_RANKS && newFile <= ChessBoard.NUM_FILES; ++newRank, ++newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}
	
	public List<ChessCoord> getAllDownLeft() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int newRank = rank - 1, newFile = file - 1; newRank >= 1 && newFile >= 1; --newRank, --newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}
	
	public List<ChessCoord> getAllDownRight() {
		List<ChessCoord> coords = new ArrayList<>();

		for (int newRank = rank - 1, newFile = file + 1; newRank >= 1 && newFile <= ChessBoard.NUM_FILES; --newRank, ++newFile) {
			coords.add(new ChessCoord(newRank, newFile));
		}
		
		return coords;
	}
	
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
