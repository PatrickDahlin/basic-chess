package my.chess.api;

import my.chess.api.ChessPiece.ChessPieceType;

import java.util.ArrayList;

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
		
		if(playerMoving != nextPlayerTurn)
			return false;
		
		if(isKingCheckMate(1) || isKingCheckMate(2))
			return false;
		

		//Checks if move is legal, if its not, disallow it
        ArrayList<int[]> legalMoves = GetLegalMoves(fromX,fromY);
        boolean legalMove = false;
        for(int i = 0; i != legalMoves.size(); i++){
            if(legalMoves.get(i)[0] == toX && legalMoves.get(i)[1] == toY){
                legalMove = true;
            }
        }

        if(!legalMove){
            return false;
        }

		ChessPiece p = board[fromX][fromY];
		board[fromX][fromY] = null;
		
		if(board[toX][toY] != null)
			System.out.println("A pawn was eaten");
		
		board[toX][toY] = p;
		
		// Fire changed event
		for(ChessBoardChangeListener c : changeListeners)
			c.OnChessBoardChange();
		
		// Checking if either player is check-mate
		// We do checked test first since it's faster and a checkmate only when needed
		
		if(isKingChecked(1))
			if(isKingCheckMate(1))
				for(ChessBoardChangeListener c : changeListeners)
					c.OnPlayerWin(2);
		
		if(isKingChecked(2))
			if(isKingCheckMate(2))
				for(ChessBoardChangeListener c : changeListeners)
					c.OnPlayerWin(1);
		
		if(nextPlayerTurn == 1)
			nextPlayerTurn = 2;
		else
			nextPlayerTurn = 1;
		
		System.out.println("Next player move is player "+nextPlayerTurn);
		return true;
	}
	
	private boolean isWithinBoard(int x, int y)
	{
		if(x < 0 || x > 7 || y < 0 || y > 7) {
			//System.out.println("Tried accessing board position "+x+","+y+" which was out of bounds!");
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

    public ArrayList<int[]> GetLegalMoves(int x, int y){

        ArrayList<int[]> offsets = new ArrayList<int[]>(); //HOW THE PIECE IS ABLE TO MOVE
        int moveLength;
        
        ChessPieceType type = GetChessPieceAt(x,y).GetPieceType(); // type can be gotten here, doesn't need to be given in parameters
        int playerIndex = GetChessPieceAt(x,y).GetPlayerIndex();

	    switch (type){
            case Pawn:

                return pawnMoves(x, y, playerIndex);

            case Bishop:

                moveLength = 7;
                offsets.add(new int[]{1,1});
                offsets.add(new int[]{1,-1});
                offsets.add(new int[]{-1,1});
                offsets.add(new int[]{-1,-1});
                return checkLegal(offsetMove(x, y, offsets, moveLength), playerIndex);

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
                return checkLegal(offsetMove(x, y, offsets, moveLength), playerIndex);

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
                return checkLegal(offsetMove(x, y, offsets, moveLength), playerIndex);

            case Rook:

                moveLength = 7;
                offsets.add(new int[]{1,0});
                offsets.add(new int[]{0,1});
                offsets.add(new int[]{0,-1});
                offsets.add(new int[]{-1,0});
                return checkLegal(offsetMove(x, y, offsets, moveLength), playerIndex);

            case Knight:

            	offsets.add(new int[]{x+2,y+1});
            	offsets.add(new int[]{x+2,y-1});
            	offsets.add(new int[]{x-2,y+1});
            	offsets.add(new int[]{x-2,y-1});
            	offsets.add(new int[]{x+1,y+2});
            	offsets.add(new int[]{x-1,y+2});
            	offsets.add(new int[]{x+1,y-2});
            	offsets.add(new int[]{x-1,y-2});
                return checkLegal(offsets,playerIndex);

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
                    if (GetChessPieceAt(x, y + 1) == null) {
                        pawnMoves.add(new int[]{x, y + 1});
                        if (y == 1) {
                            if (GetChessPieceAt(x, y + 2) == null) {
                                pawnMoves.add(new int[]{x, y + 2});
                            }
                        }
                    }
                }

                //If enemy piece at x-1 or x+1 and y+1 pawn is able to eat it
                if(isWithinBoard(x + 1, y + 1)) {
                    if (GetChessPieceAt(x + 1, y + 1) != null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x + 1, y + 1);
                        if (tmpPiece.GetPlayerIndex() != color) {
                            pawnMoves.add(new int[]{x + 1, y + 1});
                        }
                    }
                }

                if(isWithinBoard(x - 1, y + 1)) {
                    if (GetChessPieceAt(x - 1, y + 1) != null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x - 1, y + 1);
                        if (tmpPiece.GetPlayerIndex() != color) {
                            pawnMoves.add(new int[]{x - 1, y + 1});
                        }
                    }
                }

                break;
            case 2:

                //If player is Black
                if(isWithinBoard(x,y - 1)) {
                    if (GetChessPieceAt(x, y - 1) == null) {
                        pawnMoves.add(new int[]{x, y - 1});
                        if (y == 6) {
                            if (GetChessPieceAt(x, y - 2) == null) {
                                pawnMoves.add(new int[]{x, y - 2});
                            }
                        }
                    }
                }

                //If enemy piece at x-1 or x+1 and y+1 pawn is able to eat it
                if(isWithinBoard(x + 1, y - 1)) {
                    if (GetChessPieceAt(x + 1, y - 1) != null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x + 1, y - 1);
                        if (tmpPiece.GetPlayerIndex() != color) { // TODO tmpPiece is always null
                            pawnMoves.add(new int[]{x + 1, y - 1});
                        }
                    }
                }

                if(isWithinBoard(x - 1, y - 1)) {
                    if (GetChessPieceAt(x - 1, y - 1) != null) {
                        ChessPiece tmpPiece = GetChessPieceAt(x - 1, y - 1);
                        if (tmpPiece.GetPlayerIndex() != color) {
                            pawnMoves.add(new int[]{x - 1, y - 1});
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

	    for(int i = 0; i != offsets.size(); i++) {
	    	
	    	int startingIndexForThisline = moves.size(); // Remember the starting index for each line, we'll need it
	    	
            for(int k = 1; k <= moveLength; k++) {
                int newX = x;
                int newY = y;

                newX = x + k * offsets.get(i)[0];
                newY = y + k * offsets.get(i)[1];
                
                if(newX == x && newY == y) continue; //Don't add move where the current piece is at, 0 length move is invalid

            	int[] pos = new int[] {newX,newY};
                	
            	// If we found an empty slot, add it, otherwise stop testing along this offset
            	if(GetChessPieceAt(pos[0],pos[1]) == null)
            		moves.add(pos);
            	else
            	{
            		
            		if(moves.size() == startingIndexForThisline)
            		{
            			// Edge case where we hit a piece on our first step, we don't want to check for last move in this case
            			moves.add(pos);
            			break;
            		}
            		else
            		{
	            		// check if this is the first piece we hit
	            		int lastX = moves.get(moves.size()-1)[0];
	            		int lastY = moves.get(moves.size()-1)[1];
	            		
	            		// If last move was on an empty slot, this means we can eat this piece
	            		if(GetChessPieceAt(lastX,lastY) == null)
	            			moves.add(pos);
	            		
	            		// And lastly we don't want to continue along this line anymore since we hit a piece
	           			break;
            		}
            	}
                
            }
        }

        return moves;

    }

    /**
     * Checks that all given moves are within the board and that they don't overlap with your own chesspieces (invalid moves)
     */
    private ArrayList<int[]> checkLegal(ArrayList<int[]> moves, int playerindex){

	    // While traversing backwards it's ok to remove items, it'll only change the items that you've already visited
	    for(int i = moves.size()-1; i >= 0; i--){
	    	
	    	if(moves.get(i).length < 2)
	    	{
	    		moves.remove(i);
	    		continue;
	    	}
	    	
            int x,y;
            x = moves.get(i)[0];
            y = moves.get(i)[1];

            if(!isWithinBoard(x,y)){ //Checks if its within board
                moves.remove(i);
            	continue;
            }

            ChessPiece targetedPiece = GetChessPieceAt(x,y);

            // Prevent "eating" of own pieces
            if(targetedPiece != null && targetedPiece.GetPlayerIndex() == playerindex){
                moves.remove(i);
            	continue;
            }

        }

        return moves;

    }

    
    /**
     * Looks for the king of given player and returns it's boardposition
     */
    private int[] findPlayerKing(int playerIndex)
    {
    	for(int i=0; i < 8; i++)
    	{
    		for(int j=0; j < 8; j++)
    		{
    			ChessPiece tmp = GetChessPieceAt(i,j);
    			if(tmp != null && tmp.GetPieceType() == ChessPieceType.King && tmp.GetPlayerIndex() == playerIndex)
    				return new int[] {i,j};
    		}
    	}
    	return null;
    }
    
    /**
     * Is this player's king in check?
     */
    public boolean isKingChecked(int playerIndex)
    {
    	
    	// Find king for this player
    	int[] kingPos = findPlayerKing(playerIndex);
    	
    	if(kingPos == null) return true;
    	
    	int otherPlayerIndex = playerIndex == 1 ? 2 : 1;
    	
    	// is the king threathened? ie. can the other player eat the king in his/her next move
    	
    	// First acuire all moves possible for the other player
    	ArrayList<int[]> moves = getPlayerMovesAll(otherPlayerIndex, false);
    	
    	// Check if any of the moves result in eating the king
    	boolean checked = false;
    	for(int i=0; i < moves.size(); i++)
    	{
    		if(moves.get(i)[0] == kingPos[0] && moves.get(i)[1] == kingPos[1])
    		{
    			checked = true;
    			break;
    		}
    	}
    	
    	if(checked)
    		System.out.println("Player "+playerIndex+" has his king checked");
    	
    	return checked;
    }
    
    /**
     * Checks if king is checked and if there are any moves to remove the threat, if not return true (check-mate)
     */
    public boolean isKingCheckMate(int playerIndex)
    {
    	int[] kingPos = findPlayerKing(playerIndex);
    	
    	if(kingPos == null) 
    	{
    		System.out.println("Player "+playerIndex+" has no king");
    		return true; // king has been eaten
    	}
    	
    	if( isKingChecked(playerIndex) &&
    			!canThreatBeRemovedNextMoveByEatingIt(kingPos[0], kingPos[1]) &&
    			!canThreatBeRemovedByBlockingIt(kingPos[0],kingPos[1]) &&
    			!canThreatBeRemovedByMovingPiece(kingPos[0], kingPos[1]) )
    	{
    		System.out.println("Player "+playerIndex+" is in checkmate");
    		return true;
    	}
    	else
    		return false;
    }
    
    private ArrayList<int[]> getPlayerMovesAll(int playerIndex, boolean excludeKing)
    {
    	ArrayList<int[]> moves = new ArrayList<int[]>();
    	for(int x=0; x < 8; x++)
    	{
    		for(int y=0; y < 8; y++)
    		{
    			ChessPiece tmp = GetChessPieceAt(x, y);
    			if(tmp != null && tmp.GetPlayerIndex() == playerIndex && (excludeKing && tmp.GetPieceType() != ChessPieceType.King || !excludeKing) )
    			{
    				// Get moves for this chesspiece
    				ArrayList<int[]> tmpMoves = GetLegalMoves(x,y);
    				// Add them to the total moves, we don't care if we get duplicates
    				moves.addAll(tmpMoves);
    			}
    		}
    	}
    	return moves;
    }
    
    private ArrayList<int[]> getThreateningPieces(int x, int y)
    {
    	ChessPiece pieceThreathened = GetChessPieceAt(x,y);
    	if(pieceThreathened == null) return null;

    	ArrayList<int[]> threateningPieces = new ArrayList<int[]>();
    	
    	// Look for threatening chesspieces
    	for(int i=0; i < 8; i++)
    	{
    		for(int j=0; j < 8; j++)
    		{
    			// Find the chesspieces that can eat our piece
        		ChessPiece enemy = GetChessPieceAt(i,j);
        		
        		if(enemy == null || enemy.GetPlayerIndex() == pieceThreathened.GetPlayerIndex()) continue;
        	
        		ArrayList<int[]> movesEnemyCanDo = GetLegalMoves(i,j);
        		
        		// See if this piece is a threat to our piece
        		for(int[] move : movesEnemyCanDo)
        		{
        			if(move[0] == x && move[1] == y)
        			{	
        				threateningPieces.add(new int[] {i,j});
        				break;
        			}
        		}
    		}
    	}
    	
    	return threateningPieces;
    }
    
    /**
     * Finds all threats to this piece (returns false if no piece found on pos)
     */
    private boolean canThreatBeRemovedNextMoveByEatingIt(int x, int y)
    {
    	ChessPiece pieceThreathened = GetChessPieceAt(x,y);

    	if(pieceThreathened == null) return false; // uuuh, you need to specify a slot where a piece is at bro
    	
    	
    	ArrayList<int[]> threats = getThreateningPieces(x, y);
    	
    	if(threats.size() > 1) return false; // We can't eat 2 pieces in one turn
    	if(threats.size() <= 0) return true; // um, we got no threats
    	
		// We found one threatening piece and got it's position, now we check if our moves can eat it
		ArrayList<int[]> moves = getPlayerMovesAll(pieceThreathened.GetPlayerIndex(), false);
	
		boolean canEat = false;
		for(int[] move : moves)
		{
			if(move[0] == threats.get(0)[0] && move[1] == threats.get(0)[1])
			{
				// We can eat the threat
				canEat = true;
				break;
			}
		}
		if(canEat)
			System.out.println("Threat can be eaten");
		
		return canEat;
    	
    }

    /**
     * Finds all threats to this ChessPiece (returns false if no piece found on pos) 
     * and checks all moves the owning player can do to block the threats
     * @return Wether owning player can do a move to block the threats to the piece on this position
     */
    private boolean canThreatBeRemovedByBlockingIt(int x, int y)
    {
    	ChessPiece pieceThreathened = GetChessPieceAt(x,y);

    	if(pieceThreathened == null) return false; // uuuh, you need to specify a slot where a piece is at bro
    	
    	
    	
    	
    	ArrayList<int[]> threateningPieces = getThreateningPieces(x,y);
    	
    	ArrayList<int[]> possiblyBlockingMoves = getPlayerMovesAll(pieceThreathened.GetPlayerIndex(), true);
    	
    	// Check each move we can do to see if it blocks all threatening piece moves to this piece
    	// Note knights can't be blocked
    	for(int[] testBlockingMove : possiblyBlockingMoves)
    	{
    		int moveBlocksNumPieces = 0;
    		
    		for(int[] threateningPiece : threateningPieces)
    		{
    			ChessPiece p = GetChessPieceAt(threateningPiece[0],threateningPiece[1]);
    			if(p == null || p.GetPieceType() == ChessPieceType.Knight) return false; // One of the threats is a knight, it can't be blocked
    			
    			// See if our test blocking move interuppts the line between it and this piece
    			
    			// Take the direction from the threatening piece to our piece
    			int dirX = x - threateningPiece[0];
    			if(dirX < 0) 
    				dirX = -1;
    			else if(dirX > 0)
    				dirX = 1;
    			
    			int dirY = y - threateningPiece[1];
    			if(dirY < 0) 
    				dirY = -1;
    			else if(dirY > 0)
    				dirY = 1;
    			
    			// dirX and dirY is the offset(direction) we need to go to find our piece from the threatening piece
    			
    			// Step through the line and see if our move would block the line
    			for(int i=1; i < 9; i++)
    			{
    				int tmpX = threateningPiece[0] + dirX * i;
    				int tmpY = threateningPiece[1] + dirY * i;
    				
    				if(!isWithinBoard(tmpX, tmpY)) continue; // Oops, we went off the board
    				
    				if(tmpX == x && tmpY == y) break; // We reached our piece, aka we couldn't block it
    				
    				if(testBlockingMove[0] == tmpX && testBlockingMove[1] == tmpY)
    				{	
    					moveBlocksNumPieces++; // We can block this move
    					break;
    				}
    			}
    		}
    		
    		// If we can block all threats, we're good
    		if(moveBlocksNumPieces == threateningPieces.size())
    		{	
    			System.out.println("We can block the threat");
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Tests all moves possible by given piece and checks if their resulting positions are threatened or not
     * <br>If such a move exists such that the piece isn't threatened, we'll return true
     * @return True if piece can be moved to a place where it isn't threatened, false otherwise
     */
    private boolean canThreatBeRemovedByMovingPiece(int x, int y)
    {
    	ChessPiece p = GetChessPieceAt(x,y);
    	if(p == null) return true; // dude why u doing this
    	
    	// We're assuming this position is threatened, so we need to move the piece to remove threat
    	
    	int otherPlayerIndex = p.GetPlayerIndex() == 1 ? 2 : 1;
    	
    	ArrayList<int[]> moves = GetLegalMoves(x, y);
    	
    	ArrayList<int[]> threatenedPositions = getPlayerMovesAll(otherPlayerIndex, false);
    	
    	for(int[] move : moves)
    	{
    		boolean threatenedMove = false;
    		for(int[] threatenedPos : threatenedPositions)
    		{
    			if(move[0] == threatenedPos[0] && move[1] == threatenedPos[1])
    				threatenedMove = true;
    		}
    		if(!threatenedMove) // We found a move that removes the threat from the other player
    		{
    			System.out.println("We can move away from the threat");
    			return true;
    		}
    	}
    	
    	return false;
    }

}













