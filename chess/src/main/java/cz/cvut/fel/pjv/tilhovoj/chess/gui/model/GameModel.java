package cz.cvut.fel.pjv.tilhovoj.chess.gui.model;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public class GameModel {

	private ChessGame game;
	
	public GameModel(ChessGame game) {
		this.game = game;
	}
	
	public void setGame(ChessGame game) {
		this.game = game;
	}
	
	public Double getTime(PlayerColor player) {
		return game.getClock().getTime(player);
	}
	
	public ChessGame getGame() {
		return game;
	}
	
	public void playMove() {
		game.getClock().hit();
	}
}
