package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.pgn.*;

/**
 * Contains various dialog methods for loading / creating / saving chess games in different formats
 */
public class Dialog {
	/**
	 * Shows the new game dialog which lets the user pick what player will be human / computer controlled,
	 * what time control the game will be in and the starting position of the game.
	 * @return the newly constructed chess game that corresponds to the user's pick,
	 * null if anything went wrong
	 */
	public static ChessGame newGameDialog() {
		final String defaultChessPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		final String computerString = "Computer";
		final String humanString = "Human";
		
		JTextField fenField = new JTextField();
		fenField.setText(defaultChessPosition);
		JPanel fenPanel = new JPanel();
		fenPanel.setLayout(new BoxLayout(fenPanel, BoxLayout.PAGE_AXIS));
        fenPanel.add(new JLabel("FEN of the Initial Position"));
		fenPanel.add(fenField);
		
		JRadioButton whiteComputer = new JRadioButton(computerString);
		JRadioButton whiteHuman = new JRadioButton(humanString);
		ButtonGroup whitePlayerGroup = new ButtonGroup();
		whitePlayerGroup.add(whiteComputer);
		whitePlayerGroup.add(whiteHuman);
		whiteHuman.setSelected(true);
		JPanel whiteSelectionPanel = new JPanel();
		whiteSelectionPanel.setLayout(new BoxLayout(whiteSelectionPanel, BoxLayout.PAGE_AXIS));
		whiteSelectionPanel.add(new JLabel("White Is"));
		whiteSelectionPanel.add(whiteComputer);
		whiteSelectionPanel.add(whiteHuman);
		
		JRadioButton blackComputer = new JRadioButton(computerString);
		JRadioButton blackHuman = new JRadioButton(humanString);
		ButtonGroup blackPlayerGroup = new ButtonGroup();
		blackPlayerGroup.add(blackComputer);
		blackPlayerGroup.add(blackHuman);
		blackHuman.setSelected(true);
		JPanel blackSelectionPanel = new JPanel();
		blackSelectionPanel.setLayout(new BoxLayout(blackSelectionPanel, BoxLayout.PAGE_AXIS));
		blackSelectionPanel.add(new JLabel("Black Is"));
		blackSelectionPanel.add(blackComputer);
		blackSelectionPanel.add(blackHuman);
		
		JPanel playerSelectionPanel = new JPanel();
		playerSelectionPanel.setLayout(new BoxLayout(playerSelectionPanel, BoxLayout.LINE_AXIS));
		playerSelectionPanel.add(whiteSelectionPanel);
		playerSelectionPanel.add(blackSelectionPanel);
		
		JSlider startTimeSlider = new JSlider(JSlider.HORIZONTAL, 0, 60, 5);
		startTimeSlider.setMajorTickSpacing(10);
		startTimeSlider.setMinorTickSpacing(1);
		startTimeSlider.setPaintLabels(true);
		startTimeSlider.setPaintTicks(true);
		JSlider incrementSlider = new JSlider(JSlider.HORIZONTAL, 0, 60, 5);
		incrementSlider.setMajorTickSpacing(10);
		incrementSlider.setMinorTickSpacing(1);
		incrementSlider.setPaintLabels(true);
		incrementSlider.setPaintTicks(true);
		
		JPanel timeControlPanel = new JPanel();
		timeControlPanel.setLayout(new BoxLayout(timeControlPanel, BoxLayout.PAGE_AXIS));
		timeControlPanel.add(new JLabel("Start Time in Minutes"));
		timeControlPanel.add(startTimeSlider);
		timeControlPanel.add(new JLabel("Increment in Seconds"));
		timeControlPanel.add(incrementSlider);
		
		final JComponent[] inputs = new JComponent[] {
		        fenPanel,
		        playerSelectionPanel,
		        timeControlPanel
		};
		int result = JOptionPane.showConfirmDialog(null, inputs, "New Game", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			int startTime = startTimeSlider.getValue() * 60;
			int increment = incrementSlider.getValue();
			ChessGame game = new ChessGame((double)startTime, (double)increment, ChessBoard.fromFEN(fenField.getText()));
			Player whitePlayer = whiteHuman.isSelected() ? new HumanPlayer() : new ComputerPlayer(PlayerColor.COLOR_WHITE, game);
			Player blackPlayer = blackHuman.isSelected() ? new HumanPlayer() : new ComputerPlayer(PlayerColor.COLOR_BLACK, game);
			game.connectPlayer(PlayerColor.COLOR_WHITE, whitePlayer);
			game.connectPlayer(PlayerColor.COLOR_BLACK, blackPlayer);
			return game;
		} else {
			return null;
		}
	}
	
	/**
	 * Shows the load game dialog which lets the user specify a path to a file on the file system
	 * containing a valid saved chess game
	 * @return the chess game saved in the picked file, null if anything went wrong
	 */
	public static ChessGame loadGameDialog() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Chess Game Save File (*.chess)", "chess");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File selected = chooser.getSelectedFile();
			try {
				ObjectInputStream stream = new ObjectInputStream(new FileInputStream(selected));
				ChessGame game = (ChessGame)stream.readObject();
				stream.close();
				return game;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} 
	    }
		return null;
	}

	/**
	 * Shows the save game dialog which lets the user specify a path on the file system
	 * where the file containing the chess game will be saved
	 * @param game the game that will be saved to the file (may not end up being saved if the user cancels the dialog)
	 */
	public static void saveGameDialog(ChessGame game) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Chess Game Save File", "chess");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File selected = chooser.getSelectedFile();
			try {
				ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(selected));
				stream.writeObject(game);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}

	/**
	 * Shows the load game from PGN file dialog which lets the user specify a path to a file on the file system
	 * containing a valid chess game in the standard PGN format. If the file is a database, it loads the first
	 * game in the database
	 * @return the first chess game saved in the picked file, null if anything went wrong
	 */
	public static ChessGame loadGamePGNDialog() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filterPGN = new FileNameExtensionFilter("PGN File (*.pgn)", "pgn");
		FileNameExtensionFilter filterTXT = new FileNameExtensionFilter("TXT File (*.txt)", "txt");
		chooser.addChoosableFileFilter(filterTXT);
	    chooser.setFileFilter(filterPGN);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File selected = chooser.getSelectedFile();
			try {
				FileInputStream stream = new FileInputStream(selected);
				PGNParser parser = new PGNParser(stream);
				parser.parse();
				ChessGame game = parser.getResult();
				stream.close();
				return game;
			} catch (IOException e) {
				e.printStackTrace();
			} 
	    }
		return null;
	}

	/**
	 * Shows the save game as PGN dialog which lets the user specify a path on the file system
	 * where the file containing the chess game in the standard PGN format will be saved
	 * @param game the game that will be saved to the file (may not end up being saved if the user cancels the dialog)
	 */
	public static void saveGamePGNDialog(ChessGame game) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filterPGN = new FileNameExtensionFilter("PGN File (*.pgn)", "pgn");
		FileNameExtensionFilter filterTXT = new FileNameExtensionFilter("TXT File (*.txt)", "txt");
		chooser.addChoosableFileFilter(filterTXT);
	    chooser.setFileFilter(filterPGN);
	    int returnVal = chooser.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File selected = chooser.getSelectedFile();
			try {
				FileOutputStream stream = new FileOutputStream(selected);
				PGNWriter writer = new PGNWriter(stream);
				writer.writeChessGame(game);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
}
