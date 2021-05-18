package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;

/**
 * Defines the chess clock class that is connected to a chess game
 */
public class ChessClock {
	private ChessGame game;
	
	private Double startTime;
	private Double increment;
	private EnumMap<PlayerColor, ChessTimer> playersTime;
	private PlayerColor playerOnTurn;
	private volatile boolean isRunning;
	private Thread timeUpdater;

	/**
	 * Converts the time given in seconds to a GUI friendly string representation 
	 * @param time the time to be converted, in seconds
	 * @return the string representation of the time
	 */
	public static String timeToString(Double time) {
		final Double SECONDS_IN_MINUTE = 60.0;
		int minutes = (int)(time / SECONDS_IN_MINUTE);
		int seconds = (int)(time - minutes*SECONDS_IN_MINUTE);
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%02d", minutes));
		builder.append(':');
		builder.append(String.format("%02d", seconds));
		return builder.toString();
	}
	
	/**
	 * Constructs a new chess clock
	 * @param game the game this clock will be connected to
	 * @param first the color of the player who moves first
	 * @param startTime the initial time of both players at the start of the game in seconds
	 * @param increment the increment that will be added to the clock of a player after each move played in seconds 
	 */
	public ChessClock(ChessGame game, PlayerColor first, Double startTime, Double increment) {
		this.game = game;
		this.startTime = startTime;
		this.increment = increment;
		
		isRunning = false;
		playersTime = new EnumMap<PlayerColor, ChessTimer>(PlayerColor.class);
		for (PlayerColor p : PlayerColor.values()) {
			playersTime.put(p, new ChessTimer(startTime));
		}
		playerOnTurn = first;
		timeUpdater = new Thread(new Runnable() {
			@Override
			public void run() {
				final int waitTime = 20;
				while (true) {
					if (isRunning) {
						playersTime.get(playerOnTurn).updateTime();
						if (playersTime.get(playerOnTurn).hasFlagged()) {
							ChessClock.this.game.playerFlagged(playerOnTurn);
						}
					}
					
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		timeUpdater.setDaemon(true);
		timeUpdater.start();
	}
	
	/**
	 * @param player the player's color
	 * @return true if the player's flag has dropped, false otherwise
	 */
	public boolean hasFlagged(PlayerColor player) {
		return playersTime.get(player).hasFlagged();
	}
	
	/**
	 * @param player the player
	 * @return the time in seconds that the player has left
	 */
	public Double getTime(PlayerColor player) {
		return playersTime.get(player).getTime();
	}
	
	/**
	 * @param player the player
	 * @param time the new time in seconds of the given player 
	 */
	public void setTime(PlayerColor player, Double time) {
		playersTime.get(player).setTime(time);
	}
	
	/**
	 * @return the increment that is added after each move played in seconds
	 */
	public Double getIncrement() {
		return increment;
	}
	
	/**
	 * @return the time both players started with in seconds
	 */
	public Double getStartTime() {
		return startTime;
	}
	
	/**
	 * Make the clock of the player whose turn it is start running
	 */
	public void start() {
		if (!isRunning) {
			playersTime.get(playerOnTurn).start();
			isRunning = true;
		}
	}
	
	/**
	 * Stop the clock of the current player
	 */
	public void stop() {
		if (isRunning) {
			playersTime.get(playerOnTurn).stop();
			isRunning = false;
		}
	}
	
	/**
	 * Stop the clock of the current player and start the clock of the next player
	 */
	public void hit() {
		// Illegal state, throw exception
		if (!isRunning) {
			throw new IllegalStateException("An attempt has been made at hitting the clock when it was not running.");
		}
		
		// Stop the clock of the current player and add increment
		playersTime.get(playerOnTurn).stop();
		playersTime.get(playerOnTurn).addTime(increment);
		
		// Start the other player's clock
		playerOnTurn = PlayerColor.getNext(playerOnTurn);
		playersTime.get(playerOnTurn).start();
	}
	
	private class ChessTimer {
		private Double time;
		private boolean isRunning;
		private Instant timeWhenStarted;
		
		private synchronized void updateTime() {
			Instant now = Instant.now();
			long elapsedMillis = Duration.between(timeWhenStarted, now).toMillis();
			
			timeWhenStarted = now;
			time -= (double)elapsedMillis / 1000.0;
			if (time < .0) {
				time = .0;
			}
		}
		
		public ChessTimer(Double time) {
			this.time = time;
			isRunning = false;
			timeWhenStarted = Instant.now();
		}
		
		public boolean hasFlagged() {
			return time <= .0;
		}
		
		public Double getTime() {
			if (isRunning) {
				updateTime();
			}
			return time;
		}
		
		public synchronized void setTime(Double seconds) {
			time = seconds; 
		}
		
		public synchronized void addTime(Double seconds) {
			setTime(time + seconds);
			if (time < .0) {
				time = .0;
			}
		}
		
		public void start() {
			if (!isRunning) {
				timeWhenStarted = Instant.now();
				isRunning = true;	
			}
		}
		
		public void stop() {
			if (isRunning) {
				updateTime();
				isRunning = false;
			}
		}
	}
}
