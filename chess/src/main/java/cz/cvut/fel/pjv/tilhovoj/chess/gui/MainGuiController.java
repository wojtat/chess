package cz.cvut.fel.pjv.tilhovoj.chess.gui;

import java.util.logging.Logger;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessCoord;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessMove;
import cz.cvut.fel.pjv.tilhovoj.chess.game.PlayerColor;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;
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
	
	public void clickTile(ChessCoord coord) {
		ChessCoord selected = view.getBoardView().getSelectedTile(); 
		if (selected == null) {
			if (!model.getGameModel().getGame().getBoard().getTileAt(coord).isEmpty()) {
				PlayerColor onTurn = model.getGameModel().getGame().getOnTurn();
				if (model.getGameModel().getGame().getBoard().getTileAt(coord).getPiece().getColor() == onTurn) {
					view.getBoardView().setSelectedTile(coord);
					// TODO: Show possible moves
				}
			}
		} else {
			// Get the selected piece
			ChessPiece piece = model.getGameModel().getGame().getBoard().getTileAt(selected).getPiece();

			for (ChessMove move : piece.generatePossibleMoves(selected)) {
				if (coord.equals(move.getTo())) {
					// If move is possible, then do it
					model.getGameModel().getGame().playMove(move);
					LOG.fine("Moving from " + move.getFrom() + " to " + move.getTo());
					break;
				}
			}
			view.getBoardView().setSelectedTile(null);
		}
		view.getBoardView().updateView();
	}
}
