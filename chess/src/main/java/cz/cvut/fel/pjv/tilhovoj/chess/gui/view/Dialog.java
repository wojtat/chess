package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessGame;

public class Dialog {
	public static ChessGame newGameDialog() {
		JTextField fenField = new JTextField();
		fenField.setText("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		final JComponent[] inputs = new JComponent[] {
		        new JLabel("FEN of the Initial Position"),
		        fenField
		};
		int result = JOptionPane.showConfirmDialog(null, inputs, "New Game", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			return new ChessGame(30.0, 2.0, ChessBoard.fromFEN(fenField.getText()));
		} else {
			return null;
		}
	}
	
	public static ChessGame loadGameDialog() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Chess Game Save File", "chess");
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
	
	public static ChessGame loadGamePGNDialog() {
		return null;
	}
	
	public static void saveGamePGNDialog() {
	}
}
