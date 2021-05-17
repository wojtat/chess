package cz.cvut.fel.pjv.tilhovoj.chess.pgn;

import java.io.IOException;
import java.io.InputStream;

public class PGNTokenizer {
	private InputStream in;
	private int current;
	private boolean isNextCached;
	
	public PGNTokenizer(InputStream in) {
		this.in = in;
		current = -1;
		isNextCached = false;
	}
	
	private int readNext() throws IOException {
		if (!isNextCached) {
			current = in.read();
		}
		isNextCached = false;
		return current;
	}
	
	private int peekNext() throws IOException {
		if (!isNextCached) {
			current = in.read();
			isNextCached = true;
		}
		return current;
	}
	
	private boolean skipToEndComment() throws IOException {
		int nextByte = -1;
		while ((nextByte = readNext()) != -1) {
			if (nextByte == '}') {
				return true;
			}
		}
		return false;
	}
	
	private boolean skipToEndLine() throws IOException {
		int nextByte = -1;
		while ((nextByte = readNext()) != -1) {
			if (nextByte == '\n') {
				return true;
			}
		}
		return false;
	}
	
	private String readString() throws IOException {
		int nextByte = -1;
		StringBuilder builder = new StringBuilder();
		while ((nextByte = readNext()) != -1) {
			if (nextByte == '"') {
				return builder.toString();
			} else if (nextByte == '\\' && peekNext() == '"') {
				nextByte = readNext();
			}
			builder.append((char)nextByte);
		}
		return null;
	}
	
	private boolean isSymbolCharacter(int character) {
		return Character.isAlphabetic(character) || (character >= '0' && character <= '9') ||
				character == '_' || character == '+' || character == '#' || character == '=' ||
				character == ':' || character == '-'; 
	}

	public PGNToken nextToken() throws IOException {
		int nextByte = -1;
		while ((nextByte = readNext()) != -1) {
			switch ((char)nextByte) {
			case ';':
				// Line comment
				if (!skipToEndLine()) {
					return new PGNToken(PGNToken.Type.EOF);
				}
			case '{':
				// Comment
				if (!skipToEndComment()) {
					return new PGNToken(PGNToken.Type.EOF);
				}
				break;
			case '.':
				return new PGNToken(PGNToken.Type.PERIOD);
			case '-':
				return new PGNToken(PGNToken.Type.HYPHEN);
			case '/':
				return new PGNToken(PGNToken.Type.SLASH);
			case '*':
				return new PGNToken(PGNToken.Type.ASTERISK);
			case '[':
				return new PGNToken(PGNToken.Type.OPEN_BRACKET);
			case ']':
				return new PGNToken(PGNToken.Type.CLOSE_BRACKET);
			case '<':
				return new PGNToken(PGNToken.Type.OPEN_ANGLE);
			case '>':
				return new PGNToken(PGNToken.Type.CLOSE_ANGLE);
			case '(':
				return new PGNToken(PGNToken.Type.OPEN_PAREN);
			case ')':
				return new PGNToken(PGNToken.Type.CLOSE_PAREN);
			case '"':
				String value = readString();
				if (value == null) {
					return new PGNToken(PGNToken.Type.EOF);
				} else {
					return new PGNToken(PGNToken.Type.STRING, value);
				}
			case '$':
				int nagValue = 0;
				do {
					nextByte = peekNext();
					if (nextByte < '0' || nextByte > '9') {
						break;
					}
					nextByte = readNext();
					nagValue = 10 * nagValue + nextByte;
				} while (nextByte != -1);
				return new PGNToken(PGNToken.Type.NAG, nagValue);
			default:
				if (nextByte >= '0' && nextByte <= '9') {
					// Integer token
					int intValue = 0;
					do {
						intValue = 10 * intValue + nextByte - '0';
						nextByte = peekNext();
						if (nextByte < '0' || nextByte > '9') {
							break;
						}
						nextByte = readNext();
					} while (nextByte != -1);
					if (nextByte == -1) {
						return new PGNToken(PGNToken.Type.EOF);
					}
					return new PGNToken(PGNToken.Type.INTEGER, intValue);
				} else if (Character.isAlphabetic(nextByte)) {
					// Symbol token
					StringBuilder builder = new StringBuilder();
					do {
						builder.append((char)nextByte);
						nextByte = peekNext();
						if (!isSymbolCharacter(nextByte)) {
							break;
						}
						nextByte = readNext();
					} while (nextByte != -1);
					return new PGNToken(PGNToken.Type.SYMBOL, builder.toString());
				} else if (Character.isWhitespace((char)nextByte)) {
					break;
				}
				return new PGNToken(PGNToken.Type.EOF);
			}
		}
		return new PGNToken(PGNToken.Type.EOF);
	}
}
