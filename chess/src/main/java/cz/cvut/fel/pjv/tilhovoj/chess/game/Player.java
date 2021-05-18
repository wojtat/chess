package cz.cvut.fel.pjv.tilhovoj.chess.game;

/**
 * Defines an interface between a player type implementation and the chess game.
 * Allows for different types of players, such as a network human player,
 * computer player, local human player
 */
public interface Player {
	/**
	 * The game calls this method when the game starts.
	 * This does not mean that it is this player's turn.
	 */
	public abstract void startPlaying();
	/**
	 * The game calls this method when the game ends.
	 * The player should not communicate with the game anymore after this point.
	 */
	public abstract void stopPlaying();
	/**
	 * The game calls this method when it becomes this player's turn.
	 * The game expects that a move will be played.
	 */
	public abstract void startTurn();
	/**
	 * Local players are the ones whose moves come from the UI connected to the game.
	 * @return true if this player is local, false otherwise
	 */
	public boolean isLocal();
}
