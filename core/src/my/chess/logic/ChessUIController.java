package my.chess.logic;

import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import my.chess.api.ChessBoard;
import my.chess.api.ChessPiece;
import my.chess.ui.ChessStage;

public class ChessUIController {

	private ChessConnection connection;
	private Game game;
	private ChessStage chess;
	private ChessBoard chessboard;
	
	private Listener moveListener;
	
	public ChessUIController(Game game, ChessConnection connection)
	{
		this.game = game;
		this.connection = connection;	
		chess = new ChessStage(this);
		chessboard = new ChessBoard();
		game.setScreen(chess);
		
		moveListener = new RunMethodListener<NetChessMove>(NetChessMove.class) {
			@Override
			public void run(Connection con, NetChessMove object) {
				System.out.println("Received move from other player, executing...");
				chessboard.MoveChessPieceTo(object.player, object.fromX, object.fromY, object.toX, object.toY);
			}
		};
		
		// Listen to moves send by other client/server
		if(connection.isServer())
		{
			connection.GetServer().addListener(moveListener);
		}
		else
		{
			connection.GetClient().addListener(moveListener);
		}
		
		chessboard.ResetBoard();
		
		Update();
	}
	
	public void Update()
	{
		if(chessboard.GetPlayerTurn() == connection.GetPlayerIndex())
		{
			// TODO Text in UI should say Your Turn
			// TODO Allow moving of chesspieces
		}
		else
		{
			// TODO Text in UI should say Waiting for other player
			// TODO Don't allow moving of chesspieces
		}
	}
	
	/**
	 * User clicks on the chessboard to make a selection on a piece, or to make a move to a tile
	 */
	public void ChessboardClick(int screen_x, int screen_y)
	{
		if(chessboard.GetPlayerTurn() != connection.GetPlayerIndex()) return;
		
		float padding = 0;
		float size = 0;
		float boardOriginX = 0;
		float boardOriginY = 0;
		
		int pos_x = Math.round(( (screen_x - padding - boardOriginX) / size ) * 7.0f);
		int pos_y = Math.round(( (screen_y - padding - boardOriginY) / size ) * 7.0f);
		
		if(pos_x < 0 || pos_y < 0 || pos_x > 7 || pos_y > 7)
			System.out.println("Chessboard position out of range 0-7!!!");
		
		// pos_x and pos_y should be in 0-7 range
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
	
	public ChessBoard GetChessBoard() { return chessboard; }
	
}
