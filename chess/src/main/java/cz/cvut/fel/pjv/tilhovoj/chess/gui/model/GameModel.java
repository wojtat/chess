package cz.cvut.fel.pjv.tilhovoj.chess.gui.model;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

/**
 * Serves as a wrapper around the chess game for the GUI. Part of the MVC GUI system.
 */
public class GameModel {

	private ChessGame game;
	
	/**
	 * Constructs a new game model wrapped around the specified chess game.
	 * @param game the game this model will hold.
	 */
	public GameModel(ChessGame game) {
		this.game = game;
	}
	
	/**
	 * Set the new game.
	 * @param game the new game that this model will hold a reference to from now on.
	 */
	public void setGame(ChessGame game) {
		this.game = game;
	}
	
	/**
	 * Get the current game.
	 * @return the game this model holds a reference to.
	 */
	public ChessGame getGame() {
		return game;
	}
}
