package game.library.util;

import com.badlogic.gdx.math.Vector2;

public class CalcUtils {

	/**
	 * Returns the angle from the starting vector to the ending vector.
	 * 
	 * @param start the starting location
	 * @param end   the ending location
	 * @return the angle in float type
	 */
	public static float getAngle(Vector2 start, Vector2 end) {
		return (float) (Math.atan2(end.y - start.y, end.x - start.x) * (180f / Math.PI));
	}
}
