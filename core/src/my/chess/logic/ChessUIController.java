package my.chess.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import my.chess.api.ChessBoard;
import my.chess.api.ChessPiece;
import my.chess.ui.ChessStage;

public class ChessUIController {

	private ChessConnection connection;
	private Game game; // TODO @unused
	private ChessStage chess;
	private ChessBoard chessboard;
	
	private ArrayList<int[]> selectedPieceMoves = null;
	private boolean selectedPiece = false;
	private int selectedX = -1;
	private int selectedY = -1;
	
	private Listener moveListener;
	
	public ChessUIController(Game game, ChessConnection connection)
	{
		this.game = game;
		this.connection = connection;	
		chessboard = new ChessBoard();
		
		System.out.println("You are player "+connection.GetPlayerIndex());
		
		chess = new ChessStage(this);
		
		game.setScreen(chess);
		
		updateTurnText();
		
		moveListener = new RunMethodListener<NetChessMove>(NetChessMove.class) {
			@Override
			public void run(Connection con, NetChessMove object) {
				System.out.println("Received move from other player, executing...");
				boolean success = chessboard.MoveChessPieceTo(object.player, object.fromX, object.fromY, object.toX, object.toY);
				System.out.println("The move was "+(success ? "successful" : "not executed successfully"));
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
		updateTurnText();
	}
	
	private void updateTurnText()
	{
		if(connection.GetPlayerIndex() == chessboard.GetPlayerTurn())
		{
			chess.SetTurnText("Your turn");
		}
		else
			chess.SetTurnText("Other player's turn");
	}
	
	/**
	 * User clicks on the chessboard to make a selection on a piece, or to make a move to a tile
	 * <br> Input is in board indices (0-7 integer values)
	 */
	public void ChessboardClick(int boardX, int boardY)
	{
		if(chessboard.GetPlayerTurn() != connection.GetPlayerIndex()) {
			System.out.println("It's not your turn!");
			return;
		}
		
		// Check that selection is players own chesspiece
		ChessPiece selected = chessboard.GetChessPieceAt(boardX, boardY);
		
		if(selected != null) {
			System.out.println("Selected pawn of type "+selected.GetPieceType().toString());
			if(selected.GetPlayerIndex() == connection.GetPlayerIndex())
			{
				// If we clicked the same selection again, deselect
				if(selectedPiece && selectedX == boardX && selectedY == boardY)
				{
					AbortSelection();
					return;
				}
				
				// Make this the selection
				selectedPiece = true;
				selectedX = boardX;
				selectedY = boardY;
				
				selectedPieceMoves = chessboard.GetLegalMoves(selectedX,selectedY,connection.GetPlayerIndex());
			
			}
			else if(selected.GetPlayerIndex() != connection.GetPlayerIndex())
			{
				// We selected other players chessPiece, this means either
				// that we want to eat the other chesspiece or we tried an invalid move
				if(selectedPiece)
				{
					// we want to eat this piece since we've selected another piece earlier
					doMove(selectedX, selectedY,boardX,boardY);
				}
				AbortSelection();
				//else We selected other players chesspiece which does nothing	
			}
		}
		else
		{
			// We selected an empty slot, we either want to move piece here or do nothing
			if(selectedPiece)
			{
				// We got an earlier piece selected so we want to move here
				doMove(selectedX,selectedY,boardX,boardY);
				AbortSelection();
				
			}
		}
	}
	
	private void doMove(int fromX, int fromY, int toX, int toY)
	{
		chessboard.MoveChessPieceTo(connection.GetPlayerIndex(), fromX, fromY, toX, toY);
		// Send to other player
		if(connection.isServer())
		{
			connection.GetServer().sendToAllTCP(new NetChessMove(connection.GetPlayerIndex(), fromX, fromY, toX, toY));
			System.out.println("Sent move to Client");
		}
		else
		{
			connection.GetClient().sendTCP(new NetChessMove(connection.GetPlayerIndex(), fromX, fromY, toX, toY));
			System.out.println("Sent move to Server");
		}
	}
	
	/**
	 * User cancels selection
	 */
	public void AbortSelection()
	{
		selectedPiece = false;
		selectedX = -1;
		selectedY = -1;
		selectedPieceMoves = null;
	}
	
	private void OnWinCallback()
	{}
	
	private void OnLooseCallback()
	{}
	
	public ArrayList<int[]> GetLegalMovesForSelection() { return selectedPieceMoves; }
	
	public ChessBoard GetChessBoard() { return chessboard; }
	
	public int GetPlayerIndex() { return connection != null ? connection.GetPlayerIndex() : 0; }
	
	public int[] GetSelection() { return selectedPiece ? new int[] {selectedX, selectedY} : null; }
	
}
