package my.chess.logic;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

public class ChessConnection {
	
	private Server myServer;
	private Client myClient;
	private boolean isServer = false;
	private int playerIndex;
	
	public ChessConnection(Server s)
	{
		isServer = true;
		myServer = s;
		myClient = null;
		playerIndex = 1;
	}
	
	public ChessConnection(Client c)
	{
		isServer = false;
		myServer = null;
		myClient = c;
		playerIndex = 2;
	}
	
	public Server GetServer() { return myServer; }
	public Client GetClient() { return myClient; }
	public boolean isServer() { return isServer; }
	
}
