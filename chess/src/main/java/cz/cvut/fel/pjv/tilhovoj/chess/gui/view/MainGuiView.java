package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.awt.*;
import java.util.EnumMap;
import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

public class MainGuiView extends GuiTopView {
	
	static final int MIN_WIDTH = 820;
	static final int MIN_HEIGHT = 700;
	static final String WINDOW_TITLE = "Chess game";
	
	private GuiSubView sidePanelView;
	private GuiSubView gameView;
	
	public MainGuiView() {
		super(WINDOW_TITLE);
	}
	
	@Override
	public void initView(MainGuiModel model, MainGuiController controller) {
		super.initView(model, controller);
		
		// Configure my view
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		// Configure and add the game view
		gameView = new ChessBoardView();
		gameView.initView(model, controller);
		this.add(gameView, BorderLayout.WEST);
		
		// Configure and add side panel view
		sidePanelView = new SidePanelView();
		sidePanelView.initView(model, controller);
		this.add(sidePanelView, BorderLayout.EAST);
		
		this.setVisible(true);
	}
}
