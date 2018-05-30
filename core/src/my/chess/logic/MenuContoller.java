package my.chess.logic;

import java.io.IOException;
import java.util.ArrayList;

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
	/* Server listener for listening for client connections */
	private Listener serverConnectionListener;
	
	private Game game;
	
	private ChessUIController chessui;
	
	private ArrayList<Listener> connectedListeners = new ArrayList<Listener>();
	
	public MenuContoller(Game g)
	{
		game = g;
	}
	
	public boolean HostGame(String port)
	{
		if(waitingForHost) return false;
		
		Server myServ = new Server();
		myServ.start();
		
		myServ.getKryo().register(NetChessMove.class);
		myServ.getKryo().register(String.class);
		
		// TODO Don't allow more than one client connection
		
		int portNum = Integer.parseInt(port);
		try {
			myServ.bind(portNum, portNum+1);
		} catch (IOException e) {
			System.out.println("Failed to start server");
			e.printStackTrace();
			myServ.stop();
			myServ = null;
			return false;
		}
		
		serverConnectionListener = new RunMethodListener<String>(String.class) {
			@Override
			public void run(Connection con, String object) {
				System.out.println("Server received:"+object);
				
				if(object.equals("ClientConnected"))
				{
					for(Listener l : connectedListeners)
					{
						l.connected(con);
					}
				}
			}
		};
		
		myServ.addListener(serverConnectionListener);
		
		connection = new ChessConnection(myServ);
		
		waitingForHost = true;
		
		return true;
	}
	
	private boolean shouldStart = false;
	
	public boolean ConnectToGame(String ip, String port)
	{
		if(waitingForHost) return false;
		
		final Client myClient = new Client();
		myClient.start();
		
		myClient.getKryo().register(NetChessMove.class);
		myClient.getKryo().register(String.class);
		
		int portNum = Integer.parseInt(port);
	    try {
	    	myClient.connect(5000, ip, portNum, portNum+1);
		} catch (IOException e) {
			System.out.println("Failed to connect to server at "+ip+":"+port);
			e.printStackTrace();
			myClient.stop();
			return false;
		}
	    
	    
	    connection = new ChessConnection(myClient);
	    
	    waitingForHost = true;
	    
	    // Send connected message
	    myClient.sendTCP("ClientConnected");
	    
	    //Listen to a string message saying "StartGame", have to use isInstance cast check because of KryoNet
	    clientStartListener = new RunMethodListener<String>(String.class) {
	    	@Override
			public void run(Connection con, String object) {
	    		if(object.equals("StartGame"))
	    		{
	    			myClient.removeListener(clientStartListener);
	    			shouldStart = true;
	    		}
			}
	    };
	    
	    myClient.addListener(clientStartListener);
	    
	    
		return true;
	}
	
	
	public void RemoveClientConnectedListener(Listener l)
	{
		if(connectedListeners.contains(l))
			connectedListeners.remove(l);
	}
	
	public void AddClientConnectedListener(Listener l)
	{
		if(!connectedListeners.contains(l))
			connectedListeners.add(l);
	}

	
	public void Update()
	{
		if(shouldStart)
			StartGame();
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
			connection.GetServer().removeListener(serverConnectionListener);
		}
		
		
		chessui = new ChessUIController(game, connection);

		waitingForHost = false;
	}
	
	/**
	 * Checks if we are hosting and if so, if we have a client connected
	 */
	public boolean isHostAndHasClientConnection()
	{
		return connection != null && connection.isServer() && connection.GetServer().getConnections().length > 0;
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
		return connection != null && connection.isServer();
	}
}
