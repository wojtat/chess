package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.GuiResourceManager;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

public class ChessBoardView extends GuiSubView {
	private static final Logger LOG = Logger.getLogger(ChessBoardView.class.getName());
	
	public static final int TILE_SIZE = 64;
	private final Color LIGHT_COLOR = Color.WHITE;
	private final Color DARK_COLOR = Color.BLACK;
	private final Color HILIGHT_COLOR = Color.BLUE;
	private AbstractButton[][] boardTiles;
	private ChessCoord selectedTile;
	
	public ChessBoardView() {
		super();
		this.boardTiles = new AbstractButton[ChessBoard.NUM_RANKS][ChessBoard.NUM_FILES];
	}
	
	public void setSelectedTile(ChessCoord coord) {
		selectedTile = coord;
	}
	
	public ChessCoord getSelectedTile() {
		return selectedTile;
	}
	
	private void setTileIcon(AbstractButton tile, ChessCoord coord) {
		if (model.getGameModel().getGame().getBoard().getTileAt(coord).isEmpty()) {
			Icon tileIcon = new ImageIcon(new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB));
			tile.setIcon(tileIcon);
			tile.setDisabledIcon(tileIcon);
		} else {
			PlayerColor color = model.getGameModel().getGame().getBoard().getTileAt(coord).getPiece().getColor();
			ChessPieces pieceKind = model.getGameModel().getGame().getBoard().getTileAt(coord).getPiece().getKind();
			Icon tileIcon = GuiResourceManager.get().getChessPieceIcon(color, pieceKind);
			tile.setIcon(tileIcon);
			tile.setDisabledIcon(tileIcon);
		}
	}
	
	private void initChessboard(JPanel boardPanel) {

		Insets tileInsets = new Insets(0, 0, 0, 0);
		for (int y = ChessBoard.NUM_RANKS; y >= 0; --y) {
			for (int x = 0; x < ChessBoard.NUM_FILES + 1; ++x) {
				if (y == 0) {
					if (x == 0) {
						boardPanel.add(new JLabel(""));
						continue;
					}
					Character fileCharacter = Character.valueOf((char)('A' + x - 1));
					JLabel fileLabel = new JLabel(fileCharacter.toString(), SwingConstants.CENTER);
					boardPanel.add(fileLabel);
				}
				else {
					if (x == 0) {
						JLabel rankLabel = new JLabel(Integer.toString(y), SwingConstants.CENTER);
						boardPanel.add(rankLabel);
						continue;
					}
					ChessCoord coord = new ChessCoord(y, x);
					AbstractButton tile = new JButton();
					
					tile.addMouseListener(new TileButtonListener(tile, coord, model));
					
					tile.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
					tile.setMargin(tileInsets);
					tile.setEnabled(false);
					
					setTileIcon(tile, coord);
					
					if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) {
	                    tile.setBackground(DARK_COLOR);
	                } else {
	                    tile.setBackground(LIGHT_COLOR);
	                }
					boardPanel.add(tile);
					boardTiles[y - 1][x - 1] = tile;
				}
			}
		}
	}
	
	public void updateView() {
		for (int y = ChessBoard.NUM_RANKS; y >= 1; --y) {
			for (int x = 1; x < ChessBoard.NUM_FILES + 1; ++x) {
				ChessCoord coord = new ChessCoord(y, x);
				AbstractButton tile = boardTiles[y-1][x-1];
				setTileIcon(tile, coord);
			}
		}
	}
	
	@Override
	public void initView(MainGuiModel model, MainGuiController controller) {
		super.initView(model, controller);
		
		this.setLayout(new BorderLayout());
		
		// Make the chess board itself
		JPanel boardPanel = new JPanel() {
			@Override
		    public final Dimension getPreferredSize() {
		        Dimension prefSize = super.getPreferredSize();
		        Dimension parentSize = getParent().getSize();
		        if (parentSize.getWidth() > prefSize.getWidth() && parentSize.getHeight() > prefSize.getHeight()) {
		            prefSize = parentSize;
		        }
		        int w = (int)prefSize.getWidth();
		        int h = (int)prefSize.getHeight();
		        // the smaller of the two sizes
		        int s = w > h ? h : w;
		        return new Dimension(s, s);
		    }
		};
		boardPanel.setLayout(new GridLayout(ChessBoard.NUM_RANKS + 1, ChessBoard.NUM_FILES + 1));
		boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		initChessboard(boardPanel);
		this.add(boardPanel, BorderLayout.CENTER);
		
		JPanel side = new JPanel();
		side.setLayout(new BoxLayout(side, BoxLayout.PAGE_AXIS));
		this.add(side, BorderLayout.EAST);
		side.add(new JButton("Next Move"));
		side.add(new JButton("Last Move"));
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
	    	controller.clickTile(coord);
	    	LOG.fine("Mouse clicked on coord " + coord);
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
