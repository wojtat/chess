package cz.cvut.fel.pjv.tilhovoj.chess.game;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Sets up tests for the chess board class. Tests whether the number of legal moves from a given position is accurate
 */
public class TestChessBoard {
	
	@Test
	public void testNumberOfMovesFromInitialPosition() {
		ChessBoard board = ChessBoard.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertEquals(20, board.perft(1));
		assertEquals(400, board.perft(2));
	}
	
	@Test 
	public void testNumberOfMovesFromKiwipetePosition() {
		ChessBoard board = ChessBoard.fromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
		assertEquals(48, board.perft(1));
		assertEquals(2039, board.perft(2));
	}
	
	@Test 
	public void testNumberOfMovesFromAnotherPosition() {
		ChessBoard board = ChessBoard.fromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
		assertEquals(14, board.perft(1));
		assertEquals(191, board.perft(2));
	}
}
