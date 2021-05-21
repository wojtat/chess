package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;

/**
 * Holds the information about a chess game, i.e. the clock, board,
 * player types, the state of the game and all the moves.
 */
public class ChessGame implements Serializable {
	private static final long serialVersionUID = 7300158544833018878L;
	
	protected static Logger LOG = Logger.getLogger(ChessGame.class.getName());
	private static final int DRAW_HALF_MOVE_CLOCK = 100;
	
	private State state;
	private PlayerColor winner;
	protected ChessClock clock;
	protected ChessBoard board;
	protected EnumMap<PlayerColor, Player> connectedPlayers;
	protected int currentMove;
	protected List<ChessMoveAction> moveList;
	protected List<String> sanMoveList;
	
	/**
	 * Constructs a new game with the given starting time and increment in seconds and the board.
	 * @param startTime the starting time in seconds.
	 * @param increment the increment in seconds.
	 * @param board the initial chess board.
	 */
	public ChessGame(Double startTime, Double increment, ChessBoard board) {
		this.clock = new ChessClock(this, board.getOnTurn(), startTime, increment);
		this.board = board;
		this.connectedPlayers = new EnumMap<>(PlayerColor.class);
		this.currentMove = 0;
		this.moveList = new ArrayList<>();
		this.sanMoveList = new ArrayList<>();
		this.state = State.NONE;
	}
	
	/**
	 * Connect the player type to the game.
	 * @param color the color of the player.
	 * @param player the player type.
	 */
	public void connectPlayer(PlayerColor color, Player player) {
		connectedPlayers.put(color, player);
	}
	
	/**
	 * Get the player type associated with the given color.
	 * @param color the color of the player.
	 * @return the player type associated with the color.
	 */
	public Player getPlayer(PlayerColor color) {
		return connectedPlayers.get(color);
	}
	
	/**
	 * Start the clock and notify the player that it is their turn.
	 */
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
	
	/**
	 * Get whether the game is being played right now.
	 * @return true if the game is being played right now.
	 */
	public boolean isPlaying() {
		return state == State.PLAYING;
	}
	
	/**
	 * Get the current board.
	 * @return the current board.
	 */
	public ChessBoard getBoard() {
		return board;
	}
	
	/**
	 * Get the chess clock.
	 * @return the clock.
	 */
	public ChessClock getClock() {
		return clock;
	}
	
	/**
	 * Get the list of moves represented in standard algebraic notation.
	 * @return the list of moves represented in standard algebraic notation.
	 */
	public List<String> getSANMoveList() {
		return sanMoveList;
	}
	
	/**
	 * Get the state of the game.
	 * @return the current state of the game.
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Get the winner of the game.
	 * @return the winner of the game, if there is one, null otherwise.
	 */
	public PlayerColor getWinner() {
		return winner;
	}
	
	/**
	 * Get whether the board is displaying the most recent move.
	 * @return true if the board is displaying the most recent move, false otherwise.
	 */
	public boolean isUpdated() {
		return currentMove == moveList.size();
	}
	
	/**
	 * Makes the board display a previous move.
	 * @return false if this it is the initial position now, false otherwise. 
	 */
	public boolean goToPreviousMove() {
		if (currentMove <= 0) {
			return false;
		}
		board.unplayMove(moveList.get(--currentMove));
		return true;
	}
	
	/**
	 * Makes the board display the next move.
	 * @return false if it is the most recent position now, false otherwise.
	 */
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
	
	/**
	 * Plays the given move and saves it to the list of played moves.
	 * @param move the move that is to be played.
	 */
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
				LOG.fine("CHECKMATE, " + PlayerColor.getPrevious(board.getOnTurn()) + " wins.");
			} else {
				// Stalemate
				LOG.fine("STALEMATE.");
			}
			endGame();
		} else if (board.getHalfMoveClock() >= DRAW_HALF_MOVE_CLOCK) {
			state = State.DRAW;
			LOG.fine("50 MOVE RULE DRAW.");
			endGame();
		}
		if (state == State.PLAYING) {
			if (connectedPlayers.containsKey(board.getOnTurn())) {
				connectedPlayers.get(board.getOnTurn()).startTurn();
			}
		}
	}
	
	/**
	 * Informs the game that a player's flag has dropped.
	 * @param player the player.
	 */
	public void playerFlagged(PlayerColor player) {
		state = State.WIN;
		winner = PlayerColor.getPrevious(player);
		LOG.fine(player + " FLAGGED.");
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
				connectedPlayers.put(color, new ComputerPlayer(color, this));
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
    
    /**
     * Represents the state of the game.
     */
    public static enum State {
    	NONE,
    	PLAYING,
    	DRAW,
    	WIN;
    }
}
