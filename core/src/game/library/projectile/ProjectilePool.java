package game.library.projectile;

import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * The {@code ProjectilePool} class updates, renders, and reuses
 * {@code Projectile} objects.
 * 
 * @author Albert Beaupre
 * 
 * @see game.library.projectile.Projectile
 */
public class ProjectilePool extends Pool<Projectile> {

	private Array<Projectile> rendering;

	/**
	 * Constructs a new {@code ProjectilePool} with a growing initial capacity of
	 * the given argument. This capacity will grow as the pool increases the amount
	 * of projectiles it holds.
	 * 
	 * @param initialCapacity the beginning capacity of the pool
	 */
	public ProjectilePool(int initialCapacity) {
		this.rendering = new Array<>(false, initialCapacity);
	}

	/**
	 * Renders this {@code ProjectilePool} by rendering every projectile added to
	 * the pool.
	 * 
	 * @param batch the batch to render to
	 */
	public void render(SpriteBatch batch) {
		batch.begin();
		for (int index = 0; index < this.rendering.size; index++) {
			Projectile projectile = this.rendering.get(index);
			if (Objects.isNull(projectile)) continue;

			projectile.render(batch);
		}
		batch.end();
	}

	/**
	 * Updates each projectile within this pool based on the given delta time
	 * between frames
	 * 
	 * @param delta the time between frames
	 */
	public void update(float delta) {
		for (int index = 0; index < this.rendering.size; index++) {
			Projectile projectile = this.rendering.get(index);
			if (Objects.isNull(projectile)) continue;

			if (projectile.canBeDestroyed()) {
				this.free(projectile);
				this.rendering.removeIndex(index);
				continue;
			}
			projectile.update(delta);
		}
	}

	/**
	 * Adds the given {@code projectile} to the rendering process.
	 * 
	 * @param projectile the projectile to add
	 */
	public void addProjectile(Projectile projectile) {
		this.rendering.add(projectile);;
	}

	/**
	 * This is called when there are no free objects to reuse.
	 */
	protected Projectile newObject() {
		return new Projectile();
	}

}
