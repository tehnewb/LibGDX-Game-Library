package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.VisUI;

import game.library.event.GameEventManager;
import game.library.projectile.Projectile;
import game.library.projectile.ProjectilePool;
import game.library.tick.Tick;
import game.library.tick.TickPool;

/**
 * This class holds all the library constant variables.
 * 
 * @author Albert Beaupre
 */
public final class LibraryConstants {

	private static final GameEventManager EVENT_MANAGER = new GameEventManager();
	private static final GameApplicationListener APPLICATION_LISTENER = new GameApplicationListener();

	/**
	 * Loads all variables to be defined in the application.
	 * 
	 * <p>
	 * Currently loading these constants:
	 * <ul>
	 * <li>Tick Pool</li>
	 * <li>Projectile Pool</li>
	 * <li>Custom cursor</li>
	 * <li>VisUI Skin</li>
	 * <li>System Garbage Collector</li>
	 * </ul>
	 * </p>
	 * 
	 * @param logLevel the level of logging for the application
	 */
	public static void load(int logLevel) {
		Gdx.app.setLogLevel(logLevel);

		/**
		 * Adds the tick pool class to the pools map with an initial capacity of 10
		 */
		Pools.set(Tick.class, new TickPool(10));
		Gdx.app.log("Library Constants", "Set Tick Pool with initial capacity of 10.");

		/**
		 * Adds the projectile pool class to the pools map with an initial capacity of
		 * 10
		 */
		Pools.set(Projectile.class, new ProjectilePool(10));
		Gdx.app.log("Library Constants", "Set Projectile Pool with initial capacity of 10.");

		/**
		 * Loading and setting the cursor to our own person image.
		 */
		FileHandle cursorHandle = Gdx.files.internal("images/Pointer.png");
		Pixmap cursorPixmap = new Pixmap(cursorHandle);
		Cursor cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0); // 0, 0 are the coordinates of the cursor which are used to point from
		Gdx.graphics.setCursor(cursor);
		cursor.dispose(); // dispose of cursor object
		cursorPixmap.dispose(); // dispose of Pixmap
		Gdx.app.log("Library Constants", "Custom Cursor Loaded.");

		/**
		 * Load the VisUI skin.
		 */
		VisUI.load();
		Gdx.app.log("Library Constants", "VisUI Skin Loaded.");

		/**
		 * After loading all constants, clear up any garbage that may have been
		 * collected
		 */
		System.gc();
		Gdx.app.log("Library Constants", "Garbage Collector Called.");
	}

	/**
	 * Returns the {@code TickPool} constant of this {@code LibraryConstants} class.
	 * 
	 * @return the tick pool
	 */
	public static TickPool getTickPool() {
		return (TickPool) Pools.get(Tick.class);
	}

	/**
	 * Returns the {@code ProjectilePool} constant of this {@code LibraryConstants}
	 * class.
	 * 
	 * @return the projectile pool
	 */
	public static ProjectilePool getProjectilePool() {
		return (ProjectilePool) Pools.get(Projectile.class);
	}

	/**
	 * Returns the {@code EventManager} constant of this {@code LibraryConstants}
	 * class. The Event Manager is used for calling and executing events listened
	 * for by the EventListener class.
	 * 
	 * @return the event manager
	 */
	public static GameEventManager getEventManager() {
		return LibraryConstants.EVENT_MANAGER;
	}

	/**
	 * Returns the {@code ApplicationListener} that this game is handled by.
	 * 
	 * @return the application listener
	 */
	public static GameApplicationListener getApplicationListener() {
		return LibraryConstants.APPLICATION_LISTENER;
	}

}
