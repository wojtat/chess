package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;

public class ChessClock {
	
	private Double startTime;
	private Double increment;
	private EnumMap<PlayerColor, ChessTimer> playersTime;
	private PlayerColor playerOnTurn;
	private boolean isRunning;
	
	public ChessClock(Double startTime, Double increment) {
		this.startTime = startTime;
		this.increment = increment;
		
		isRunning = false;
		playersTime = new EnumMap<PlayerColor, ChessTimer>(PlayerColor.class);
		for (PlayerColor p : PlayerColor.values()) {
			playersTime.put(p, new ChessTimer(startTime));
		}
		playerOnTurn = PlayerColor.getFirst();
	}
	
	public boolean hasFlagged(PlayerColor player) {
		return playersTime.get(player).hasFlagged();
	}
	
	public Double getTime(PlayerColor player) {
		return playersTime.get(player).getTime();
	}
	
	public Double getIncrement() {
		return increment;
	}
	
	public Double getStartTime() {
		return startTime;
	}
	
	public void start() {
		if (!isRunning) {
			playersTime.get(playerOnTurn).start();
			isRunning = true;	
		}
	}
	
	public void stop() {
		if (isRunning) {
			playersTime.get(playerOnTurn).stop();
			isRunning = false;
		}
	}
	
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
		
		private void updateTime() {
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
		
		public void addTime(Double seconds) {
			time += seconds;
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
