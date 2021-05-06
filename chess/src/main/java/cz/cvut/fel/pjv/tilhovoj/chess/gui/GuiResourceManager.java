package cz.cvut.fel.pjv.tilhovoj.chess.gui;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.*;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.view.ChessBoardView;

public class GuiResourceManager {
	private static GuiResourceManager singletonInstance = new GuiResourceManager();
	
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
            File path = new File("S:/PJV/tilhovoj/chess/src/resources/icons/pieces.png");
            BufferedImage bi = ImageIO.read(path);
            for (int color = 0; color < 2; ++color) {
                for (int i = 0; i < ChessPieces.values().length; i++) {
                	int w, h;
                	w = h = ChessBoardView.TILE_SIZE;
                    pieceImages[color][i] = bi.getSubimage(i * w, color * h, w, h);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
	
	public Icon getChessPieceIcon(PlayerColor color, ChessPieces piece) {
		int colorIndex = 1 - color.ordinal();
		int pieceIndex = piece.ordinal();
		return new ImageIcon(pieceImages[colorIndex][pieceIndex]);
	}
}
