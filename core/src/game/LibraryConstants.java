package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
	private static final AssetManager ASSET_MANAGER = new AssetManager();

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
		 * Load the VisUI skin.
		 */
		VisUI.load();
		Gdx.app.log("Library Constants", "Empty VisUI Skin Loaded.");

		/**
		 * Set the loader types to the asset manager
		 */
		FileHandleResolver resolver = new InternalFileHandleResolver();
		ASSET_MANAGER.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		ASSET_MANAGER.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		ASSET_MANAGER.setLoader(TiledMap.class, new TmxMapLoader());
		Gdx.app.log("Library Constants", "Loader types set");

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

	/**
	 * Returns the {@code AssetManager} that this game loads assets with.
	 * 
	 * @return the asset manager
	 */
	public static AssetManager getAssetManager() {
		return LibraryConstants.ASSET_MANAGER;
	}

	/**
	 * Returns the asset of the given class type with the corresponding fileName.
	 * 
	 * @param <T>      the type of class
	 * @param fileName the name of the file
	 * @param clazz    the caster
	 * @return the asset
	 */
	public static <T> T getAsset(String fileName) {
		return LibraryConstants.ASSET_MANAGER.get(fileName);
	}

	/**
	 * Returns the asset of the given class type with the corresponding fileName.
	 * 
	 * @param <T>      the type of class
	 * @param fileName the name of the file
	 * @param clazz    the caster
	 * @return the asset
	 */
	public static <T> T getAsset(String fileName, Class<T> clazz) {
		return LibraryConstants.ASSET_MANAGER.get(fileName, clazz);
	}

}
