package cz.cvut.fel.pjv.tilhovoj.chess.gui;

import java.awt.FlowLayout;
import java.util.EnumMap;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;

public class MainGuiView extends JFrame {
	
	private MainGuiModel model;
	private MainGuiController controller;
	
	private EnumMap<PlayerColor, JLabel> timeLabels;
	private JButton playMoveButton;
	
	public MainGuiView() {
		super("Chess game");
		timeLabels = new EnumMap<>(PlayerColor.class);
	}
	
	public void initView(MainGuiModel model, MainGuiController controller) {
		this.model = model;
		this.controller = controller;
		
		this.setSize(480, 360);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		playMoveButton = new JButton("PLAY MOVE");
		this.add(playMoveButton);
		for (PlayerColor player : PlayerColor.values()) {
			JLabel label = new JLabel(model.getTime(player).toString());
			this.add(label);
			timeLabels.put(player, label);
		}
		this.setVisible(true);
	}
}
