package cz.cvut.fel.pjv.tilhovoj.chess.game;

import java.io.Serializable;

import cz.cvut.fel.pjv.tilhovoj.chess.game.pieces.ChessPieces;

public class ChessMove implements Serializable {
	private static final long serialVersionUID = 6750787645427082709L;
	
	private ChessCoord from;
	private ChessCoord to;
	private ChessPieces promoteToKind;
	
	public ChessMove(ChessCoord from, ChessCoord to, ChessPieces promoteToKind) {
		this.from = from;
		this.to = to;
		this.promoteToKind = promoteToKind;
	}
	
	public ChessMove(ChessCoord from, ChessCoord to) {
		this(from, to, null);
	}

	public ChessCoord getFrom() {
		return from;
	}

	public ChessCoord getTo() {
		return to;
	}
	
	public ChessPieces getPromoteToKind() {
		return promoteToKind;
	}
}
