package my.chess.game;

import com.badlogic.gdx.Game;
import my.chess.ui.NetworkTestingScreen;


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


public class BasicChess extends Game {

	
	
	@Override
	public void create () {
		//setScreen(new ChessStage(this));
		setScreen(new NetworkTestingScreen());
		// TODO add menu screen and it's MP connection thingies
	}

	@Override
	public void render () {
		super.render(); // IMPORTANT, other screens won't render without this
	}
	
	@Override
	public void dispose () {
	
	}
}
