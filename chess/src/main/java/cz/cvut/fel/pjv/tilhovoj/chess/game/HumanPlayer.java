package cz.cvut.fel.pjv.tilhovoj.chess.game;

/**
 * This <a href="#{@link}">{@link Player}</a> does nothing. It can be used alongside a GUI system.
 */
public class HumanPlayer implements Player {
	
	/**
	 * Does nothing
	 */
	@Override
	public void startPlaying() {
		// Do nothing
	}

	/**
	 * Does nothing
	 */
	@Override
	public void stopPlaying() {
		// Do nothing
	}

	/**
	 * Does nothing
	 */
	@Override
	public void startTurn() {
		// Do nothing
	}

	/**
	 * Returns true because it represents a GUI aware player
	 */
	@Override
	public boolean isLocal() {
		return true;
	}
}
