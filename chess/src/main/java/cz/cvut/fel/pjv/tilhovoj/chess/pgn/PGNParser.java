package cz.cvut.fel.pjv.tilhovoj.chess.pgn;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessGame;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ViewableChessGame;

public class PGNParser {
	private static Logger LOG = Logger.getLogger(PGNParser.class.getName());
	private PGNTokenizer tokenizer;
	private ViewableChessGame result;
	private boolean parsed;
	
	public PGNParser(InputStream in) {
		this.tokenizer = new PGNTokenizer(in);
		this.parsed = false;
	}
	
	private boolean expect(PGNToken token, PGNToken.Type type) {
		LOG.fine("Got " + token);
		return token.getType() == type;
	}
	
	private boolean verify(PGNToken token, PGNToken.Type type) {
		if (token.getType() == type) {
			LOG.fine("Got " + token);
			return true;
		} else {
			LOG.severe("Expected type" + type + ", but got " + token);
			return false;
		}
	}
	
	public boolean parse() {
		if (parsed) {
			LOG.warning("Attempt to parse an already parsed file");
			return parsed;
		}
		try {
			String fenPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; 
			PGNToken token;
			// Parse tag pair section
			// We ignore most of this part, because we don't use this information anywhere
			for (token = tokenizer.nextToken(); !expect(token, PGNToken.Type.EOF); token = tokenizer.nextToken()) {
				if (!expect(token, PGNToken.Type.OPEN_BRACKET)) {
					break;
				}
				token = tokenizer.nextToken();
				boolean isFenTag = false;
				if (verify(token, PGNToken.Type.SYMBOL)) {
					if (token.getString() == "FEN") {
						isFenTag = true;
					}
				} else {
					break;
				}
				token = tokenizer.nextToken();
				if (verify(token, PGNToken.Type.STRING)) {
					if (isFenTag) {
						fenPosition = token.getString();
					}
				} else {
					break;
				}
				token = tokenizer.nextToken();
				if (!verify(token, PGNToken.Type.CLOSE_BRACKET)) {
					break;
				}
			}
			result = new ViewableChessGame(ChessBoard.fromFEN(fenPosition));
			
			// Now we parse the move text section
			while (!expect(token, PGNToken.Type.EOF)) {
				if (expect(token, PGNToken.Type.INTEGER)) {
					int number = token.getInteger();
					// If there is a move number, skip it
					token = tokenizer.nextToken();
					if (number == 0 || number == 1) {
						if (expect(token, PGNToken.Type.HYPHEN) || expect(token, PGNToken.Type.SLASH)) {
							// This is the game result, we can stop
							parsed = true;
							break;
						}
					}
				}
				// Read all the periods
				while (expect(token, PGNToken.Type.PERIOD)) {
					token = tokenizer.nextToken();
				}
				// Skip Recursive Annotation Variation
				if (expect(token, PGNToken.Type.OPEN_PAREN)) {
					int depth = 1;
					do {
						token = tokenizer.nextToken();
						if (expect(token, PGNToken.Type.CLOSE_PAREN)) {
							--depth;
						} else if (expect(token, PGNToken.Type.OPEN_PAREN)) {
							++depth;
						}
					} while (depth > 0);
					token = tokenizer.nextToken();
				}
				if (!expect(token, PGNToken.Type.SYMBOL)) {
					// This would be invalid
					break;
				}
				// Now decode the move
				String move = token.getString();
				result.decodeAndPlayMove(move);
				token = tokenizer.nextToken();
			}
		} catch (IOException e) {
		}
		
		return parsed;
	}
	
	public ChessGame getResult() {
		if (!parsed) {
			return null;
		}
		return result;
	}
}
