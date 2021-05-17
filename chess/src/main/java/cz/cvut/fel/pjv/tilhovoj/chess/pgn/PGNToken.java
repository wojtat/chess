package cz.cvut.fel.pjv.tilhovoj.chess.pgn;

public class PGNToken {
	private Type type;
	private Object value;
	
	public PGNToken(Type type, Object value) {
		this.type = type;
		this.value = value;
	}

	public PGNToken(Type type) {
		this(type, null);
	}
	
	public Type getType() {
		return type;
	}
	
	public int getInteger() {
		return (int)value;
	}
	
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
