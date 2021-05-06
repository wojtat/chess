package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.util.EnumMap;
import java.awt.*;
import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

public class SidePanelView extends GuiSubView {

	private static final int WIDTH = 120;

	private EnumMap<PlayerColor, JLabel> timeLabels;
	private JButton playMoveButton;
	
	public SidePanelView() {
		super();
		timeLabels = new EnumMap<>(PlayerColor.class);
	}
	
	@Override
	public void initView(MainGuiModel model, MainGuiController controller) {
		super.initView(model, controller);
		
		this.setPreferredSize(new Dimension(WIDTH, this.getPreferredSize().height));
		this.setBackground(Color.LIGHT_GRAY);
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		playMoveButton = new JButton("PLAY MOVE");
		this.add(playMoveButton);
		for (PlayerColor player : PlayerColor.values()) {
			JLabel label = new JLabel(model.getGameModel().getTime(player).toString());
			this.add(label);
			timeLabels.put(player, label);
		}
	}
}
