package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.awt.*;
import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.gui.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.*;

/**
 * Represents an abstract view that is a part of a top view.
 */
public abstract class GuiSubView extends JPanel implements GuiView {
	
	protected MainGuiModel model;
	protected MainGuiController controller;
	
	@Override
	public void initView(MainGuiModel model, MainGuiController controller) {
		this.model = model;
		this.controller = controller;
	}
}
