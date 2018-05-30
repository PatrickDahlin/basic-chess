package my.chess.api;

import my.chess.api.ChessPiece.ChessPieceType;

import java.util.ArrayList;
import java.util.Collections;

public class ChessBoard {
	
	// 0,0 is in bottom left and player 1 is bottom white
	ChessPiece[][] board; // 2D array of chesspieces, board[x][y] where x and y are 0 to 7 (8 "slots" in each direction)

	
	boolean isPlaying; // Flag unset if either player won and no more moves can be made until restart
	int nextPlayerTurn = 1; // Player 1 => 1 (White), Player 2 => 2 (Black)

	
	private ArrayList<ChessBoardChangeListener> changeListeners = new ArrayList<ChessBoardChangeListener>();
	
	
	public ChessBoard()
	{
		ResetBoard();
	}
	
	/**
	 * Reset all pawns back to original positions, discards old pawns and board
	 */
	public void ResetBoard()
	{
		board = null;
		board = new ChessPiece[8][];
		for(int i=0; i < 8; i++)
		{
			board[i] = new ChessPiece[8];
			switch(i)
			{
			case 0:
			case 7:
				board[i][0] = new ChessPiece(ChessPieceType.Rook, 1);
				board[i][7] = new ChessPiece(ChessPieceType.Rook, 2);
				break;
			case 1:
			case 6:
				board[i][0] = new ChessPiece(ChessPieceType.Knight, 1);
				board[i][7] = new ChessPiece(ChessPieceType.Knight, 2);
				break;
			case 2:
			case 5:
				board[i][0] = new ChessPiece(ChessPieceType.Bishop, 1);
				board[i][7] = new ChessPiece(ChessPieceType.Bishop, 2);
				break;
			case 3:
				board[i][0] = new ChessPiece(ChessPieceType.Queen, 1);
				board[i][7] = new ChessPiece(ChessPieceType.Queen, 2);
				break;
			case 4:
				board[i][0] = new ChessPiece(ChessPieceType.King, 1);
				board[i][7] = new ChessPiece(ChessPieceType.King, 2);
				break;
			}
			
			
			// Pawns all along y = 1 and y = 6
			board[i][1] = new ChessPiece(ChessPieceType.Pawn, 1);
			board[i][6] = new ChessPiece(ChessPieceType.Pawn, 2);
		}
		
		isPlaying = true;
		
		// Fire changed event
		for(ChessBoardChangeListener c : changeListeners)
			c.OnChessBoardChange();
	}
	
	/**
	 * Get the pawn located at x,y in boardposition (0,0) bottom left
	 * null is returned if position is out of board bounds 0-7
	 */
	public ChessPiece GetChessPieceAt(int x, int y)
	{
		if(!isWithinBoard(x,y))
			return null;
		
		return board[x][y];
	}
	
	/**
	 * Get which player's turn it is, 1 for player 1(white), 2 for player 2(black)
	 */
	public int GetPlayerTurn()
	{
		return nextPlayerTurn;
	}
	
	/**
	 * Move chess piece from (fromX,fromY) to (toX,toY), if target position has pawn, it's eaten
	 * <br><i>NOTE: this is where pawn movement rules are enforced TODO</i>
	 * @return True on successful move, false otherwise
	 */
	public boolean MoveChessPieceTo(int playerMoving, int fromX, int fromY, int toX, int toY)
	{
		// Check bounds for input
		if(!isWithinBoard(fromX,fromY))
			return false;
		
		if(!isWithinBoard(toX,toY))
			return false;
		
		ChessPiece p = board[fromX][fromY];
		board[fromX][fromY] = null;
		
		if(board[toX][toY] != null)
			System.out.println("A pawn was eaten");
		
		board[toX][toY] = p;
		
		// Fire changed event
		for(ChessBoardChangeListener c : changeListeners)
			c.OnChessBoardChange();
		
		return true;
	}
	
	private boolean isWithinBoard(int x, int y)
	{
		if(x < 0 || x > 7 || y < 0 || y > 7) {
			System.out.printf("Tried accessing board position %i,%i which was out of bounds!\n",x,y);
			return false;
		}
		return true;
	}
	
	// TODO Win/Lost action with possibility to add listeners from outside, moves might trigger win/lost scenario
	
	/**
	 * Add listener for changes made to the chessboard in the form of moves
	 */
	public void AddChangeListener(ChessBoardChangeListener l)
	{
		if(!changeListeners.contains(l))
			changeListeners.add(l);
	}
	
	/**
	 * Removes a listened that has been added with AddChangeListener
	 */
	public void RemoveChangeListener(ChessBoardChangeListener l)
	{
		if(changeListeners.contains(l))
			changeListeners.remove(l);
	}

    public ArrayList<int[]> GetLegalMoves(int x, int y, ChessPieceType type, int color){

        ArrayList<int[]> offsets = new ArrayList<int[]>(); //HOW THE PIECE IS ABLE TO MOVE
        int moveLength;

	    switch (type){
            case Pawn:

                return pawnMoves(x, y, color);

            case Bishop:

                moveLength = 7;
                offsets.add(new int[]{1,1});
                offsets.add(new int[]{1,-1});
                offsets.add(new int[]{-1,1});
                offsets.add(new int[]{-1,-1});
                return checkLegal(offsetMove(x, y, offsets, moveLength), color);

            case King:

                moveLength = 1;
                offsets.add(new int[]{1,1});
                offsets.add(new int[]{1,0});
                offsets.add(new int[]{1,-1});
                offsets.add(new int[]{0,1});
                offsets.add(new int[]{0,-1});
                offsets.add(new int[]{-1,1});
                offsets.add(new int[]{-1,0});
                offsets.add(new int[]{-1,-1});
                return checkLegal(offsetMove(x, y, offsets, moveLength), color);

            case Queen:

                moveLength = 7;
                offsets.add(new int[]{1,1});
                offsets.add(new int[]{1,0});
                offsets.add(new int[]{1,-1});
                offsets.add(new int[]{0,1});
                offsets.add(new int[]{0,-1});
                offsets.add(new int[]{-1,1});
                offsets.add(new int[]{-1,0});
                offsets.add(new int[]{-1,-1});
                return checkLegal(offsetMove(x, y, offsets, moveLength), color);

            case Rook:

                moveLength = 7;
                offsets.add(new int[]{1,0});
                offsets.add(new int[]{0,1});
                offsets.add(new int[]{0,-1});
                offsets.add(new int[]{-1,0});
                return checkLegal(offsetMove(x, y, offsets, moveLength), color);

            case Knight:

                ArrayList<int[]> moves = new ArrayList<int[]>();
                moves.add(new int[]{x+2,y+1});
                moves.add(new int[]{x+2,y-1});
                moves.add(new int[]{x-2,y+1});
                moves.add(new int[]{x-2,y-1});
                moves.add(new int[]{x+1,y+2});
                moves.add(new int[]{x-1,y+2});
                moves.add(new int[]{x+1,y-2});
                moves.add(new int[]{x-1,y-2});
                return checkLegal(moves,color);

            default:

                return new ArrayList<int[]>();//I put this here to make missing return statement dissapear ;D

        }

    }

    private ArrayList<int[]> pawnMoves(int x, int y, int color){

	    ArrayList<int[]> pawnMoves = new ArrayList<int[]>();

	    //Assuming that the black side starts on "top" y=7 and the white side starts on bottom y=0
        switch (color){
            case 1:

                //If player is White
                if(isWithinBoard(x,y + 1)) {
                    if (GetChessPieceAt(x, y + 1) != null) {
                        pawnMoves.add(new int[]{x, y + 1});
                        if (y == 1) {
                            if (GetChessPieceAt(x, y + 2) != null) {
                                pawnMoves.add(new int[]{x, y + 2});
                            }
                        }
                    }
                }

                //If enemy piece at x-1 or x+1 and y+1 pawn is able to eat it
                if(isWithinBoard(x + 1, y + 1)) {
                    if (GetChessPieceAt(x + 1, y + 1) == null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x + 1, y + 1);
                        if (tmpPiece.playerOwner != color) {
                            pawnMoves.add(new int[]{x + 1, y + 1});
                        }
                    }
                }

                if(isWithinBoard(x - 1, y + 1)) {
                    if (GetChessPieceAt(x - 1, y + 1) == null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x + 1, y + 1);
                        if (tmpPiece.playerOwner != color) {
                            pawnMoves.add(new int[]{x + 1, y + 1});
                        }
                    }
                }

                break;
            case 2:

                //If player is Black
                if(isWithinBoard(x,y - 1)) {
                    if (GetChessPieceAt(x, y - 1) != null) {
                        pawnMoves.add(new int[]{x, y - 1});
                        if (y == 6) {
                            if (GetChessPieceAt(x, y - 2) != null) {
                                pawnMoves.add(new int[]{x, y - 2});
                            }
                        }
                    }
                }

                //If enemy piece at x-1 or x+1 and y+1 pawn is able to eat it
                if(isWithinBoard(x + 1, y - 1)) {
                    if (GetChessPieceAt(x + 1, y - 1) == null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x + 1, y - 1);
                        if (tmpPiece.playerOwner != color) {
                            pawnMoves.add(new int[]{x + 1, y - 1});
                        }
                    }
                }

                if(isWithinBoard(x - 1, y - 1)) {
                    if (GetChessPieceAt(x - 1, y - 1) == null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x + 1, y - 1);
                        if (tmpPiece.playerOwner != color) {
                            pawnMoves.add(new int[]{x + 1, y - 1});
                        }
                    }
                }

                break;
            default:
                break;

        }

        return pawnMoves;

    }

    private ArrayList<int[]> offsetMove(int x, int y, ArrayList<int[]> offsets, int moveLength){

        ArrayList<int[]> moves = new ArrayList<int[]>();

	    for(int i = 0; i != offsets.size(); i++){
            for(int k = 1; k > moveLength; k++){
                int newX = x;
                int newY = y;

                if(offsets.get(i)[0] == 1){
                    newX = x + k;
                }else if(offsets.get(i)[0] == -1) {
                    newX = x - k;
                }

                if(offsets.get(i)[1] == 1){
                    newY = y + k;
                }else if(offsets.get(i)[1] == -1) {
                    newY = y - k;
                }

                if(newX != x && newY != y){ //Doesn't add new move if it didn't change anything
                    moves.add(new int[]{newX,newY});
                }

                if(GetChessPieceAt(moves.get(moves.size()-1)[0],moves.get(moves.size()-1)[1]) == null){ // Stops current offset loop (inner loop) if it finds a chess piece on newest move
                    break;
                }

            }
        }

        return moves;

    }

    private ArrayList<int[]> checkLegal(ArrayList<int[]> moves, int color){

	    ArrayList<Integer> nonLegalIndex = new ArrayList<Integer>();

	    for(int i = 0; i != moves.size(); i++){

            int x,y;
            x = moves.get(i)[0];
            y = moves.get(i)[1];

            if(!isWithinBoard(x,y)){ //Checks if its within board
                nonLegalIndex.add(i);
                continue;
            }

            ChessPiece targetedPiece = GetChessPieceAt(x,y);

            if(targetedPiece.playerOwner == color){
                nonLegalIndex.add(i);
                continue;
            }

        }

        //Removes illegal moves from move array
        Collections.sort(nonLegalIndex, Collections.<Integer>reverseOrder()); //Sorts Illegal index list so the arraylist removes the highest indexes first

        for(int i = 0; i != nonLegalIndex.size(); i++){
            moves.remove(nonLegalIndex.get(i));
        }

        return moves;

    }

}
