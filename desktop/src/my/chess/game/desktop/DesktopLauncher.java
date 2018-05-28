package my.chess.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import my.chess.game.BasicChess;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.width = 1280;
		config.height = 960;
		config.resizable = false;
		config.foregroundFPS = 60;
		config.backgroundFPS = 10;
		config.samples = 4;
		config.title = "Chess";
		config.vSyncEnabled = false;
    
		new LwjglApplication(new BasicChess(), config);
	}
}
