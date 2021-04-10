package cz.cvut.fel.pjv.tilhovoj.chess.gui;

public class MainGuiController {
	
	private MainGuiModel model;
	private MainGuiView view;
	
	public MainGuiController(MainGuiModel model, MainGuiView view) {
		this.model = model;
		this.view = view;
	}
	
	public void init() {
		view.initView(model, this);
	}
}
