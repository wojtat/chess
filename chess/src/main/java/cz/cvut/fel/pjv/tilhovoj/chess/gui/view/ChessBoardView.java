package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.GuiResourceManager;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

public class ChessBoardView extends GuiSubView {
	public static final int TILE_SIZE = 64;
	private final Color LIGHT_COLOR;
	private final Color DARK_COLOR;
	private AbstractButton[][] boardTiles;
	private ChessCoord selectedTile;
	
	public ChessBoardView() {
		super();
		this.boardTiles = new AbstractButton[ChessBoard.NUM_RANKS][ChessBoard.NUM_FILES];
		this.LIGHT_COLOR = Color.WHITE;
		this.DARK_COLOR = Color.BLACK;
	}
	
	@Override
    public final Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        Dimension prefSize = null;
        Component c = getParent();
        if (c == null) {
            prefSize = new Dimension(
                    (int)d.getWidth(),(int)d.getHeight());
        } else if (c!=null &&
                c.getWidth()>d.getWidth() &&
                c.getHeight()>d.getHeight()) {
            prefSize = c.getSize();
        } else {
            prefSize = d;
        }
        int w = (int) prefSize.getWidth();
        int h = (int) prefSize.getHeight();
        // the smaller of the two sizes
        int s = (w>h ? h : w);
        return new Dimension(s,s);
    }
	
	@Override
	public void initView(MainGuiModel model, MainGuiController controller) {
		super.initView(model, controller);
		
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setLayout(new GridLayout(ChessBoard.NUM_RANKS + 1, ChessBoard.NUM_FILES + 1));
		
		Insets tileInsets = new Insets(0, 0, 0, 0);
		for (int y = ChessBoard.NUM_RANKS; y >= 0; --y) {
			for (int x = 0; x < ChessBoard.NUM_FILES + 1; ++x) {
				if (y == 0) {
					if (x == 0) {
						this.add(new JLabel(""));
						continue;
					}
					Character fileCharacter = Character.valueOf((char)('A' + x - 1));
					JLabel fileLabel = new JLabel(fileCharacter.toString(), SwingConstants.CENTER);
					fileLabel.setSize(fileLabel.getMinimumSize());
					this.add(fileLabel);
				}
				else {
					if (x == 0) {
						JLabel rankLabel = new JLabel(Integer.toString(y), SwingConstants.CENTER);
						rankLabel.setSize(rankLabel.getMinimumSize());
						this.add(rankLabel);
						continue;
					}
					ChessCoord coord = new ChessCoord(y, x);
					AbstractButton tile = new JButton();
					
					tile.addMouseListener(new TileButtonListener(tile, coord, model));
					
					tile.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
					tile.setMargin(tileInsets);
					tile.setEnabled(false);
					
					if (model.getGameModel().getBoard().getTileAt(coord).isEmpty()) {
						Icon tileIcon = new ImageIcon(new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB));
						tile.setIcon(tileIcon);
						tile.setDisabledIcon(tileIcon);
					} else {
						PlayerColor color = model.getGameModel().getBoard().getTileAt(coord).getPiece().getColor();
						ChessPieces pieceKind = model.getGameModel().getBoard().getTileAt(coord).getPiece().getKind();
						Icon tileIcon = GuiResourceManager.get().getChessPieceIcon(color, pieceKind);
						tile.setIcon(tileIcon);
						tile.setDisabledIcon(tileIcon);
					}
					
					if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) {
	                    tile.setBackground(DARK_COLOR);
	                } else {
	                    tile.setBackground(LIGHT_COLOR);
	                }
					this.add(tile);
					boardTiles[y - 1][x - 1] = tile;
				}
			}
		}
	}
	
	private class TileButtonListener implements MouseListener {
		private ChessCoord coord;
		private AbstractButton button;
		private MainGuiModel model;
		
		public TileButtonListener(AbstractButton button, ChessCoord coord, MainGuiModel model) {
			this.button = button;
			this.coord = coord;
			this.model = model;
		}

	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	//((JToggleButton)e.getSource()).setBackground(new Color(3, 59, 90).brighter());
	    	System.out.println("MOUSE CLICKED");
	    }
		
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	    }

		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}