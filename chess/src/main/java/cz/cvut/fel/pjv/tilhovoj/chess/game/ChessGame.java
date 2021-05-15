package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChessGame implements Serializable {
	private static final long serialVersionUID = 7300158544833018878L;
	
	private boolean beforeStartGame;
	private ChessClock clock;
	private ChessBoard board;
	private int currentMove;
	private List<ChessMoveAction> moveList;
	
	public ChessGame(Double startTime, Double increment, ChessBoard board) {
		this.clock = new ChessClock(this, board.getOnTurn(), startTime, increment);
		this.board = board;
		this.currentMove = 0;
		this.moveList = new ArrayList<>();
		this.beforeStartGame = true;
	}
	
	public void startGame() {
		beforeStartGame = false;
		clock.start();
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
				System.out.println("CHECKMATE, " + PlayerColor.getPrevious(board.getOnTurn()) + " wins.");
			} else {
				// Stalemate
				System.out.println("STALEMATE.");
			}
		}
	}

	public void playerFlagged(PlayerColor player) {
		beforeStartGame = true;
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException 
    {
		beforeStartGame = in.readBoolean();
		board = (ChessBoard)in.readObject();
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
 
    private void writeObject(ObjectOutputStream out) throws IOException 
    {
    	out.writeBoolean(beforeStartGame);
    	out.writeObject(board);
    	out.writeDouble(clock.getStartTime());
    	out.writeDouble(clock.getIncrement());
    	out.writeDouble(clock.getTime(PlayerColor.COLOR_WHITE));
    	out.writeDouble(clock.getTime(PlayerColor.COLOR_BLACK));
    	out.writeInt(currentMove);
    	out.writeObject(moveList);
    }
}
