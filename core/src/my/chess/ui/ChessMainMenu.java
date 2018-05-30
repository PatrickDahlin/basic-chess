package my.chess.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import my.chess.logic.MenuContoller;

public class ChessMainMenu implements Screen {

    Texture background;
    SpriteBatch batch;
    private MenuContoller controller;

    public ChessMainMenu(MenuContoller ctrl)
    {
        controller = ctrl;
        batch = new SpriteBatch();
        background = new Texture("background.jpg");
    }

    private Stage stage;

    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    private TextField portField = new TextField("",skin);
    private TextField ipField = new TextField("",skin);
    private Label textLabel = new Label("",skin);
    private TextButton startGameBtn = new TextButton("Start Game!", skin, "default");

    private boolean startGame = false;

    @Override
    public void show() {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        HorizontalGroup HostConnGroup = new HorizontalGroup();

        VerticalGroup GroupGroup = new VerticalGroup();

        VerticalGroup hostGroup = new VerticalGroup();
        hostGroup.padLeft(20);

        HorizontalGroup hostGroupSub = new HorizontalGroup();
        hostGroupSub.padLeft(22);
        hostGroupSub.padBottom(5);

        VerticalGroup connectGroup = new VerticalGroup();
        connectGroup.padLeft(20);
        connectGroup.padRight(20);

        HorizontalGroup connectGroupSub = new HorizontalGroup();
        connectGroupSub.padLeft(22);
        connectGroupSub.padBottom(5);


        final TextButton hostBtn = new TextButton("Host Server", skin, "default");
        hostBtn.setColor(.5f, .5f, .5f, 1);

        hostBtn.addListener( new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                if(portField.getText().trim().equals("")){
                    portField.setColor(2, .5f, .5f, 1);
                    textLabel.setText("Field is Empty!");
                } else {
                    portField.setColor(.5f, .5f, .5f, 1);
                    
                    textLabel.setText("Starting server...");
                    boolean success = controller.HostGame(portField.getText());
                    
                    if(success)
                    {	
                    	textLabel.setText("Waiting for player to join...");
                    	controller.AddClientConnectedListener(new Listener() {
                    		@Override
							public void connected(Connection arg0) {
                    			textLabel.setText("Client connected!");
                    			System.out.println("Client connected!");
                    		}
                    	});
                    } 
                    else
                    	textLabel.setText("Hosting failed! Try again");
                    
                    startGameBtn.setVisible(true);
                }
            }
        });

        Label ipLabel = new Label("IP:PORT ",skin);
        ipLabel.setColor(.5f, .5f, .5f, 1);

        ipField.setColor(.5f, .5f, .5f, 1);

        Label portLabel = new Label("Port: ",skin);
        portLabel.setColor(.5f, .5f, .5f, 1);

        portField.setColor(.5f, .5f, .5f, 1);

        TextButton connectBtn = new TextButton("Connect", skin, "default");
        connectBtn.setColor(.5f, .5f, .5f, 1);

        connectBtn.addListener( new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                String cleaned = ipField.getText().trim();
                
            	if(cleaned.equals("")){
                    ipField.setColor(2, .5f, .5f, 1);
                    textLabel.setText("IP is Empty!");
                } else if(cleaned.split(":").length < 2) {
                    ipField.setColor(2, .5f, .5f, 1);
                	textLabel.setText("You need to specify a port to connect to!");
                } else {
                    ipField.setColor(.5f, .5f, .5f, 1);
                    textLabel.setText("Connecting...");
                    String[] ip = cleaned.split(":");
                    boolean success = controller.ConnectToGame(ip[0],ip[1]);
                    if(success)
                    	textLabel.setText("Connected! Waiting for host...");
                    else
                    	textLabel.setText("Failed to connect!");
                }
            }
        });

        startGameBtn.setColor(.5f, .5f, .5f, 1);

        startGameBtn.setVisible(false);
        startGameBtn.addListener( new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
            	startGame = true;
            	if(controller.isHostAndHasClientConnection())
            		startGame = true;
            	else
            		textLabel.setText("No client is connected!");
            }
        });

        textLabel.setColor(.5f, .5f, .5f, 1);

        //Adds to groups
        HostConnGroup.addActor(hostGroup);
        HostConnGroup.addActor(connectGroup);

        hostGroup.addActor(hostGroupSub);
        hostGroup.addActor(hostBtn);

        hostGroupSub.addActor(portLabel);
        hostGroupSub.addActor(portField);

        connectGroup.addActor(connectGroupSub);
        connectGroup.addActor(connectBtn);

        connectGroupSub.addActor(ipLabel);
        connectGroupSub.addActor(ipField);

        GroupGroup.addActor(HostConnGroup);
        GroupGroup.addActor(textLabel);
        GroupGroup.addActor(startGameBtn);

        //Adds to stage
        HorizontalGroup tmp = new HorizontalGroup();
        tmp.setFillParent(true);

        tmp.addActor(GroupGroup);
        stage.addActor(tmp);
    }

    @Override public void resize(int width, int height) { }

    @Override public void pause() { }

    @Override public void resume() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(background, 0, 0);


        // TODO draw chesspieces here
        batch.end();
        stage.draw();
        if(controller.isHosting() && startGame == true){
            controller.StartGame();
        }
        controller.Update();
    }

    @Override public void hide() { }


    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();

    }

}
