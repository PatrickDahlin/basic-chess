package my.chess.logic;

public class ChessPiece {

	public enum ChessPieceType {
		Pawn,Bishop,King,Queen,Rook,Knight
	}
	
	ChessPieceType type = ChessPieceType.Pawn;
	int chessBoardX = 0;
	int chessBoardY = 0;
	int playerNr = 0;
	
	public ChessPiece(ChessPieceType t, int x, int y, int playernr)
	{
		chessBoardX = x;
		chessBoardY = y;
		playerNr = playernr;
		type = t;
	}
	
	/**
	 * Get the owner player index
	 */
	public int GetPlayerIndex() { return playerNr; }
	/**
	 * Get the chess piece type
	 */
	public ChessPieceType GetPieceType() { return type; }
	/**
	 * Get the X of the boardposition (0,0) in bottom left
	 */
	public int GetBoardPosX() { return chessBoardX; }
	/**
	 * Get the Y of the boardposition (0,0) in bottom left
	 */
	public int GetBoardPosY() { return chessBoardY; }
	
	
	/**
	 * Set the boardposition of this chesspiece, (0,0) in bottom left
	 * @param newX
	 * @param newY
	 */
	public void SetChessboardPosition(int newX, int newY) { chessBoardX = newX; chessBoardY = newY; }
}