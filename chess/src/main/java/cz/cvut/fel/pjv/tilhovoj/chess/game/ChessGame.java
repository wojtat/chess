package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class ChessGame implements Serializable {
	private static final long serialVersionUID = 7300158544833018878L;
	
	private boolean beforeStartGame;
	private ChessClock clock;
	private ChessBoard board;
	private EnumMap<PlayerColor, Player> connectedPlayers;
	private int currentMove;
	private List<ChessMoveAction> moveList;
	
	public ChessGame(Double startTime, Double increment, ChessBoard board) {
		this.clock = new ChessClock(this, board.getOnTurn(), startTime, increment);
		this.board = board;
		this.connectedPlayers = new EnumMap<>(PlayerColor.class);
		this.currentMove = 0;
		this.moveList = new ArrayList<>();
		this.beforeStartGame = true;
	}
	
	public void connectPlayer(PlayerColor color, Player player) {
		connectedPlayers.put(color, player);
	}
	
	public Player getPlayer(PlayerColor color) {
		return connectedPlayers.get(color);
	}
	
	public void startGame() {
		beforeStartGame = false;
		clock.start();
		for (PlayerColor color : PlayerColor.values()) {
			if (connectedPlayers.containsKey(color)) {
				connectedPlayers.get(color).startPlaying();
			}
		}
		if (connectedPlayers.containsKey(board.getOnTurn())) {
			connectedPlayers.get(board.getOnTurn()).startTurn();
		}
	}
	
	public boolean beforeStartGame() {
		return beforeStartGame;
	}
	
	public ChessBoard getBoard() {
		return board;
	}
	
	public ChessClock getClock() {
		return clock;
	}
	
	public boolean isUpdated() {
		return currentMove == moveList.size();
	}
	
	public boolean goToPreviousMove() {
		if (currentMove <= 0) {
			return false;
		}
		board.unplayMove(moveList.get(--currentMove));
		return true;
	}
	
	public boolean goToNextMove() {
		if (isUpdated()) {
			return false;
		}
		board.playMove(moveList.get(currentMove++));
		return true;
	}
	
	private void endGame() {
		beforeStartGame = true;
		for (PlayerColor color : PlayerColor.values()) {
			if (connectedPlayers.containsKey(color)) {
				connectedPlayers.get(color).stopPlaying();
			}
		}
		clock.stop();
	}
	
	public void playMove(ChessMove move) {
		if (beforeStartGame || currentMove != moveList.size()) {
			return;
		}
		ChessMoveAction action = board.getTileAt(move.getFrom()).getPiece().getActionFromMove(move);
		board.playMove(action);
		clock.hit();
		
		++currentMove;
		moveList.add(action);
		
		int numLegalMoves = board.getNumLegalMoves();
		if (numLegalMoves == 0) {
			// This is the end of the game
			beforeStartGame = true;
			clock.stop();
			if (board.isKingUnderAttack(board.getOnTurn())) {
				// Checkmate
				endGame();
				System.out.println("CHECKMATE, " + PlayerColor.getPrevious(board.getOnTurn()) + " wins.");
			} else {
				// Stalemate
				endGame();
				System.out.println("STALEMATE.");
			}
		}
		if (!beforeStartGame) {
			if (connectedPlayers.containsKey(board.getOnTurn())) {
				connectedPlayers.get(board.getOnTurn()).startTurn();
			}
		}
	}
	
	public void playerFlagged(PlayerColor player) {
		endGame();
		System.out.println("FLAGGED.");
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		beforeStartGame = in.readBoolean();
		board = (ChessBoard)in.readObject();
		connectedPlayers = new EnumMap<>(PlayerColor.class);
		for (PlayerColor color : PlayerColor.values()) {
			boolean isLocal = in.readBoolean();
			if (isLocal) {
				connectedPlayers.put(color, new HumanPlayer());
			} else {
				connectedPlayers.put(color, new ComputerRandomPlayer(color, this));
			}
		}
		double startTime = in.readDouble();
		double increment = in.readDouble();
		double whiteTime = in.readDouble();
		double blackTime = in.readDouble();
		clock = new ChessClock(this, board.getOnTurn(), startTime, increment);
		clock.setTime(PlayerColor.COLOR_WHITE, whiteTime);
		clock.setTime(PlayerColor.COLOR_BLACK, blackTime);
		currentMove = in.readInt();
		moveList = (List<ChessMoveAction>)in.readObject();
    }
 
    private void writeObject(ObjectOutputStream out) throws IOException {
    	out.writeBoolean(beforeStartGame);
    	out.writeObject(board);
		for (PlayerColor color : PlayerColor.values()) {
			Player player = connectedPlayers.get(color);
			if (player == null || player.isLocal()) {
				out.writeBoolean(true);
			} else {
				out.writeBoolean(false);
			}
		}
    	out.writeDouble(clock.getStartTime());
    	out.writeDouble(clock.getIncrement());
    	out.writeDouble(clock.getTime(PlayerColor.COLOR_WHITE));
    	out.writeDouble(clock.getTime(PlayerColor.COLOR_BLACK));
    	out.writeInt(currentMove);
    	out.writeObject(moveList);
    }
}
