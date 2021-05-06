package cz.cvut.fel.pjv.tilhovoj.chess.game;

public abstract class Player {
	private String name;
	private PlayerColor color;
	private ChessGame game;
	
	public Player(String name, PlayerColor color, ChessGame game) {
		this.name = name;
		this.color = color;
		this.game = game;
	}
	
	public abstract void startTurn();
}
