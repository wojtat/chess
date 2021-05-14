package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessBoard;
import cz.cvut.fel.pjv.tilhovoj.chess.game.ChessClock;
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
			return new ChessGame(new ChessClock(5.0*60, 20.0), ChessBoard.fromFEN(fenField.getText()));
		} else {
			return null;
		}
	}
	
	public static ChessGame loadGameDialog() {
		return null;
	}
	
	public static void saveGameDialog() {
	}
	
	public static ChessGame loadGamePGNDialog() {
		return null;
	}
	
	public static void saveGamePGNDialog() {
	}
}
