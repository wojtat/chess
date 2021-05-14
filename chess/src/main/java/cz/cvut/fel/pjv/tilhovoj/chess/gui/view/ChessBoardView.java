package cz.cvut.fel.pjv.tilhovoj.chess.gui.view;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.*;

import cz.cvut.fel.pjv.tilhovoj.chess.game.*;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPiece;
import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.GuiResourceManager;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.MainGuiController;
import cz.cvut.fel.pjv.tilhovoj.chess.gui.model.MainGuiModel;

public class ChessBoardView extends GuiSubView {
	private static final Logger LOG = Logger.getLogger(ChessBoardView.class.getName());
	
	public static final int TILE_SIZE = 64;
	private final Color LIGHT_COLOR = Color.WHITE;
	private final Color DARK_COLOR = Color.BLACK;
	private final Color HILIGHT_COLOR = new Color(172, 172, 128);
	private AbstractButton[][] boardTiles;
	private ChessCoord selectedTile;
	
	private JPanel clockPanel;
	private JLabel whiteTime;
	private JLabel blackTime;
	private Timer clockUpdater;
	
	private boolean isInPromotionDialog;
	private JPanel promotionPanel;
	private AbstractButton[] promotionButtons;
	private ChessMove promotionMove;
	
	public ChessBoardView() {
		super();
		this.boardTiles = new AbstractButton[ChessBoard.NUM_RANKS][ChessBoard.NUM_FILES];
		this.promotionButtons = new AbstractButton[ChessPieces.PROMOTABLE_PIECES.length];
	}
	
	public void showPromoteToDialog(ChessMove move) {
		promotionMove = move;
		isInPromotionDialog = true;
		promotionPanel.setVisible(true);
	}
	
	public void hidePromoteToDialog() {
		promotionMove = null;
		isInPromotionDialog = false;
		promotionPanel.setVisible(false);
	}
	
	public void setSelectedTile(ChessCoord coord) {
		if (selectedTile != null) {
			// Unselect tile
			setTileBackground(boardTiles[selectedTile.getRank()-1][selectedTile.getFile()-1], selectedTile);
		}
		selectedTile = coord;
		// Select tile
		if (selectedTile != null) {
			boardTiles[selectedTile.getRank()-1][selectedTile.getFile()-1].setBackground(HILIGHT_COLOR);
		}
	}
	
	public boolean isInPromotionDialog() {
		return isInPromotionDialog;
	}
	
	public ChessCoord getSelectedTile() {
		return selectedTile;
	}
	
	public void setWhiteTime(Double time) {
		whiteTime.setText(ChessClock.timeToString(time));
	}
	
	public void setBlackTime(Double time) {
		blackTime.setText(ChessClock.timeToString(time));
	}
	
	private void setTileBackground(AbstractButton tile, ChessCoord coord) {
		if ((coord.getRank() % 2 == 1 && coord.getFile() % 2 == 1) || (coord.getRank() % 2 == 0 && coord.getFile() % 2 == 0)) {
            tile.setBackground(DARK_COLOR);
        } else {
            tile.setBackground(LIGHT_COLOR);
        }
	}
	
	private void setTileIcon(AbstractButton tile, ChessCoord coord) {
		if (model.getGameModel().getGame() == null || model.getGameModel().getGame().getBoard().getTileAt(coord).isEmpty()) {
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
					
					tile.addMouseListener(new TileButtonListener(coord));
					
					tile.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
					tile.setMargin(tileInsets);
					tile.setEnabled(false);
					
					setTileIcon(tile, coord);
					setTileBackground(tile, coord);
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
		setWhiteTime(model.getGameModel().getGame().getClock().getTime(PlayerColor.COLOR_WHITE));
		setBlackTime(model.getGameModel().getGame().getClock().getTime(PlayerColor.COLOR_BLACK));
	}
	
	private void initPromotionPanel(JPanel parent) {
		promotionPanel = new JPanel();
		promotionPanel.setLayout(new BoxLayout(promotionPanel, BoxLayout.PAGE_AXIS));
		parent.add(promotionPanel);
		promotionPanel.add(new JLabel("Select a piece to promote to!", JLabel.CENTER));
		for (int i = 0; i < ChessPieces.PROMOTABLE_PIECES.length; ++i) {
			promotionButtons[i] = new JButton();

			promotionButtons[i].setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
			promotionButtons[i].setMargin(new Insets(0, 0, 0, 0));
			promotionButtons[i].setEnabled(false);

			PlayerColor color = PlayerColor.getFirst();
			ChessPieces pieceKind = ChessPieces.PROMOTABLE_PIECES[i];
			Icon tileIcon = GuiResourceManager.get().getChessPieceIcon(color, pieceKind);
			promotionButtons[i].setIcon(tileIcon);
			promotionButtons[i].setDisabledIcon(tileIcon);
			promotionButtons[i].setBackground(HILIGHT_COLOR);
			promotionButtons[i].addMouseListener(new PromotionButtonListener(pieceKind));
			promotionPanel.add(promotionButtons[i]);
		}
		promotionPanel.setVisible(false);
	}
	
	private void initClockView(JPanel parent) {
		Font timeFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		clockPanel = new JPanel();
		clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.PAGE_AXIS));
		parent.add(clockPanel);
		clockPanel.add(new JLabel("White Time", JLabel.CENTER));
		whiteTime = new JLabel(ChessClock.timeToString(0.0));
		whiteTime.setFont(timeFont);
		clockPanel.add(whiteTime);
		clockPanel.add(new JLabel("Black Time", JLabel.CENTER));
		blackTime = new JLabel(ChessClock.timeToString(0.0));
		blackTime.setFont(timeFont);
		clockPanel.add(blackTime);
		clockPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		clockPanel.setVisible(true);
		clockUpdater = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChessGame game = model.getGameModel().getGame();
				if (game != null) {
					setWhiteTime(game.getClock().getTime(PlayerColor.COLOR_WHITE));
					setBlackTime(game.getClock().getTime(PlayerColor.COLOR_BLACK));
				}
			}
		});
		clockUpdater.start();
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
		
		JButton nextMoveButton = new JButton("Next Move");
		nextMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChessBoardView.this.controller.nextMove();
			}
		});
		JButton previousMoveButton = new JButton("Last Move");
		previousMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChessBoardView.this.controller.previousMove();
			}
		});
		
		side.add(nextMoveButton);
		side.add(previousMoveButton);
		
		initClockView(side);		
		initPromotionPanel(side);
	}
	
	private class PromotionButtonListener implements MouseListener {
		
		private ChessPieces kind;
		
		public PromotionButtonListener(ChessPieces kind) {
			this.kind = kind;
		}

	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	LOG.fine("Mouse clicked on " + kind);
	    	controller.clickPromotion(promotionMove, kind);
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
	
	private class TileButtonListener implements MouseListener {
		
		private ChessCoord coord;
		
		public TileButtonListener(ChessCoord coord) {
			this.coord = coord;
		}

	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	LOG.fine("Mouse clicked on coord " + coord);
	    	controller.clickTile(coord);
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
