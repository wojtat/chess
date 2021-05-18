package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

/**
 * Specifies a common interface for all views in a GUI MVC architecture
 */
public interface GuiView {
	/**
	 * Initialises the view, i.e. creates all the GUI specific components needed to display the model
	 * @param model the model that models this view
	 * @param controller the top level controller for the MVC GUI architecture
	 */
	void initView(MainGuiModel model, MainGuiController controller);
}
