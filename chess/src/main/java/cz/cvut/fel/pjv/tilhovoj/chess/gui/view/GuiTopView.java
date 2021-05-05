package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

public abstract class GuiTopView extends JFrame implements GuiView {
	
	protected MainGuiModel model;
	protected MainGuiController controller;
	
	public GuiTopView(String title) {
		super(title);
	}
	
	@Override
	public void initView(MainGuiModel model, MainGuiController controller) {
		this.model = model;
		this.controller = controller;
	}
}
