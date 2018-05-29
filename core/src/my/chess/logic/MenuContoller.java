package my.chess.logic;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


public class MenuContoller {

	
	private boolean waitingForHost = false;
	/* Connection is the local network state of the client/server */
	private ChessConnection connection;
	/* Client listener for StartGame message from server */
	private Listener clientStartListener;
	
	private Game game;
	
	private ChessUIController chessui;
	
	public MenuContoller(Game g)
	{
		game = g;
	}
	
	public boolean HostGame(String port)
	{
		if(waitingForHost) return false;
		
		Server myServ = new Server();
		myServ.start();
		
		// TODO Don't allow more than one client connection
		
		int portNum = Integer.parseInt(port);
		try {
			myServ.bind(portNum, portNum+1);
		} catch (IOException e) {
			System.out.println("Failed to start server");
			e.printStackTrace();
			return false;
		}
		
		connection = new ChessConnection(myServ);
		
		waitingForHost = true;
		
		return true;
	}
	
	public boolean ConnectToGame(String ip, String port)
	{
		if(waitingForHost) return false;
		
		final Client myClient = new Client();
		myClient.start();
		
		int portNum = Integer.parseInt(port);
	    try {
	    	myClient.connect(5000, ip, portNum, portNum+1);
		} catch (IOException e) {
			System.out.println("Failed to connect to server at "+ip+":"+port);
			e.printStackTrace();
			return false;
		}
	    
	    
	    connection = new ChessConnection(myClient);
	    
	    waitingForHost = true;
	    
	    //Listen to a string message saying "StartGame", have to use isInstance cast check because of KryoNet
	    clientStartListener = new RunMethodListener<String>(String.class) {
	    	@Override
			public void run(Connection con, String object) {
	    		if(object.equals("StartGame"))
	    		{
	    			myClient.removeListener(clientStartListener);
	    			StartGame();
	    		}
			}
	    };
	    
	    myClient.addListener(clientStartListener);
	    
	    
		return true;
	}
	
	/**
	 * Starts the chess game when host pressed Start and client received Start message
	 */
	public void StartGame()
	{
		if(connection.isServer())
		{
			// We want to send StartGame to the other client letting it know we've started
			System.out.println("sending start message to clients");
			connection.GetServer().sendToAllTCP("StartGame");
			
		}
		
		chessui = new ChessUIController(game, connection);

		waitingForHost = false;
	}
	
	/**
	 * Are we waiting for the host to start the game? Applies to both client/server
	 */
	public boolean isWaitingForHost()
	{
		return waitingForHost;
	}
	
	/**
	 * Are we currently hosting a server?
	 */
	public boolean isHosting()
	{
		return connection.isServer();
	}
}
