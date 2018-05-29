package my.chess.api;

public class ChessPiece {

	public enum ChessPieceType {
		Pawn,Bishop,King,Queen,Rook,Knight
	}
	
	ChessPieceType type = ChessPieceType.Pawn;
	int playerOwner = 0;
	
	public ChessPiece(ChessPieceType t, int playernr)
	{
		playerOwner = playernr;
		type = t;
	}
	
	/**
	 * Get the owner player index
	 */
	public int GetPlayerIndex() { return playerOwner; }
	/**
	 * Get the chess piece type
	 */
	public ChessPieceType GetPieceType() { return type; }
	
	/**
	 * Player 1 has white color, player 2 has black
	 */
	public boolean isPieceWhite() { return playerOwner == 1; }
}