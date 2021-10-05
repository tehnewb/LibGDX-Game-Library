package game.library.screen;

import java.awt.event.ItemEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.widget.VisLabel;

import game.GameApplication;
import game.library.event.GameEvent;
import game.library.event.GameEventListener;
import game.library.event.GameEventMethod;
import game.library.tick.Tick;

/**
 * This class is used as an example for creating a game screen.
 * 
 * @author Albert Beaupre
 * 
 * @see game.library.screen.GameScreen
 */
public class ApplicationScreen extends GameScreen {

	private Texture titleTexture;
	private VisLabel fpsLabel;

	/**
	 * Constructs a new {@code GameScreen} with the given {@code game} argument as
	 * the parenting application.
	 * 
	 * @param game the parenting application
	 */
	public ApplicationScreen(GameApplication game) {
		super(game);
	}

	class PlayerMovementEvent extends GameEvent {

	}

	class PlayerMovementListener implements GameEventListener {

		@GameEventMethod
		public void checkItemAdd(ItemEvent event) {
			System.out.println("WHUT DUDE!");
		}
	}

	@Override
	public void create() {
		this.titleTexture = new Texture("images/Title.png");
		this.fpsLabel = new VisLabel("FPS: ");
		this.fpsLabel.setPosition(15, Gdx.graphics.getHeight() - 40);
		this.fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
		this.screenStage.addActor(fpsLabel);

		/**
		 * @TICK_EXAMPLE This is an example of submitting a tick to the application.
		 * 
		 *               The tick below has a delay of 1 seconds and updates the FPS
		 *               label every time
		 */
		new Tick().action(() -> fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond())).delay(1).start();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		this.fpsLabel.setPosition(15, Gdx.graphics.getHeight() - 40); // update position of fps label
	}

	@Override
	public void render() {
		// TODO render all things for the screen here
		int titleX = Gdx.graphics.getWidth() / 2 - this.titleTexture.getWidth() / 2;
		int titleY = Gdx.graphics.getHeight() - this.titleTexture.getHeight() - 15;

		this.screenBatch.draw(this.titleTexture, titleX, titleY);
	}

	@Override
	public void update(float delta) {}

}
