package cz.cvut.fel.pjv.tilhovoj.chess;

import javax.swing.SwingUtilities;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.*;

public class App {
	
	private ChessGame game;
	
	public void runApp() {
		/*
		ChessClock c = new ChessClock(5.0, .5);
		c.start();
		System.out.println("Started clock with 5 seconds and 0.5 seconds increment.");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.hit();
		System.out.println("Player thought for 1 second");
		System.out.println("WHITE played a move");
		System.out.println("WHITE has " + c.getTime(PlayerColor.COLOR_WHITE) + " seconds.");
		System.out.println("BLACK has " + c.getTime(PlayerColor.COLOR_BLACK) + " seconds.");
		*/
		
		game = new ChessGame(new ChessClock(5.0, .5));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		MainGuiModel model = new MainGuiModel(game);
        		MainGuiView view = new MainGuiView();
        		MainGuiController controller = new MainGuiController(model, view);
        		controller.init();
            }
        });
	}
	
    public static void main(String[] args) {
        App app = new App();
        app.runApp();
    }
}
