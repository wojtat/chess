package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.util.List;

/**
 * This <a href="#{@link}">{@link Player}</a> only plays the first possible move
 * and waits a certain percentage of its time.
 *
 */
public class ComputerPlayer implements Player {
	private PlayerColor color;
	private ChessGame game;
	private MoveGenerator currentlyRunningMoveGenerator;

	/**
	 * Constructs a new <a href="#{@link}">{@link ComputerPlayer}</a>.
	 * @param color the color this player will play as
	 * @param game the game object that the player will be a part of
	 */
	public ComputerPlayer(PlayerColor color, ChessGame game) {
		this.color = color;
		this.game = game;
		this.currentlyRunningMoveGenerator = null;
	}

	/**
	 * Does nothing when instructed to start playing.
	 */
	@Override
	public void startPlaying() {
		// Do nothing
	}

	/**
	 * Instructs the underlying thread to stop execution.
	 */
	@Override
	public void stopPlaying() {
		if (currentlyRunningMoveGenerator != null) {
			currentlyRunningMoveGenerator.requestStop();
		}
	}

	/**
	 * Starts the underlying thread that does the computational work.
	 */
	@Override
	public void startTurn() {
		// Use 1/10 time that I have
		currentlyRunningMoveGenerator = new MoveGenerator(game.getClock().getTime(color) / 10);
		currentlyRunningMoveGenerator.start();
	}

	/**
	 * Returns false, because <a href="#{@link}">{@link ComputerPlayer}</a> isn't a user of the GUI
	 */
	@Override
	public boolean isLocal() {
		return false;
	}

	/**
	 * The <a href="#{@link}">{@link Thread}</a> class that does the computational work
	 */
	private class MoveGenerator extends Thread {
		private volatile boolean shouldStop;
		private double timeToThink;
		
		/**
		 * Constructs a new <a href="#{@link}">{@link MoveGenerator}</a>
		 * @param timeToThink the time in seconds that will be spent on the move
		 */
		public MoveGenerator(double timeToThink) {
			this.shouldStop = false;
			this.timeToThink = timeToThink;
		}
		
		/**
		 * Gets the first possible move that can be played
		 * and then waits the time it was instructed to and plays the move
		 */
		@Override
		public void run() {
			double timeSpentThinking = 0.0;
			
			ChessMove move = null;
			
			// Get the first available move
			for (int rank = 1; rank <= ChessBoard.NUM_RANKS; ++rank) {
				for (int file = 1; file <= ChessBoard.NUM_FILES; ++file) {
					Tile tile = game.getBoard().getTileAt(rank, file);
					if (!tile.isEmpty() && tile.getPiece().getColor() == color) {
						List<ChessMoveAction> moves = tile.getPiece().generateLegalMoves(new ChessCoord(rank, file));
						if (moves.size() != 0) {
							move = moves.get(0).getMove();
						}
					}
				}
			}
			
			// Do some artificial thinking
			while (!shouldStop && timeSpentThinking < timeToThink) {
				final long timePerRound = 10;
				try {
					// Do some calculation
					Thread.sleep(timePerRound);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timeSpentThinking += timePerRound / 1000.0;
			}
			
			if (!shouldStop) {
				game.playMove(move);
			}
		}
		
		/**
		 * Tells the <a href="#{@link}">{@link MoveGenerator}</a> that it should stop computing
		 */
		public void requestStop() {
			shouldStop = true;
		}
	}
}
