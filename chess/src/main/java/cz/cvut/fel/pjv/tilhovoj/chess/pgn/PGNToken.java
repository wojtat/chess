package cz.cvut.fel.pjv.tilhovoj.chess.pgn;

/**
 * Represents a token in the PGN text stream
 */
public class PGNToken {
	private Type type;
	private Object value;
	
	/**
	 * Construct a new PGN token
	 * @param type the type of the token
	 * @param value the value associated with the token type
	 */
	public PGNToken(Type type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Construct a new PGN token
	 * @param type the type of the token
	 */
	public PGNToken(Type type) {
		this(type, null);
	}
	
	/**
	 * @return the type of the token
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * @return the value of the token in the form of an integer
	 */
	public int getInteger() {
		return (int)value;
	}
	
	/**
	 * @return the value of the token in the form of a string
	 */
	public String getString() {
		return (String)value;
	}
	
	@Override
	public String toString() {
		String result = type.toString();
		if (type == Type.INTEGER) {
			result += " " + getInteger();
		} else if (type == Type.NAG) {
			result += " " + getString();
		} else if (type == Type.STRING) {
			result += " " + getString();
		} else if (type == Type.SYMBOL) {
			result += " " + getString();
		}
		return result;
	}
	
	/**
	 * The token type
	 */
	public static enum Type {
		EOF,
		STRING,
		INTEGER,
		PERIOD,
		ASTERISK,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		OPEN_PAREN,
		CLOSE_PAREN,
		OPEN_ANGLE,
		CLOSE_ANGLE,
		NAG,
		HYPHEN,
		SLASH,
		SYMBOL
	}
}
