package game.library.event;

import java.lang.reflect.Method;
import java.util.Objects;

import com.badlogic.gdx.utils.Array;

/**
 * <p>
 * Holds {@code EventListener} implementations to listen for {@code Event} calls
 * that are relevant to them. Once an {@code EventListener} has been registered,
 * by the {@link GameEventManager#registerEventListener(GameEventListener)}
 * method, any method annotated by the {@code EventMethod} annotation will be
 * registered to listening to its relevant {@code Event}. If an
 * {@code EventListener} has successfully listened to an {@code Event}, any
 * method that has been registered within the listener will be called.
 * 
 * @see game.library.event.GameEvent
 * @see game.library.event.GameEventListener
 * @see game.library.event.GameEventMethod
 * 
 * @author Albert Beaupre
 */
public class GameEventManager {

	/**
	 * Used to execute a {@code Method} when an {@code EventListener} has listened
	 * to the call of an {@code Event}.
	 * 
	 * @author Albert
	 */
	private class EventExecutor {

		private final GameEventListener listener;
		private final Method method;

		/**
		 * Constructs a new {@code EventExecutor} with the specified {@code listener}
		 * that uses the specified {@code method} for execution.
		 * 
		 * @param listener the event listener with the underlying method
		 * @param method   the method to use for execution
		 */
		public EventExecutor(GameEventListener listener, Method method) {
			this.listener = listener;
			this.method = method;
		}

		/**
		 * Executes the specified {@code event} using the {@code EventListener} and
		 * {@code Method} attached to this {@code EventExecutor.}
		 * 
		 * @param event the event to execute
		 */
		public void execute(GameEvent event) {
			try {
				method.invoke(listener, event);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}

		@Override
		public boolean equals(Object obj) {
			return listener.equals(obj);
		}

		@Override
		public int hashCode() {
			return listener.hashCode();
		}

		@Override
		public String toString() {
			return listener.getClass().getSimpleName();
		}

	}

	/**
	 * This map is used to store event listener methods based on their relevant
	 * event.
	 */
	private final Array<EventExecutor> eventExecutors;

	/**
	 * Constructs a new {@code EventManager} with no {@code EventListener}
	 * registered.
	 */
	public GameEventManager() {
		this.eventExecutors = new Array<>(false, 10);
	}

	/**
	 * Registers the specified {@code listener} to this {@code EventManager} to
	 * listen for any events relevant to the methods the {@code listener} has.
	 * 
	 * @param listener the listener to be registered
	 * 
	 * @see game.library.event.GameEventListener
	 * 
	 * @throws NullPointerException if the listener argument is null
	 */
	public void registerEventListener(GameEventListener listener) {
		if (Objects.isNull(listener)) throw new NullPointerException("Cannot register NULL EventListener");

		for (Method method : listener.getClass().getMethods()) {
			if (method.getParameterTypes().length != 1) continue;
			if (!method.isAnnotationPresent(GameEventMethod.class)) continue;
			method.setAccessible(true);
			this.eventExecutors.add(new EventExecutor(listener, method));
		}
	}

	/**
	 * Unregisters the specified {@code listener} from this {@code EventManager} if
	 * it is existing, so it cannot listen for any events.
	 * 
	 * @param listener the listener to be unregistered, if existing
	 * 
	 * @see game.library.event.GameEventListener
	 * 
	 * @throws NullPointerException if the listener argument is null
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void unregisterEventListener(GameEventListener listener) {
		if (listener == null) throw new NullPointerException("Cannot unregister NULL EventListener");

		for (int index = 0; index < this.eventExecutors.size; index++) {
			EventExecutor executor = this.eventExecutors.get(index);
			if (Objects.isNull(executor)) continue;
			if (executor.equals(listener)) {
				this.eventExecutors.set(index, null);
			}
		}

	}

	/**
	 * Calls the specified {@code event} so that any registered
	 * {@code EventListener} will listen for the call and be executed upon receiving
	 * the call.
	 * 
	 * @param event the event to be called for listening
	 * 
	 * @see game.library.event.GameEvent
	 * 
	 * @throws NullPointerException if the event argument is null
	 */
	public void callEvent(GameEvent event) {
		Objects.requireNonNull(event, "Cannot call a NULL Event");

		for (int index = 0; index < this.eventExecutors.size; index++) {
			EventExecutor executor = this.eventExecutors.get(index);
			if (event.isCancelled()) continue;
			executor.execute(event);
			if (event.isConsumed()) break;
		}
	}
}
