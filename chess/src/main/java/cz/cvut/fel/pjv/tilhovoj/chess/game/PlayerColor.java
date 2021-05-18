package cz.cvut.fel.pjv.tilhovoj.chess.game;

/**
 * Represents the color of a given player's pieces, specifying a team the player plays for.
 */
public enum PlayerColor {
	COLOR_WHITE,
	COLOR_BLACK;
	
	private static PlayerColor[] colors = values(); 
	
	/**
	 * @return the player color that conventionally moves first in a game
	 */
	public static PlayerColor getFirst() {
		return colors[0];
	}
	
	/**
	 * @param current current player's color
	 * @return the previous player's color
	 */
	public static PlayerColor getPrevious(PlayerColor current) {
		int previousIndex = (current.ordinal() + colors.length - 1) % colors.length;
		return colors[previousIndex];
	}

	/**
	 * 
	 * @param current current player's color
	 * @return the next player's color
	 */
	public static PlayerColor getNext(PlayerColor current) {
		int nextIndex = (current.ordinal() + 1) % colors.length;
		return colors[nextIndex];
	}
}
