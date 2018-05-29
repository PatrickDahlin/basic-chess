package my.chess.ui;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


/**
 * TESTING ONLY, basic chat functionality on local network
 * TODO Delete this, only use as reference
 */

public class NetworkTestingScreen implements Screen {

	
	private Stage stage;
	
	private Skin skin;
	private Label status;
	
	private VerticalGroup rightPane;
	private VerticalGroup chatPane;
	private TextField inputText;
	
	Texture background;
	SpriteBatch batch;
	
	@Override
	public void show() {
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		
		batch = new SpriteBatch();
		background = new Texture("chessboard.png");
		
		Gdx.input.setInputProcessor(stage);

		rightPane = new VerticalGroup();
		rightPane.setWidth(400);
		rightPane.setHeight(400);
		rightPane.align(Align.right);
		
		chatPane = new VerticalGroup();
		chatPane.setFillParent(true);
		chatPane.align(Align.right);
		
		inputText = new TextField("",skin);
		inputText.setWidth(400);
		inputText.setHeight(50);
		
		
		inputText.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode)
			{
				//System.out.println(keycode);
				if(keycode == 66)
				{
					// Enter was pressed, send message
					String text = inputText.getText();
					inputText.setText("");
					if(running)
					{
						if(myClient != null)
						{
							myClient.sendTCP(text);
						}
						else if(myServer != null)
						{
							myServer.sendToAllTCP(text);
						}
					}
				}
				return true;
			}
		});
		
		
		rightPane.addActor(chatPane);
		rightPane.addActor(inputText);
		
		VerticalGroup group = new VerticalGroup();
		group.setWidth(200);
		group.setHeight(200);
		group.align(Align.left);
		
		status = new Label("",skin);
		status.setWidth(200);
		status.setHeight(50);
		
		TextButton testbtn = new TextButton("Host", skin, "default");
		testbtn.setWidth(200);
		testbtn.setHeight(50);
		testbtn.setColor(.5f, .5f, .5f, 1);
		
		testbtn.addListener(new ClickListener() {
			@Override
	         public void clicked(InputEvent event, float x, float y) {
	            Host();
	         }
		});
		
		TextButton testbtn2 = new TextButton("Connect to local", skin, "default");
		testbtn2.setWidth(200);
		testbtn2.setHeight(50);
		testbtn2.setColor(.5f, .5f, .5f, 1);
		
		testbtn2.addListener(new ClickListener() {
			@Override
	         public void clicked(InputEvent event, float x, float y) {
	            Connect();
	         }
		});
		
		group.addActor(status);
		group.addActor(testbtn);
		group.addActor(testbtn2);
		
		HorizontalGroup tmp = new HorizontalGroup();
		tmp.setFillParent(true);
		tmp.addActor(group);
		tmp.addActor(rightPane);
		
		stage.addActor(tmp);
		
		
	}
	
	boolean running = false;
	Server myServer;
	Client myClient;
	
	private void Host()
	{
		if(running) return;
		
		myServer = new Server();
		myServer.start();
		try {
			myServer.bind(54555, 54777);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		myServer.addListener(new Listener() {
		       public void received (Connection connection, Object object) {
		          if (object instanceof String) {
		             String request = (String)object;
		             System.out.println("Server: "+request);
		             
		             Label tmp = new Label(request,skin);
		             chatPane.addActor(tmp);
		          }
		       }
		    });
		
		status.setText("Running as Server");
		
		running = true;
	}
	
	private void Connect()
	{
		if(running) return;
		
		myClient = new Client();
		myClient.start();
	    try {
	    	myClient.connect(5000, "127.0.0.1", 54555, 54777);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    

	    String request = "Here is the request";
	    myClient.sendTCP(request);
		
	    myClient.addListener(new Listener() {
	        public void received (Connection connection, Object object) {
	           if (object instanceof String) {
	              String response = (String)object;
	              System.out.println("Client: "+response);
	              
	              Label tmp = new Label(response,skin);
		          chatPane.addActor(tmp);
	           }
	        }
	     });
	    
	    status.setText("Running as Client");
	    
		running = true;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background,0,0);
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {		
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
