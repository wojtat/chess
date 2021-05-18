package cz.cvut.fel.pjv.tilhovoj.chess.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.view.ChessBoardView;

/**
 * Manages resources, namely piece bitmaps, for the use of a GUI
 */
public class GuiResourceManager {
	private static GuiResourceManager singletonInstance = new GuiResourceManager();
	private static Logger LOG = Logger.getLogger(GuiResourceManager.class.getName());
	/**
	 * @return the singleton instance of the resource manager
	 */
	public static GuiResourceManager get() {
		return singletonInstance;
	}
	
	private BufferedImage[][] pieceImages;
	
	private GuiResourceManager() {
		createPieceImages();
	}
	
    private final void createPieceImages() {
    	pieceImages = new BufferedImage[2][ChessPieces.values().length];
        try {
            BufferedImage bi = ImageIO.read(GuiResourceManager.class.getResource("/pieces.png"));
            for (int color = 0; color < 2; ++color) {
                for (int i = 0; i < ChessPieces.values().length; i++) {
                	int w, h;
                	w = h = ChessBoardView.TILE_SIZE;
                    pieceImages[color][i] = bi.getSubimage(i * w, color * h, w, h);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.severe("CANNOT OPEN RESOURCE FILE");
            System.exit(1);
        }
    }
	
    /**
     * @param color the color of the piece the icon will represent
     * @param piece the piece the returned icon will represent
     * @return an icon representing the given piece of a specified color
     */
	public Icon getChessPieceIcon(PlayerColor color, ChessPieces piece) {
		int colorIndex = 1 - color.ordinal();
		int pieceIndex = piece.ordinal();
		return new ImageIcon(pieceImages[colorIndex][pieceIndex]);
	}
}
