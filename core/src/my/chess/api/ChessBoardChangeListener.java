package my.chess.api;

public interface ChessBoardChangeListener {
	
	void OnChessBoardChange();
	void OnPlayerWin(int playerIndex);
	
}
