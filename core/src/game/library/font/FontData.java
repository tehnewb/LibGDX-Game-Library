package game.library.font;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * The FontData class holds all information necessary for obtaining or
 * generating fonts.
 * 
 * @author Albert Beaupre
 */
public final class FontData {

	private static final HashMap<String, FreeTypeFontGenerator> FONTS = new HashMap<>();

	private FontData() {
		// Inaccessible.
	}

	/**
	 * Loads the given font and adds it to this {@code FontData} class.
	 * 
	 * @param fontName the name of the font
	 * @param handle   the file handle attached to the font file
	 */
	public static void loadFont(String fontName, FileHandle handle) {
		FONTS.put(fontName, new FreeTypeFontGenerator(handle));
	}

	/**
	 * Returns the font generated using the given {@code parameter}.
	 * 
	 * @param parameter the parameter used to generate the font
	 * @return the generated font
	 */
	public static BitmapFont generateFont(String name, FreeTypeFontParameter parameter) {
		FreeTypeFontGenerator generator = FONTS.get(name);
		if (generator == null) throw new NullPointerException("Font with name of " + name + " does not exist");
		return generator.generateFont(parameter);
	}

	/**
	 * Returns the font generated using the given {@code parameter}.
	 * 
	 * @param parameter the parameter used to generate the font
	 * @param markup    the flag used to set the font to markup font
	 * @return the generated font
	 */
	public static BitmapFont generateFont(String name, FreeTypeFontParameter parameter, boolean markup) {
		FreeTypeFontGenerator generator = FONTS.get(name);
		if (generator == null) throw new NullPointerException("Font with name of " + name + " does not exist");
		BitmapFont font = generator.generateFont(parameter);
		font.getData().markupEnabled = markup;
		return font;
	}
}
