package game.library.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.GameApplicationListener;

/**
 * Main class initializer
 * 
 * @author Albert Beaupre
 */
public class DesktopLauncher {
	/**
	 * Initializes the desktop application
	 */
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280; // 1280 pixels width for the application window
		config.height = 720; // 720 pixels height for the application window
		config.resizable = true;
		config.title = " LibGDX Game Library";
		new LwjglApplication(new GameApplicationListener(), config);
	}
}
