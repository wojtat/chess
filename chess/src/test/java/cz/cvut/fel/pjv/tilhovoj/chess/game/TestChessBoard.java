package cz.cvut.fel.pjv.tilhovoj.chess.game;

import org.junit.*;
import static org.junit.Assert.*;

public class TestChessBoard {
	
	@Test
	public void testNumberOfMovesFromInitialPosition() {
		ChessBoard board = ChessBoard.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertEquals(20, board.perft(1));
		assertEquals(400, board.perft(2));
	}
}
