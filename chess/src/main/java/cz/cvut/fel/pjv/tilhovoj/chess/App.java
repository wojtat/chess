package cz.cvut.fel.pjv.tilhovoj.chess;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.swing.SwingUtilities;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.view.MainGuiView;

public class App {
	
	private void setupDebugLogging() {
		Logger rootLogger = Logger.getLogger("cz.cvut.fel.pjv.tilhovoj.chess");
		rootLogger.setLevel(Level.FINE);
		
		Handler stdout = new StreamHandler(System.out, new SimpleFormatter()) {
		    @Override
		    public void publish(LogRecord record) {
		        super.publish(record);
		        flush();
		    }
		};
		stdout.setLevel(Level.FINE);
		rootLogger.addHandler(stdout);
	}
	
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
		
		// Setup logging
		setupDebugLogging();
		
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		MainGuiModel model = new MainGuiModel();
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
