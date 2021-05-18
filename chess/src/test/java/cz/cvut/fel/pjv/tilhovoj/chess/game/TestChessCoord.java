package cz.cvut.fel.pjv.tilhovoj.chess.game;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Sets up tests for the creation of chess coordinates from string representations
 */
public class TestChessCoord {

	@Test
	public void testMakingCoordFromInvalidString() {
		assertNull(ChessCoord.fromString("ab"));
	}
	
	@Test
	public void testMakingCoordFromLowercaseString() {
		assertEquals(new ChessCoord(5, 1), ChessCoord.fromString("a5"));
	}
	
	@Test
	public void testMakingCoordFromUppercaseString() {
		assertEquals(new ChessCoord(5, 1), ChessCoord.fromString("A5"));
	}
}
