package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;

public interface Player extends Serializable {
	public abstract void startPlaying();
	public abstract void stopPlaying();
	public abstract void startTurn();
	public boolean isLocal();
}
