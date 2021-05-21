package cz.cvut.fel.pjv.tilhovoj.chess.gui.model;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

/**
 * Serves as a model part of the MVC GUI architecture modeling the top view.
 */
public class MainGuiModel {
	
	private GameModel gameModel;
	
	/**
	 * Constructs a new top view model with no chess game displayed.
	 */
	public MainGuiModel() {
		ChessGame newGame = null;
		this.gameModel = new GameModel(newGame);
	}
	
	/**
	 * Get the current game model.
	 * @return the game model.
	 */
	public GameModel getGameModel() {
		return gameModel;
	}
}
