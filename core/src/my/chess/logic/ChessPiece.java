package my.chess.logic;

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
	
}