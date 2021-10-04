package game.library.examples;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisLabel;

import game.library.LibGDXGameLibrary;
import game.library.screen.GameScreen;
import game.library.tick.Tick;

/**
 * This class is used as an example for creating a game screen.
 * 
 * @author Albert Beaupre
 * 
 * @see game.library.screen.GameScreen
 */
public class Examples extends GameScreen {

	private VisLabel fpsLabel;

	/**
	 * Constructs a new {@code GameScreen} with the given {@code game} argument as
	 * the parenting application.
	 * 
	 * @param game the parenting application
	 */
	public Examples(LibGDXGameLibrary game) {
		super(game);
	}

	@Override
	public void create() {
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
	}

	@Override
	public void update(float delta) {}

}
