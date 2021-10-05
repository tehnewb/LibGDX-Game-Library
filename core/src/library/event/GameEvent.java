package library.event;

import game.LibraryConstants;

/**
 * The {@code Event} class represents an actual event occurring. Events are used
 * to help organize actions in a program relevant to themselves. For example, if
 * you wish to create an {@code Event} relevant only to a specific entity called
 * {@code Animal}, then you would create an {@code Event} called
 * {@code AnimalEvent} and call that event whenever a relevant
 * {@code AnimalEvent} entity.actor.action would occur.
 * 
 * <p>
 * The {@link library.event.GameEventManager} class is used to manage the
 * calling, registering, and unregistering of an {@code Event}.
 * 
 * @see library.event.GameEventManager
 * @see library.event.GameEventListener
 * @see library.event.GameEventMethod
 * 
 * @author Albert Beaupre
 */
public class GameEvent {

	private boolean consumed; // This will flag whether or not this specific event has been used.
	private boolean cancelled; // This will flag whether or not this specific event has been cancelled.

	/**
	 * Calls this {@code Event} for the {@code EventManager} within the
	 * {@link game.LibraryConstants} to have any {@code EventListener} listen for
	 * this {@code Event}.
	 * 
	 * <p>
	 * This is effectively equivalent to:
	 * 
	 * <pre>
	 * Attachments.getEventManager().callEvent(this);
	 * </pre>
	 */
	public void call() {
		LibraryConstants.getEventManager().callEvent(this);
	}

	/**
	 * This will switch the {@code consumed} flag of this {@code Event} to be on so
	 * the {@link library.event.GameEventManager} will know it has been consumed
	 * and will <b>not continue to be called.</b>
	 */
	public void consume() {
		this.consumed = true;
	}

	/**
	 * Returns {@code true} if this {@code Event} has been flagged as consumed so it
	 * will <b>not continue to be called</b> by the
	 * {@link library.event.GameEventManager}, otherwise returns {@code false}.
	 * 
	 * @return true if flagged as consumed; return false otherwise
	 */
	public boolean isConsumed() {
		return this.consumed;
	}

	/**
	 * This will switch the {@code cancelled} flag of this {@code Event} to be on so
	 * the {@link library.event.GameEventManager} will know it has been cancelled
	 * and will <b>not be called at all.</b>
	 */
	public void cancel() {
		this.cancelled = true;
	}

	/**
	 * Returns {@code true} if this {@code Event} has been flagged as cancelled so
	 * it will <b>not be called at all</b> by the
	 * {@link library.event.GameEventManager}, otherwise returns {@code false}.
	 * 
	 * @return true if flagged as cancelled; return false otherwise
	 */
	public boolean isCancelled() {
		return cancelled;
	}

}
