package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.util.List;

public class ComputerRandomPlayer implements Player {
	private static final long serialVersionUID = 3609403614358667102L;
	
	private PlayerColor color;
	private ChessGame game;
	private MoveGenerator currentlyRunningMoveGenerator;

	public ComputerRandomPlayer(PlayerColor color, ChessGame game) {
		this.color = color;
		this.game = game;
		this.currentlyRunningMoveGenerator = null;
	}

	@Override
	public void startPlaying() {
		// Do nothing
	}

	@Override
	public void stopPlaying() {
		if (currentlyRunningMoveGenerator != null) {
			currentlyRunningMoveGenerator.requestStop();
		}
	}

	@Override
	public void startTurn() {
		// Use 1/10 time that I have
		currentlyRunningMoveGenerator = new MoveGenerator(game.getClock().getTime(color) / 10);
		currentlyRunningMoveGenerator.start();
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	private class MoveGenerator extends Thread {
		private volatile boolean shouldStop;
		private double timeToThink;
		
		public MoveGenerator(double timeToThink) {
			this.shouldStop = false;
			this.timeToThink = timeToThink;
		}
		
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
		
		public void requestStop() {
			shouldStop = true;
		}
	}
}
