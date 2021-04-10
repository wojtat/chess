package cz.cvut.fel.pjv.tilhovoj.chess.gui;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public class MainGuiModel {
	private ChessGame game;
	
	public MainGuiModel(ChessGame game) {
		this.game = game;
	}
	
	public Double getTime(PlayerColor player) {
		return game.getClock().getTime(player);
	}
	
	public void playMove() {
		game.getClock().hit();
	}
}
