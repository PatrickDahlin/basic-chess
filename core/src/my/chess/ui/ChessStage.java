package my.chess.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import my.chess.logic.ChessConnection;

public class ChessStage implements Screen {

	
	Texture background;
	SpriteBatch batch;
	
	
	public ChessStage(Game g, ChessConnection connection)
	{
		batch = new SpriteBatch();
		background = new Texture("chessboard.png");
	
		
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
	public void show() {
		//Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background, 0, 0);
		
		
		// TODO draw chesspieces here
		
		batch.end();
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
