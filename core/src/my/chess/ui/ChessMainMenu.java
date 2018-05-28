package my.chess.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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

        TextButton hostBtn = new TextButton("Host Server", skin, "default");
        hostBtn.setWidth(200);
        hostBtn.setHeight(50);
        hostBtn.setColor(.5f, .5f, .5f, 1);
/*
        TextButton ipField = new
        ipField.setWidth(200);
        ipField.setHeight(50);
        ipField.setColor(.5f, .5f, .5f, 1);

        TextButton portField = new TextButton("Connect to local", skin, "default");
        portField.setWidth(200);
        portField.setHeight(50);
        portField.setColor(.5f, .5f, .5f, 1);
*/
        TextButton connectBtn = new TextButton("Connect to local", skin, "default");
        connectBtn.setWidth(200);
        connectBtn.setHeight(50);
        connectBtn.setColor(.5f, .5f, .5f, 1);

        group.addActor(hostBtn);
        //group.addActor(ipField);
        //group.addActor(portField);
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
