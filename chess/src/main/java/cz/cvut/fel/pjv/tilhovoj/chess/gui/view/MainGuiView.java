package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.List;

import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

/**
 * Represents a top level view of a GUI MVC system.
 */
public class MainGuiView extends GuiTopView {
	
	private static final int MIN_WIDTH = 820;
	private static final int MIN_HEIGHT = 700;
	private static final String WINDOW_TITLE = "Chess game";
	
	private GuiSubView boardView;
	
	/**
	 * Constructs a new top level GUI view for the chess game application.
	 */
	public MainGuiView() {
		super(WINDOW_TITLE);
	}
	
	/**
	 * Get the board view.
	 * @return the chess board view in this main GUI view.
	 */
	public ChessBoardView getBoardView() {
		return (ChessBoardView)boardView;
	}
	
	@Override
	public void initView(MainGuiModel model, MainGuiController controller) {
		super.initView(model, controller);
		
		// Configure my view
		this.setLayout(new BorderLayout());

		JToolBar mainToolBar = new JToolBar();
		mainToolBar.setFloatable(false);
		JButton newGameButton = new JButton("New Game");
		JButton loadGameButton = new JButton("Load Game");
		JButton saveGameButton = new JButton("Save Game");
		JButton loadGamePGNButton = new JButton("Load Game As PGN");
		JButton saveGamePGNButton = new JButton("Save Game As PGN");
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGuiView.super.controller.newGame();
			}
		});
		loadGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGuiView.super.controller.loadGame();
			}
		});
		saveGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGuiView.super.controller.saveGame();
			}
		});
		loadGamePGNButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGuiView.super.controller.loadGamePGN();
			}
		});
		saveGamePGNButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGuiView.super.controller.saveGamePGN();
			}
		});
		mainToolBar.add(newGameButton);
		mainToolBar.add(loadGameButton);
		mainToolBar.add(saveGameButton);
		mainToolBar.add(loadGamePGNButton);
		mainToolBar.add(saveGamePGNButton);
		this.add(mainToolBar, BorderLayout.NORTH);
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Configure and add the board view
		boardView = new ChessBoardView();
		boardView.initView(model, controller);
		this.add(boardView, BorderLayout.WEST);
		
		this.setVisible(true);
	}
}
