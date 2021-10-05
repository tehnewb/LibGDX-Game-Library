package library.tick;

import com.badlogic.gdx.utils.Pool.Poolable;

import game.LibraryConstants;

/**
 * This class is used for timed actions. The delay of the tick is the time
 * between the execution of the action. This class is poolable.
 * 
 * @author Albert Beaupre
 * 
 * @see com.badlogic.gdx.utils.Pool.Poolable
 * @see library.tick.TickPool
 */
public class Tick implements Poolable {

	private float delay; // the delay in seconds
	private float duration; // the duration in fractional seconds
	private short occurences; // the amount of times this tick has ticked
	private Runnable action; // the action to execute every delay occurrence

	private boolean stopped; // the flag to determine if this tick needs stopped

	/**
	 * Starts this {@code Tick} by adding it to the {@code TickPool}.
	 */
	public final void start() {
		LibraryConstants.getTickPool().addTick(this);
	}

	/**
	 * Updates this {@code Tick} with the given delta. This method uses the delta to
	 * determine the duration of the tick.
	 * 
	 * @param delta the time between frames
	 */
	protected void update(float delta) {
		if (this.stopped) return;

		this.duration += delta;

		if (this.duration >= this.delay) {
			this.duration = 0;
			if (action == null) throw new NullPointerException("The action of a Tick must be set!");
			this.action.run();
			this.occurences++;
		}
	}

	/**
	 * Sets the runnable action of this {@code Tick} to the given argument. This
	 * action will be executed every time the duration of this tick equals the
	 * delay.
	 * 
	 * @param action the action to execute
	 */
	public Tick action(Runnable action) {
		this.action = action;
		return this;
	}

	/**
	 * Returns the action of this tick.
	 * 
	 * @return the action
	 */
	public Runnable getAction() {
		return action;
	}

	/**
	 * Sets the delay of this {@code Tick} to the given argument. The delay is the
	 * time between each execution of this tick's action. This method can be used to
	 * manipulation the delay even while the tick is running.
	 * 
	 * @param delay the delay of the tick
	 */
	public Tick delay(float delay) {
		this.delay = delay;
		return this;
	}

	/**
	 * Stops this {@code Tick} from updating any further.
	 */
	public void stop() {
		this.stopped = true;
	}

	/**
	 * Returns true if this {@code Tick} has been stopped.
	 * 
	 * @return true if stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * Returns the amount of times this {@code Tick} has ticked.
	 * 
	 * @return the tick amount
	 */
	public short getOccurences() {
		return occurences;
	}

	/**
	 * Resets the object for reuse. Object references should be nulled and fields
	 * may be set to default values.
	 */
	public void reset() {
		this.delay = 0;
		this.duration = 0;
	}

}
