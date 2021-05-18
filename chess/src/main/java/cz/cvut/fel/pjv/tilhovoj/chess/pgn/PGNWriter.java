package cz.cvut.fel.pjv.tilhovoj.chess.pgn;

import java.util.List;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

/**
 * Represents a PGN text stream writer
 */
public class PGNWriter {
	private OutputStreamWriter out;
	
	/**
	 * Construct a new PGN stream writer
	 * @param out the output text stream
	 */
	public PGNWriter(OutputStream out) {
		this.out = new OutputStreamWriter(out);
	}
	
	private String getResultString(ChessGame.State result, PlayerColor winner) {
		String resultString = "*";
		if (result == ChessGame.State.DRAW) {
			resultString = "1/2-1/2";
		} else if (result == ChessGame.State.WIN) {
			if (winner == PlayerColor.COLOR_WHITE) {
				resultString = "1-0";
			} else {
				resultString = "0-1";
			}
		}
		return resultString;
	}
	
	private void writeTagPair(String name, String value) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append(name);
		builder.append(" \"" + value + '"');
		builder.append("]\n");
		out.write(builder.toString());
	}
	
	private void writeTagPairs(String resultString) throws IOException {
		writeTagPair("Event", "Casual Game");
		writeTagPair("Site", "?");
		writeTagPair("Date", "????.??.??");
		writeTagPair("Round", "-");
		writeTagPair("White", "?");
		writeTagPair("Black", "?");
		writeTagPair("Result", resultString);
		out.write('\n');
	}
	
	/**
	 * Writes the given chess game in its PGN format representation
	 * @param game the game to write to the stream
	 * @throws IOException when an error occurs while writing to the output stream
	 */
	public void writeChessGame(ChessGame game) throws IOException {
		String resultString = getResultString(game.getState(), game.getWinner());
		writeTagPairs(resultString);
		
		List<String> moveList = game.getSANMoveList();
		final int maxLineLength = 70;
		int currentLineLength = 0;
		for (int i = 0; i < moveList.size(); ++i) {
			int writtenLength = 0;
			if (i % 2 == 0) {
				// White's turn
				String moveNumber = Integer.toString(i/2+1) + '.'; 
				out.write(moveNumber);
				writtenLength += moveNumber.length();
			}
			String move = moveList.get(i);
			out.write(move);
			writtenLength += move.length();
			currentLineLength += writtenLength;
			if (currentLineLength >= maxLineLength) {
				currentLineLength = 0;
				out.write('\n');
			} else {
				out.write(' ');
			}
		}
		out.write(resultString + '\n');
		out.flush();
	}
}
