package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

public interface GuiView {
	void initView(MainGuiModel model, MainGuiController controller);
}
