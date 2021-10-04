package game.library;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.kotcrab.vis.ui.VisUI;

/**
 * This class holds all the library constant variables.
 * 
 * @author Albert Beaupre
 */
public final class LibraryConstants {

	/**
	 * Loads all variables to be defined in the application.
	 * 
	 * <p>
	 * Current loading these constants:
	 * <ul>
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
		 * Loading and setting the cursor to our own person image.
		 */
		FileHandle cursorHandle = Gdx.files.internal("Cursors/Pointer.png");
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
		Gdx.app.log("Library Constants", "System Garbage Collector Called.");
	}

}
