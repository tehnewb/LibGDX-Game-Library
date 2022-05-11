package game;

import java.util.Objects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

import game.library.screen.GameScreen;

/**
 * This class is the {@code ApplicationListener} for the application. Handles
 * all {@link GameScreen} screens.
 * 
 * @author Albert Beaupre
 * @see game.library.screen.GameScreen
 */
public class GameApplicationListener implements ApplicationListener {

	private static boolean PAUSED = false; // The paused flag determined whether or not the game will update

	private GameScreen currentScreen;

	/**
	 * Called when the {@link Application} is first created.
	 */
	public void create() {
		/**
		 * Load all constant variables for the library to use and sets the logging level
		 */
		LibraryConstants.load(Application.LOG_INFO);
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
		if (Objects.nonNull(this.currentScreen)) {
			this.currentScreen.resize(width, height);
		}
	}

	/**
	 * Called when the {@link Application} should render itself.
	 */
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1, false);

		if (Objects.nonNull(this.currentScreen)) {

			if (!GameApplicationListener.PAUSED) {
				final float deltaTime = Gdx.graphics.getDeltaTime();

				/**
				 * World updating
				 */
				this.currentScreen.update(deltaTime);
				this.currentScreen.getWorld().step(deltaTime, 1, 1);

				/**
				 * Ray handler updating
				 */
				this.currentScreen.getBatchCamera().update();
				this.currentScreen.getRayHandler().setCombinedMatrix(this.currentScreen.getBatchCamera());
				this.currentScreen.getRayHandler().update();
				this.currentScreen.getScreenBatch().setProjectionMatrix(this.currentScreen.getBatchCamera().combined);

				LibraryConstants.getTickPool().update(deltaTime);
				LibraryConstants.getProjectilePool().update(deltaTime);
			}
			this.currentScreen.render();
			this.currentScreen.getRayHandler().render();
			this.currentScreen.getStage().act();
			this.currentScreen.getStage().draw();

			LibraryConstants.getProjectilePool().render(this.currentScreen.getScreenBatch());
		}
	}

	/**
	 * Called when the {@link Application} is paused, usually when it's not active
	 * or visible on-screen. An Application is also paused before it is destroyed.
	 */
	public void pause() {
		GameApplicationListener.PAUSED = true;
	}

	/**
	 * Called when the {@link Application} is resumed from a paused state, usually
	 * when it regains focus.
	 */
	public void resume() {
		GameApplicationListener.PAUSED = false;
	}

	/**
	 * Called when the {@link Application} is destroyed. Preceded by a call to
	 * {@link #pause()}.
	 */
	public void dispose() {
		if (Objects.nonNull(this.currentScreen)) {
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
		if (Objects.nonNull(this.currentScreen)) {
			this.currentScreen.dispose(); // disposes of the current screen before it sets a new screen
		}
		this.currentScreen = screen;
		if (Objects.nonNull(this.currentScreen)) {
			this.currentScreen.create();
			this.currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		Gdx.input.setInputProcessor(screen);
	}

	/**
	 * Returns the current screen this game is on.
	 * 
	 * @return the current screen
	 */
	public GameScreen getScreen() {
		return this.currentScreen;
	}

	/**
	 * Returns true if this {@code GameApplicationListener} is paused. If this
	 * returns true, then the application will not update, but it will still render.
	 * 
	 * @return true if paused; return false otherwise
	 */
	public static boolean isPaused() {
		return GameApplicationListener.PAUSED;
	}

}
