package library.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.GameApplication;

/**
 * This class is used to render things on the application's screen. A font,
 * stage, camera, batch, and viewport has been assigned to this class for use by
 * the inheriting class.
 * 
 * @author Albert Beaupre
 */
public abstract class GameScreen implements InputProcessor {

	/**
	 * The parenting game application for this screen
	 */
	protected GameApplication game;

	/**
	 * The font used to render text on this screen
	 */
	protected BitmapFont screenFont;

	/**
	 * The stage used to render widgets on this screen
	 */
	protected Stage screenStage;

	/**
	 * The batch used for drawing textures on this screen
	 */
	protected SpriteBatch screenBatch;

	/**
	 * The camera used for the sprite batch
	 */
	protected OrthographicCamera batchCamera;

	private OrthographicCamera stageCamera; // the camera used for the stage
	private Viewport stageViewport; // the viewport used for the stage

	private boolean interactable; // the flag used to determine if this screen is interactable
	private Vector2 mouseVector; // the reusable vector for handling the mouse coordinates

	/**
	 * Constructs a new {@code GameScreen} with the given {@code game} as the
	 * parenting application listener.
	 * 
	 * @param game the application listener
	 */
	public GameScreen(GameApplication game) {
		this.game = game;
		this.batchCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stageViewport = new ScreenViewport(this.stageCamera);
		this.screenStage = new Stage(this.stageViewport);
		this.screenFont = new BitmapFont();
		this.mouseVector = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		this.interactable = true;
		this.screenBatch = new SpriteBatch();

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
		this.batchCamera.setToOrtho(false, width, height);
		this.batchCamera.update();
		this.screenBatch.setProjectionMatrix(this.batchCamera.combined);
	}

	/**
	 * Called when a key was pressed
	 * 
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed
	 */
	public boolean keyDown(int keyCode) {
		return this.interactable && this.screenStage.keyDown(keyCode);
	}

	/**
	 * Called when a key was released
	 * 
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed
	 */
	public boolean keyUp(int keyCode) {
		return this.interactable && this.screenStage.keyUp(keyCode);
	}

	/**
	 * Called when a key was typed
	 * 
	 * @param character The character
	 * @return whether the input was processed
	 */
	public boolean keyTyped(char character) {
		return this.interactable && this.screenStage.keyTyped(character);
	}

	/**
	 * Called when the screen was touched or a mouse button was pressed. The button
	 * parameter will be {@link Buttons#LEFT} on iOS.
	 * 
	 * @param screenX The x coordinate, origin is in the upper left corner
	 * @param screenY The y coordinate, origin is in the upper left corner
	 * @param pointer the pointer for the event.
	 * @param button  the button
	 * @return whether the input was processed
	 */
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Actor hitActor = this.getHitActor(screenX, screenY);
		if (hitActor == null) {
			this.screenStage.unfocusAll();
		}
		return this.interactable && this.screenStage.touchDown(screenX, screenY, pointer, button);
	}

	/**
	 * Called when a finger was lifted or a mouse button was released. The button
	 * parameter will be {@link Buttons#LEFT} on iOS.
	 * 
	 * @param pointer the pointer for the event.
	 * @param button  the button
	 * @return whether the input was processed
	 */
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return this.interactable && this.screenStage.touchUp(screenX, screenY, pointer, button);
	}

	/**
	 * Called when a finger or the mouse was dragged.
	 * 
	 * @param pointer the pointer for the event.
	 * @return whether the input was processed
	 */
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return this.interactable && this.screenStage.touchDragged(screenX, screenY, pointer);
	}

	/**
	 * Called when the mouse was moved without any buttons being pressed. Will not
	 * be called on iOS.
	 * 
	 * @return whether the input was processed
	 */
	public boolean mouseMoved(int screenX, int screenY) {
		return this.interactable && this.screenStage.mouseMoved(screenX, screenY);
	}

	/**
	 * Called when the mouse wheel was scrolled. Will not be called on iOS.
	 * 
	 * @param amountX the horizontal scroll amount, negative or positive depending
	 *                on the direction the wheel was scrolled.
	 * @param amountY the vertical scroll amount, negative or positive depending on
	 *                the direction the wheel was scrolled.
	 * @return whether the input was processed.
	 */
	public boolean scrolled(float amountX, float amountY) {
		return this.interactable && this.screenStage.scrolled(amountX, amountY);
	}

	/**
	 * Adds an action to the stage of this {@code GameScreen}.
	 * 
	 * @param action the action to add
	 */
	public void addAction(Action action) {
		this.screenStage.addAction(action);
	}

	/**
	 * Returns the actor hit at the given {@code screenX} and {@code screenY}
	 * coordinates of this stage. If there is no actor at the given coordinates,
	 * null is returned.
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
	 * Disposes this screen's stage, font, batch, and mouse vector.
	 */
	public void dispose() {
		this.screenStage.dispose();
		this.screenFont.dispose();
		this.screenBatch.dispose();
		this.mouseVector = null;
		this.stageCamera = null;
		this.stageViewport = null;
		this.screenFont = null;
		this.screenBatch = null;
		this.screenStage = null;
		this.game = null;
	}

	/**
	 * Returns the stage used to render widgets on this screen.
	 * 
	 * @return the stage
	 */
	public Stage getStage() {
		return this.screenStage;
	}

	/**
	 * Returns the camera used for the sprite batch of the screen.
	 * 
	 * @return the camera
	 */
	public OrthographicCamera getBatchCamera() {
		return batchCamera;
	}

	/**
	 * Returns the sprite batch used for rendering textures to this screen.
	 * 
	 * @return the sprite batch
	 */
	public SpriteBatch getScreenBatch() {
		return screenBatch;
	}

	/**
	 * Returns true if this {@code GameScreen} <b>can</b> be interacted with.
	 * Returns false if this {@code GameScreen} <b>cannot</b> be interacted with.
	 * 
	 * @return true if interactable; return false otherwise
	 */
	public boolean isInteractable() {
		return interactable;
	}

	/**
	 * Sets the interactable flag of this {@code GameScreen} to the given argument.
	 * If this value is set to true, then this screen can be interacted with. If
	 * this value is set to false, then this screen cannot be interacted with.
	 * 
	 * @param interactable the flag to set
	 */
	public void setInteractable(boolean interactable) {
		this.interactable = interactable;
	}
}
