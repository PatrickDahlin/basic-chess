package my.chess.logic;

import com.badlogic.gdx.Game;

import my.chess.api.ChessBoard;
import my.chess.api.ChessPiece;
import my.chess.ui.ChessStage;

public class ChessUIController {

	private ChessConnection connection;
	private Game game;
	private ChessStage chess;
	private ChessBoard chessboard;
	
	public ChessUIController(Game game, ChessConnection connection)
	{
		this.game = game;
		this.connection = connection;	
		chess = new ChessStage(this);
		game.setScreen(chess);
		
		chessboard.ResetBoard();
	}
	
	/**
	 * User clicks on the chessboard to make a selection on a piece, or to make a move to a tile
	 */
	public void ChessboardClick(int x, int y)
	{


	}
	
	/**
	 * User cancels selection
	 */
	public void AbortSelection()
	{
		
	}
	
	private void OnWinCallback()
	{
		
	}
	
	private void OnLooseCallback()
	{
		
	}
	
}
