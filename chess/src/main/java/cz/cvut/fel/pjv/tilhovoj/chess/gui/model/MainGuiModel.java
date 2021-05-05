package cz.cvut.fel.pjv.tilhovoj.chess.gui.model;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public class MainGuiModel {
	
	private GameModel gameModel;
	
	public MainGuiModel() {
		ChessGame newGame = new ChessGame(new ChessClock(5.0, .5));
		this.gameModel = new GameModel(newGame);
	}
	
	public GameModel getGameModel() {
		return gameModel;
	}
}
