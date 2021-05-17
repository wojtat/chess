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

/**
 * The main startup class that is instantiated at launch.
 *
 */
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
	
	/**
	 * Start the main GUI application.
	 */
	public void runApp() {
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
	
	/**
	 * The entry point.
	 * @param args unused
	 */
    public static void main(String[] args) {
        App app = new App();
        app.runApp();
    }
}
