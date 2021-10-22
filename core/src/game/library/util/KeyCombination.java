package game.library.util;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;

/**
 * This class is used to hold different key combinations.
 * 
 * @author Albert Beaupre
 */
public final class KeyCombination {
	private static final HashMap<String, KeyCombination> COMBINATIONS = new HashMap<>();

	private final int[] keyCodes;
	private final String name;

	/**
	 * Constructs a new {@code KeyCombination}. The name parameter will be used to
	 * identify the combination and the key codes are the codes for each of the keys
	 * that can be used to trigger the action.
	 * 
	 * @param name     the name of the combination
	 * @param keyCodes the different key codes that can trigger the action
	 */
	public KeyCombination(String name, int... keyCodes) {
		this.keyCodes = keyCodes;
		this.name = name;
	}

	/**
	 * Returns the different key codes assigned to the key combination
	 * 
	 * @return the key codes
	 */
	public int[] getKeyCodes() {
		return this.keyCodes;
	}

	/**
	 * Returns true if the given key code is within this combination.
	 * 
	 * @param keyCode the key code to check
	 * @return true if found; return false otherwise
	 */
	public boolean isInCombination(int keyCode) {
		for (int c : this.keyCodes) {
			if (keyCode == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if any key within this combination is pressed.
	 * 
	 * @return true if pressed; return false otherwise
	 */
	public boolean isPressed() {
		for (int c : this.keyCodes) {
			if (Gdx.input.isKeyPressed(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if any key within this combination has just been pressed once;
	 * 
	 * @return true if pressed once; return false otherwise
	 */
	public boolean isJustPressed() {
		for (int c : this.keyCodes) {
			if (Gdx.input.isKeyJustPressed(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the name of this {@code KeyCombination}.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds the combination of given {@code keyCodes} to the map of combinations by
	 * the correlating name.
	 * 
	 * @param name     the name of the combination
	 * @param keyCodes the different types of key codes
	 */
	public static void addCombination(String name, int... keyCodes) {
		KeyCombination combination = new KeyCombination(name, keyCodes);
		KeyCombination.COMBINATIONS.put(name, combination);
	}

	/**
	 * Removes the combination with the correlated name.
	 * 
	 * @param name the name of the combination
	 */
	public static void removeCombination(String name) {
		KeyCombination.COMBINATIONS.remove(name);
	}

	/**
	 * Retrieves the first key combination that contains the given key code as one
	 * of its keys. Returns null if no combination has been found.
	 * 
	 * @param keyCode the key code the search
	 * @return the combination found; returns null otherwise
	 */
	public static KeyCombination forKeyCode(int keyCode) {
		for (KeyCombination combo : COMBINATIONS.values()) {
			for (int k : combo.keyCodes) {
				if (k == keyCode) {
					return combo;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the key combination with the given name that has been assigned to it.
	 * If no key combination has been found, then null is returned.
	 * 
	 * @param name the name of the combination
	 * @return the combination found; returns null otherwise
	 */
	public static KeyCombination forCombinationName(String name) {
		for (KeyCombination combo : COMBINATIONS.values()) {
			if (combo.name.equals(name)) {
				return combo;
			}
		}
		return null;
	}
}
