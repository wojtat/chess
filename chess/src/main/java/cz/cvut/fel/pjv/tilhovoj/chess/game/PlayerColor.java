package cz.cvut.fel.pjv.tilhovoj.chess.game;

public enum PlayerColor {
	COLOR_WHITE,
	COLOR_BLACK;
	
	private static PlayerColor[] colors = values(); 
	
	public static PlayerColor getFirst() {
		return colors[0];
	}

	public static PlayerColor getNext(PlayerColor current) {
		int nextIndex = (current.ordinal() + 1) % colors.length;
		return colors[nextIndex];
	}
}