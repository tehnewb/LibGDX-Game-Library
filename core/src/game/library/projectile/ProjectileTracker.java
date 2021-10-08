package game.library.projectile;

import com.badlogic.gdx.math.Vector2;

/**
 * The ProjectileTracker is used for projectiles to follow. The position of this
 * tracker is the direction that the projectile constantly moves towards until
 * it finally reaches the position of this tracker. Once the projectile reaches
 * the tracker's position, then the projectile is removed from updating and
 * rendering and is stored back into the pool for reuse.
 * 
 * @author Albert Beaupre
 */
public interface ProjectileTracker {

	/**
	 * Returns the position that this tracker is at for the projectile to follow.
	 * 
	 * @return the position
	 */
	public Vector2 getPosition();

}
