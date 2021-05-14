package cz.cvut.fel.pjv.tilhovoj.chess.gui;

import java.util.logging.Logger;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessClock;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessGame;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMoveAction;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.view.Dialog;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.view.MainGuiView;

public class MainGuiController {
	private static final Logger LOG = Logger.getLogger(MainGuiController.class.getName());
	
	private MainGuiModel model;
	private MainGuiView view;
	
	public MainGuiController(MainGuiModel model, MainGuiView view) {
		this.model = model;
		this.view = view;
	}
	
	public void init() {
		view.initView(model, this);
	}
	
	public void nextMove() {
		model.getGameModel().getGame().goToNextMove();
		view.getBoardView().updateView();
	}
	
	public void previousMove() {
		model.getGameModel().getGame().goToPreviousMove();
		view.getBoardView().updateView();
	}
	
	private void resetGame(ChessGame newGame) {
		model.getGameModel().setGame(newGame);
		model.getGameModel().getGame().startGame();
		view.getBoardView().setSelectedTile(null);
		view.getBoardView().updateView();
	}
	
	public void newGame() {
		ChessGame newGame = null;
		newGame = Dialog.newGameDialog();
		if (newGame == null) {
			return;
		}
		resetGame(newGame);
	}
	
	public void loadGame() {
		ChessGame newGame = null;
		newGame = Dialog.loadGameDialog();
		if (newGame == null) {
			return;
		}
		resetGame(newGame);
	}
	
	public void saveGame() {
		Dialog.saveGameDialog();
	}
	
	public void loadGamePGN() {
		ChessGame newGame = null;
		newGame = Dialog.loadGamePGNDialog();
		if (newGame == null) {
			return;
		}
		resetGame(newGame);
	}
	
	public void saveGamePGN() {
		Dialog.saveGamePGNDialog();
	}
	
	public void clickPromotion(ChessMove move, ChessPieces kind) {
		move = new ChessMove(move.getFrom(), move.getTo(), kind);
		LOG.fine("Moving from " + move.getFrom() + " to " + move.getTo());
		view.getBoardView().hidePromoteToDialog();
		model.getGameModel().getGame().playMove(move);
		view.getBoardView().updateView();
	}
	
	public void clickTile(ChessCoord coord) {
		if (view.getBoardView().isInPromotionDialog()
				|| !model.getGameModel().getGame().isUpdated()
				|| model.getGameModel().getGame().beforeStartGame()) {
			// Ignore tile clicks when we're in the process of promotion or not in the current position or game hasn't started
			return;
		}
		ChessCoord selected = view.getBoardView().getSelectedTile(); 
		if (selected == null) {
			if (!model.getGameModel().getGame().getBoard().getTileAt(coord).isEmpty()) {
				PlayerColor onTurn = model.getGameModel().getGame().getBoard().getOnTurn();
				if (model.getGameModel().getGame().getBoard().getTileAt(coord).getPiece().getColor() == onTurn) {
					view.getBoardView().setSelectedTile(coord);
					// TODO: Show possible moves
				}
			}
		} else {
			// Get the selected piece
			ChessPiece piece = model.getGameModel().getGame().getBoard().getTileAt(selected).getPiece();

			for (ChessMoveAction action : piece.generateLegalMoves(selected)) {
				ChessMove move = action.getMove();
				if (coord.equals(move.getTo())) {
					// The move is possible
					if (move.getPromoteToKind() != null) {
						// It's a promotion. Let the player choose the promoted piece
						view.getBoardView().showPromoteToDialog(move);
						break;
					} else {
						LOG.fine("Moving from " + move.getFrom() + " to " + move.getTo());
						model.getGameModel().getGame().playMove(move);
					}
					break;
				}
			}
			view.getBoardView().setSelectedTile(null);
		}
		view.getBoardView().updateView();
	}
}
