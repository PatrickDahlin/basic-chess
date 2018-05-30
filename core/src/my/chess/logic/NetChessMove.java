package my.chess.logic;

/**
 * Data object used to send a move over the network
 */
public class NetChessMove {
	public int player;
	public int fromX;
	public int fromY;
	public int toX;
	public int toY;

	public NetChessMove()
	{
		player = -1;
		fromX = -1;
		fromY = -1;
		toX = -1;
		toY = -1;
	}
	
	public NetChessMove(int player, int fromX, int fromY, int toX, int toY)
	{
		this.player = player;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
}
