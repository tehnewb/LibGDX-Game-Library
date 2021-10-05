package game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

import game.screen.ApplicationScreen;
import library.screen.GameScreen;

/**
 * This class is the {@code ApplicationListener} for the application. Handles
 * all {@link GameScreen} screens.
 * 
 * @author Albert Beaupre
 * @see library.screen.GameScreen
 */
public class GameApplication implements ApplicationListener {

	private GameScreen currentScreen;
	private static boolean paused;

	/**
	 * Called when the {@link Application} is first created.
	 */
	public void create() {
		GameApplication.paused = false;

		/**
		 * Load all constant variables for the library to use and sets the logging level
		 */
		LibraryConstants.load(Application.LOG_INFO);

		this.setScreen(new ApplicationScreen(this));
	}

	/**
	 * Called when the {@link Application} is resized. This can happen at any point
	 * during a non-paused state but will never happen before a call to
	 * {@link #create()}.
	 * 
	 * @param width  the new width in pixels
	 * @param height the new height in pixels
	 */
	public void resize(int width, int height) {
		if (this.currentScreen != null) {
			this.currentScreen.resize(width, height);
		}
	}

	/**
	 * Called when the {@link Application} should render itself.
	 */
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);

		if (this.currentScreen != null) {
			this.currentScreen.render();
			this.currentScreen.getStage().act();
			this.currentScreen.getStage().draw();
			if (!GameApplication.paused) {
				LibraryConstants.getTickPool().update(Gdx.graphics.getDeltaTime());
				this.currentScreen.update(Gdx.graphics.getDeltaTime());
			}
		}
	}

	/**
	 * Called when the {@link Application} is paused, usually when it's not active
	 * or visible on-screen. An Application is also paused before it is destroyed.
	 */
	public void pause() {
		GameApplication.paused = true;
	}

	/**
	 * Called when the {@link Application} is resumed from a paused state, usually
	 * when it regains focus.
	 */
	public void resume() {
		GameApplication.paused = false;
	}

	/**
	 * Called when the {@link Application} is destroyed. Preceded by a call to
	 * {@link #pause()}.
	 */
	public void dispose() {
		if (this.currentScreen != null) {
			this.currentScreen.dispose();
		}
	}

	/**
	 * Sets the current screen of this {@code PrevailGame} to the given
	 * {@code screen} argument. The {@code GameScreen} set will have
	 * {@link GameScreen#enterScreen()} called as well as
	 * {@link GameScreen#resize(int, int)}. The current screen, if not <b>null</b>,
	 * will be disposed by {@link GameScreen#dispose()}.
	 * 
	 * @param screen the screen to set
	 */
	public void setScreen(GameScreen screen) {
		if (screen == null) {
			throw new NullPointerException("You cannot set a NULL GameScreen");
		}
		if (this.currentScreen != null) {
			this.currentScreen.dispose(); // disposes of the current screen before it sets a new screen
		}
		this.currentScreen = screen;
		if (this.currentScreen != null) {
			this.currentScreen.create();
			this.currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	/**
	 * Returns the current screen this game is on.
	 * 
	 * @return the current screen
	 */
	public GameScreen getScreen() {
		return this.currentScreen;
	}

}
