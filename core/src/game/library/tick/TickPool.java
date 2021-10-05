package game.library.tick;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * This is a pool for the {@code Tick} class. Ticks are added to this pool by
 * using the {@link #addTick(Runnable, float)} method. The action of the tick is
 * executed between the tick delay.
 * 
 * @author Albert Beaupre
 * 
 * @see game.library.tick.Tick
 * @see com.badlogic.gdx.utils.Pool
 */
public class TickPool extends Pool<Tick> {

	private Array<Tick> runningTicks;

	/**
	 * Constructs a new {@code TickPool} with an initial capacity of the given
	 * argument.
	 * 
	 * @param initialCapacity the initial capacity of the tick array
	 */
	public TickPool(int initialCapacity) {
		this.runningTicks = new Array<>(false, initialCapacity);
	}

	/**
	 * Updates this {@code TickPool} with the given delta. This method uses the
	 * delta to determine the duration for each of the ticks. Every tick that has
	 * been created through this pool will be
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		for (int i = 0; i < this.runningTicks.size; i++) {
			Tick tick = this.runningTicks.get(i);
			if (tick == null) continue; // ignore empty spots in the array

			if (tick.isStopped()) {
				this.free(tick);
				this.runningTicks.set(i, null);
				continue;
			}
			tick.update(delta); // updates all ticks within the array
		}
	}

	/**
	 * Adds a new {@code Tick} to this {@code TickPool} with the given action and
	 * delay.
	 * 
	 * @param action the action to execute
	 * @param delay  the delay between actions
	 * @return the tick created
	 */
	public Tick addTick(Runnable action, float delay) {
		Tick tick = this.obtain().action(action).delay(delay);
		this.runningTicks.add(tick);
		return tick;
	}

	/**
	 * Adds a new {@code Tick} to the pool. The action and delay of the tick must be
	 * set for it to
	 * 
	 * @param tick the tick to add
	 */
	public void addTick(Tick tick) {
		this.runningTicks.add(tick);
	}

	/**
	 * This is called when there are no free objects to reuse.
	 */
	protected Tick newObject() {
		return new Tick();
	}

}
