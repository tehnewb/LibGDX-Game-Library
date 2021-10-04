package game.library.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.library.LibGDXGameLibrary;

/**
 * This class is used to render things on the application's screen. A font,
 * stage, camera, batch, and viewport has been assigned to this class for use by
 * the inheriting class.
 * 
 * @author Albert Beaupre
 */
public abstract class GameScreen implements InputProcessor {

	protected final LibGDXGameLibrary game;
	protected final BitmapFont screenFont;
	protected final Stage screenStage;
	protected final OrthographicCamera stageCamera;
	protected final Viewport stageViewport;
	protected final SpriteBatch stageBatch;

	private Vector2 mouseVector;

	/**
	 * Constructs a new {@code GameScreen} with the given {@code game} as the
	 * parenting application listener.
	 * 
	 * @param game the application listener
	 */
	public GameScreen(LibGDXGameLibrary game) {
		this.game = game;
		this.stageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stageViewport = new ScreenViewport(this.stageCamera);
		this.screenStage = new Stage(this.stageViewport, this.stageBatch = new SpriteBatch());
		this.screenFont = new BitmapFont();
		this.mouseVector = new Vector2(Gdx.input.getX(), Gdx.input.getY());

		Gdx.input.setInputProcessor(this); // sets this screen as the input processor
	}

	/**
	 * Initializes this {@code GameScreen} for using.
	 */
	public abstract void create();

	/**
	 * Renders this {@code GameScreen}. Use the protected {@code stageBatch}
	 * variable within this class to render textures.
	 * 
	 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch
	 */
	public abstract void render();

	/**
	 * Updates this {@code GameScreen}. This method is not called when the game is
	 * paused.
	 * 
	 * @param delta the time between each frame
	 */
	public abstract void update(float delta);

	/**
	 * This method is called when the application is resized.
	 * 
	 * @param width  the new width of the screen
	 * @param height the new height of the screen
	 */
	public void resize(int width, int height) {
		this.screenStage.getViewport().update(width, height, true);
	}

	@Override
	public boolean keyDown(int keyCode) {
		return this.screenStage.keyDown(keyCode);
	}

	@Override
	public boolean keyUp(int keyCode) {
		return this.screenStage.keyUp(keyCode);
	}

	@Override
	public boolean keyTyped(char character) {
		return this.screenStage.keyTyped(character);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Actor hitActor = this.getHitActor(screenX, screenY);
		if (hitActor == null) {
			this.screenStage.unfocusAll();
		}
		return this.screenStage.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return this.screenStage.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return this.screenStage.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return this.screenStage.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return this.screenStage.scrolled(amountX, amountY);
	}

	/**
	 * Returns the actor hit at the given {@code screenX} and {@code screenY}
	 * coordinates of this stage.
	 * 
	 * @param screenX the absolute screen x coordinate
	 * @param screenY the absolute screen y coordinate
	 * @return the actor hit; returns null if no actor is hit
	 */
	public Actor getHitActor(int screenX, int screenY) {
		this.screenStage.screenToStageCoordinates(this.mouseVector.set(screenX, screenY));
		return this.screenStage.hit(mouseVector.x, mouseVector.y, false);
	}

	/**
	 * Disposes this screen as well as the stage and font and sprite batch.
	 */
	public void dispose() {
		this.screenStage.dispose();
		this.screenFont.dispose();
		this.stageBatch.dispose();
	}

	/**
	 * Returns the stage used to render widgets on this screen.
	 * 
	 * @return the stage
	 */
	public Stage getStage() {
		return this.screenStage;
	}
}
