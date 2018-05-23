package my.chess.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/*
 Moment 1
Schackets underliggande datamodell: schackbrädan, pjäser med positioner, m.m.

Moment 2
GUI

Moment 3
Möjlighet att ”spela” schack-spelet, dvs initialisering av spelet, flytta på pjäser

Moment 4
Datakommunikation mellan datorerna, dvs skicka och ta emot data, tolka data och uppdatera spelet utgående från emottagen data

Moment 5
Schackregler, kontroll av lagliga flyttningar, kontroll av vinst/förlust. Kontroll av vems tur som är på gång, dvs den vars tur det inte är skall inte kunna flytta på pjäser. 
*/


public class BasicChess extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("chessboard.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
