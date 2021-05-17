package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;

public class ChessGame implements Serializable {
	private static final long serialVersionUID = 7300158544833018878L;
	
	private static final int DRAW_HALF_MOVE_CLOCK = 100;
	
	private State state;
	private PlayerColor winner;
	protected ChessClock clock;
	protected ChessBoard board;
	protected EnumMap<PlayerColor, Player> connectedPlayers;
	protected int currentMove;
	protected List<ChessMoveAction> moveList;
	protected List<String> sanMoveList;
	
	public ChessGame(Double startTime, Double increment, ChessBoard board) {
		this.clock = new ChessClock(this, board.getOnTurn(), startTime, increment);
		this.board = board;
		this.connectedPlayers = new EnumMap<>(PlayerColor.class);
		this.currentMove = 0;
		this.moveList = new ArrayList<>();
		this.sanMoveList = new ArrayList<>();
		this.state = State.NONE;
	}
	
	public void connectPlayer(PlayerColor color, Player player) {
		connectedPlayers.put(color, player);
	}
	
	public Player getPlayer(PlayerColor color) {
		return connectedPlayers.get(color);
	}
	
	public void startGame() {
		state = State.PLAYING;
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
	
	public boolean isPlaying() {
		return state == State.PLAYING;
	}
	
	public ChessBoard getBoard() {
		return board;
	}
	
	public ChessClock getClock() {
		return clock;
	}
	
	public List<String> getSANMoveList() {
		return sanMoveList;
	}
	
	public State getState() {
		return state;
	}
	
	public PlayerColor getWinner() {
		return winner;
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
		for (PlayerColor color : PlayerColor.values()) {
			if (connectedPlayers.containsKey(color)) {
				connectedPlayers.get(color).stopPlaying();
			}
		}
		clock.stop();
	}
	
	private String getSANStringForChessMoveAction(ChessMoveAction action) {
		// Special cases
		if (action.isCastleShort()) {
			return "O-O";
		} else if (action.isCastleLong()) {
			return "O-O-O";
		}
		StringBuilder builder = new StringBuilder();
		ChessPiece mover = board.getTileAt(action.getMove().getFrom()).getPiece();
		if (mover.getKind() != ChessPieces.PIECE_PAWN) {
			builder.append(ChessPieces.toSANCharacter(mover.getKind()));
		}
		// We disambiguate even when we don't have to, which should be handled correctly by other PGN readers
		builder.append(action.getMove().getFrom().toString());
		if (action.isCapture()) {
			builder.append('x');
		}
		builder.append(action.getMove().getTo().toString());
		if (action.isPromotion()) {
			builder.append("=" + ChessPieces.toSANCharacter(action.getPromotionPieceKind()));
		}
		return builder.toString();
	}
	
	public void playMove(ChessMove move) {
		if (state != State.PLAYING || currentMove != moveList.size()) {
			return;
		}
		ChessMoveAction action = board.getTileAt(move.getFrom()).getPiece().getActionFromMove(move);
		String sanString = getSANStringForChessMoveAction(action);
		board.playMove(action);
		clock.hit();
		
		++currentMove;
		moveList.add(action);
		if (board.isKingUnderAttack(board.getOnTurn())) {
			if (board.getNumLegalMoves() == 0) {
				sanString += '#';
			} else {
				sanString += '+';
			}
		}
		sanMoveList.add(sanString);
		
		int numLegalMoves = board.getNumLegalMoves();
		if (numLegalMoves == 0) {
			// This is the end of the game
			if (board.isKingUnderAttack(board.getOnTurn())) {
				// Checkmate
				state = State.WIN;
				winner = PlayerColor.getPrevious(board.getOnTurn());
				System.out.println("CHECKMATE, " + PlayerColor.getPrevious(board.getOnTurn()) + " wins.");
			} else {
				// Stalemate
				System.out.println("STALEMATE.");
			}
			endGame();
		} else if (board.getHalfMoveClock() >= DRAW_HALF_MOVE_CLOCK) {
			state = State.DRAW;
			System.out.println("50 MOVE RULE DRAW.");
			endGame();
		}
		if (state == State.PLAYING) {
			if (connectedPlayers.containsKey(board.getOnTurn())) {
				connectedPlayers.get(board.getOnTurn()).startTurn();
			}
		}
	}
	
	public void playerFlagged(PlayerColor player) {
		state = State.WIN;
		winner = PlayerColor.getPrevious(player);
		System.out.println("FLAGGED.");
		endGame();
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		state = (State)in.readObject();
		winner = (PlayerColor)in.readObject();
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
		sanMoveList = (List<String>)in.readObject();
    }
 
    private void writeObject(ObjectOutputStream out) throws IOException {
    	out.writeObject(state);
    	out.writeObject(winner);
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
    	out.writeObject(sanMoveList);
    }
    
    public static enum State {
    	NONE,
    	PLAYING,
    	DRAW,
    	WIN;
    }
}
