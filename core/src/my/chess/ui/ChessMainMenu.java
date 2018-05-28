package my.chess.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class ChessMainMenu implements Screen {

    Texture background;
    SpriteBatch batch;

    public ChessMainMenu(Game g)
    {
        batch = new SpriteBatch();
        background = new Texture("background.jpg");
    }

    private Stage stage;

    private Skin skin;

    private VerticalGroup rightPane;
    private VerticalGroup chatPane;
    private TextField inputText;

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        rightPane = new VerticalGroup();
        rightPane.setWidth(400);
        rightPane.setHeight(400);
        rightPane.align(Align.right);

        VerticalGroup group = new VerticalGroup();
        group.setWidth(200);
        group.setHeight(200);
        group.align(Align.left);

        TextButton hostBtn = new TextButton("Host Server", skin, "default");
        hostBtn.setWidth(200);
        hostBtn.setHeight(50);
        hostBtn.setColor(.5f, .5f, .5f, 1);

        Label ipLabel = new Label("IP: ",skin);
        ipLabel.setWidth(200);
        ipLabel.setHeight(50);
        ipLabel.setColor(.5f, .5f, .5f, 1);

        TextField ipField = new TextField("",skin);
        ipField.setWidth(200);
        ipField.setHeight(50);
        ipField.setColor(.5f, .5f, .5f, 1);

        Label portLabel = new Label("Port:  ",skin);
        portLabel.setWidth(200);
        portLabel.setHeight(50);
        portLabel.setColor(.5f, .5f, .5f, 1);

        TextField portField = new TextField("",skin);
        portField.setWidth(200);
        portField.setHeight(50);
        portField.setColor(.5f, .5f, .5f, 1);

        TextButton connectBtn = new TextButton("Connect to local", skin, "default");
        connectBtn.setWidth(200);
        connectBtn.setHeight(50);
        connectBtn.setColor(.5f, .5f, .5f, 1);

        group.addActor(hostBtn);
        group.addActor(ipLabel);
        group.addActor(ipField);
        group.addActor(portLabel);
        group.addActor(portField);
        group.addActor(connectBtn);

        HorizontalGroup tmp = new HorizontalGroup();
        tmp.setFillParent(true);
        tmp.addActor(group);
        tmp.addActor(rightPane);

        stage.addActor(tmp);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(background, 0, 0);


        // TODO draw chesspieces here
        batch.end();
        stage.draw();
    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();

    }

}
