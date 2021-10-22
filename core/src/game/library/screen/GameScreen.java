package game.library.screen;

import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.RayHandler;
import game.GameApplicationListener;

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
	protected GameApplicationListener game;

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

	/**
	 * The ray handler is used for rendering lights to the world
	 */
	protected final RayHandler rayHandler;

	/**
	 * This is the world the ray handler uses and can be used for bodies
	 */
	protected final World world;

	private Vector2 mouseVector; // the reusable vector for handling the mouse coordinates
	private Viewport stageViewport; // the viewport used for the stage
	private Music currentMusic;
	private boolean interactable; // the flag used to determine if this screen is interactable
	private boolean transitioning; // the flag used to determine if this screen is transitioning

	static {
		/**
		 * Sets the ray handler diffiuse light and gamma correction flags
		 */
		RayHandler.useDiffuseLight(false);
		RayHandler.setGammaCorrection(false);
	}

	/**
	 * Constructs a new {@code GameScreen} with the given {@code game} as the
	 * parenting application listener.
	 * 
	 * @param game the application listener
	 */
	public GameScreen(GameApplicationListener game) {
		this.game = game;
		this.world = new World(new Vector2(0, 0), false);
		this.rayHandler = new RayHandler(world);
		this.rayHandler.setAmbientLight(0, 0, 0, 0.85f);
		this.rayHandler.setShadows(false);
		this.rayHandler.setBlur(true);

		this.batchCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.screenStage = new Stage(this.stageViewport);
		this.screenFont = new BitmapFont();
		this.mouseVector = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		this.interactable = true;
		this.screenBatch = new SpriteBatch();
	}

	/**
	 * Fades from this current screen to the next screen and sets the game
	 * application's current screen to the new one.
	 * 
	 * @param newScreen the new screen to set
	 */
	public void transitionToScreen(final GameScreen newScreen) {
		if (transitioning) return;

		newScreen.interactable = false;
		this.interactable = false;
		this.screenStage.getRoot().getColor().a = 1;
		SequenceAction sequenceAction = new SequenceAction();
		sequenceAction.addAction(Actions.fadeOut(1.5f));

		sequenceAction.addAction(Actions.run(() -> {
			game.setScreen(newScreen);
			newScreen.screenStage.getRoot().getColor().a = 0;
			SequenceAction sequenceAction2 = new SequenceAction();
			sequenceAction2.addAction(Actions.fadeIn(1.5f));
			sequenceAction2.addAction(Actions.run(() -> newScreen.interactable = true));
			newScreen.screenStage.getRoot().addAction(sequenceAction2);
		}));
		this.screenStage.getRoot().addAction(sequenceAction);
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
	 * This method is called when this screen is disposed. This method should be
	 * used to dispose anything this screen has created.
	 */
	public abstract void destroy();

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
		if (this.screenStage == null) return false;
		return this.interactable && this.screenStage.keyDown(keyCode);
	}

	/**
	 * Called when a key was released
	 * 
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed
	 */
	public boolean keyUp(int keyCode) {
		if (this.screenStage == null) return false;
		return this.interactable && this.screenStage.keyUp(keyCode);
	}

	/**
	 * Called when a key was typed
	 * 
	 * @param character The character
	 * @return whether the input was processed
	 */
	public boolean keyTyped(char character) {
		if (this.screenStage == null) return false;
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
		if (this.screenStage == null) return false;
		Actor hitActor = this.getHitActor(screenX, screenY);
		if (Objects.isNull(hitActor)) {
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
		if (this.screenStage == null) return false;
		return this.interactable && this.screenStage.touchUp(screenX, screenY, pointer, button);
	}

	/**
	 * Called when a finger or the mouse was dragged.
	 * 
	 * @param pointer the pointer for the event.
	 * @return whether the input was processed
	 */
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (this.screenStage == null) return false;
		return this.interactable && this.screenStage.touchDragged(screenX, screenY, pointer);
	}

	/**
	 * Called when the mouse was moved without any buttons being pressed. Will not
	 * be called on iOS.
	 * 
	 * @return whether the input was processed
	 */
	public boolean mouseMoved(int screenX, int screenY) {
		if (this.screenStage == null) return false;
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
		if (this.screenStage == null) return false;
		return this.interactable && this.screenStage.scrolled(amountX, amountY);
	}

	/**
	 * Adds an action to the stage of this {@code GameScreen}.
	 * 
	 * @param action the action to add
	 */
	public void addAction(Action action) {
		if (this.screenStage == null) return;
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
		if (this.screenStage == null) return null;
		this.screenStage.screenToStageCoordinates(this.mouseVector.set(screenX, screenY));
		return this.screenStage.hit(mouseVector.x, mouseVector.y, false);
	}

	/**
	 * Sets the current music of this screen to the given argument and begins
	 * playing the music.
	 * 
	 * @param music the music to set
	 */
	public void setMusic(Music music) {
		if (Objects.nonNull(this.currentMusic)) {
			this.currentMusic.stop();
		}

		this.currentMusic = Objects.requireNonNull(music);
		this.currentMusic.play();
	}

	/**
	 * Disposes this screen's stage, font, batch, and mouse vector.
	 */
	public void dispose() {
		this.destroy();
		this.screenStage.dispose();
		this.screenFont.dispose();
		this.screenBatch.dispose();
		this.rayHandler.dispose();
		this.world.dispose();
		this.mouseVector = null;
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
	 * Returns the ray handler used to render lights to this screen.
	 * 
	 * @return the ray handler
	 */
	public RayHandler getRayHandler() {
		return rayHandler;
	}

	/**
	 * Returns the world that handles the bodies and lights of this screen
	 * 
	 * @return the world
	 */
	public World getWorld() {
		return world;
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
