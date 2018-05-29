package my.chess.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

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

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        HorizontalGroup group = new HorizontalGroup();

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


        TextButton hostBtn = new TextButton("Host Server", skin, "default");
        hostBtn.setColor(.5f, .5f, .5f, 1);

        Label ipLabel = new Label("IP:PORT ",skin);
        ipLabel.setColor(.5f, .5f, .5f, 1);

        TextField ipField = new TextField("",skin);
        ipField.setColor(.5f, .5f, .5f, 1);

        Label portLabel = new Label("Port: ",skin);
        portLabel.setColor(.5f, .5f, .5f, 1);

        TextField portField = new TextField("",skin);
        portField.setColor(.5f, .5f, .5f, 1);

        TextButton connectBtn = new TextButton("Connect", skin, "default");
        connectBtn.setColor(.5f, .5f, .5f, 1);

        Label textLabel = new Label("",skin);
        textLabel.setColor(.5f, .5f, .5f, 1);

        //Adds to groups
        group.addActor(hostGroup);
        group.addActor(connectGroup);

        hostGroup.addActor(hostGroupSub);
        hostGroup.addActor(hostBtn);

        hostGroupSub.addActor(portLabel);
        hostGroupSub.addActor(portField);

        connectGroup.addActor(connectGroupSub);
        connectGroup.addActor(connectBtn);

        connectGroupSub.addActor(ipLabel);
        connectGroupSub.addActor(ipField);

        group.addActor(textLabel);

        //Adds to stage
        HorizontalGroup tmp = new HorizontalGroup();
        tmp.setFillParent(true);

        tmp.addActor(group);
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
