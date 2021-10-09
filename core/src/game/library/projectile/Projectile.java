package game.library.projectile;

import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import game.LibraryConstants;

/**
 * This class is meant to act as a projectile within the game that can render a
 * sprite with the projectile to display as it travels.
 *
 * @author Albert Beaupre
 */
public class Projectile implements Poolable {

	private Vector2 beginningLocation;
	private Vector2 position;
	private Vector2 direction;
	private Vector2 endingLocation;

	private Sprite sprite;
	private float angle, speed;
	private float distanceLimit;

	private ProjectileTracker tracker;

	/**
	 * Creates a new {@code Projectile} reusing an object from the
	 * {@code ProjectilePool}.
	 * 
	 * @return a reused projectile object
	 */
	public static Projectile create() {
		return LibraryConstants.getProjectilePool().obtain();
	}

	/**
	 * Starts this projectile's updating and rendering process by adding it to the
	 * {@code ProjectilePool}, which handles both updating and rendering of all
	 * projectiles.
	 */
	public void start() {
		LibraryConstants.getProjectilePool().addProjectile(this);
	}

	/**
	 * Constructs a new {@code Projectile} with no starting or ending location.
	 * There will also be no sprite rendered until a sprite is set to this
	 * projectile.
	 */
	public Projectile() {
		this.beginningLocation = new Vector2(0, 0);
		this.position = new Vector2(0, 0);
		this.direction = new Vector2(0, 0);
		this.endingLocation = new Vector2(0, 0);
	}

	/**
	 * Sets the projectile tracker to the given argument. The tracker will be
	 * followed by this projectile.
	 * 
	 * @param tracker the tracker to set
	 * @return this instance for chaining
	 */
	public Projectile track(ProjectileTracker tracker) {
		this.tracker = tracker;
		return this.ending(tracker.getPosition());
	}

	/**
	 * Sets the starting beginning of this projectile to the given argument.
	 * 
	 * @param beginningLocation the beginning location
	 * @return this instance for chaining
	 */
	public Projectile beginning(Vector2 beginningLocation) {
		this.beginningLocation.set(beginningLocation.x, beginningLocation.y);
		this.position.set(this.beginningLocation.x, this.beginningLocation.y);
		return this;
	}

	/**
	 * Sets the ending location of this projectile to the given argument.
	 * 
	 * @param endingLocation the ending location
	 * @return this instance for chaining
	 */
	public Projectile ending(Vector2 endingLocation) {
		this.angle = this.getAngle(this.position, endingLocation.set(endingLocation.x, endingLocation.y));
		this.endingLocation.set(endingLocation.x, endingLocation.y);
		this.direction.set(endingLocation.x - this.position.x, endingLocation.y - this.position.y).nor();

		if (Objects.isNull(tracker) && this.distanceLimit <= 0) {
			this.distanceLimit = this.beginningLocation.dst(this.endingLocation);
		}
		return this;
	}

	/**
	 * Sets the limit to the distance this projectile can travel. If this value is
	 * <= 0, then it will only travel to the ending location of this projectile.
	 * 
	 * @param distanceLimit the travel distance limit
	 * @return this instance for chaining
	 */
	public Projectile distanceLimit(float distanceLimit) {
		this.distanceLimit = distanceLimit;

		if (Objects.isNull(tracker) && this.distanceLimit <= 0) {
			this.distanceLimit = this.beginningLocation.dst(this.endingLocation);
		}
		return this;
	}

	/**
	 * Sets the speed at which this projectile tiles.
	 * 
	 * @param speed the projectile speed
	 * @return this instance for chaining
	 */
	public Projectile speed(float speed) {
		this.speed = speed;
		return this;
	}

	/**
	 * Sets the sprite to render to this projectile and returns this instance for
	 * chaining.
	 * 
	 * @param sprite the sprite to set
	 * @return this instance for chaining
	 */
	public Projectile sprite(Sprite sprite) {
		this.sprite = sprite;
		return this;
	}

	/**
	 * Renders this projectile's sprite to the given batch.
	 * 
	 * @param batch the batch to draw with
	 */
	public void render(SpriteBatch batch) {
		if (this.direction.x == 0 && this.direction.y == 0) return;

		if (Objects.nonNull(this.sprite)) {
			this.sprite.setPosition(position.x, position.y);
			this.sprite.setRotation(angle);
			this.sprite.draw(batch);
		}
	}

	/**
	 * Updates this projectile's movement. If the distance this projectile travels
	 * is greater than its limit, then the projectile is reset so it can be
	 * destroyed and reused from its pool.
	 */
	public void update(float delta) {
		if (this.direction.x == 0 && this.direction.y == 0) return;

		if (Objects.nonNull(this.tracker)) {
			if (!Objects.equals(this.tracker.getPosition(), this.endingLocation)) { // check if the tracker's position has changed
				this.beginning(this.position);
				this.ending(this.tracker.getPosition()); // set the new ending location to the tracker's position
			}
		}

		float minX = Math.min(this.endingLocation.x, this.beginningLocation.x);
		float minY = Math.min(this.endingLocation.y, this.beginningLocation.y);
		float maxX = Math.max(this.endingLocation.x, this.beginningLocation.x);
		float maxY = Math.max(this.endingLocation.y, this.beginningLocation.y);

		this.position.add(direction.x * delta * speed, direction.y * delta * speed);
		this.position.set(MathUtils.clamp(position.x, minX, maxX), MathUtils.clamp(position.y, minY, maxY)); // this will clamp the position to the ending location

		if (Objects.equals(this.position, this.endingLocation)) { // check if the position of this projectile is at the ending location, if so then reset it
			this.reset();
			return;
		}

		if (this.distanceLimit > 0 && this.beginningLocation.dst(this.position) >= this.distanceLimit) { // check if the projectile's location has past its distance limit, if so, then reset it
			this.reset();
			return;
		}
	}

	/**
	 * Returns the angle from the starting vector to the ending vector.
	 * 
	 * @param start the starting location
	 * @param end   the ending location
	 * @return the angle in float type
	 */
	private float getAngle(Vector2 start, Vector2 end) {
		return (float) (Math.atan2(end.y - start.y, end.x - start.x) * (180 / Math.PI));
	}

	/**
	 * Resets the object for reuse. Object references should be nulled and fields
	 * may be set to default values.
	 */
	public void reset() {
		this.sprite = null;
		this.beginningLocation.set(0, 0);
		this.position.set(0, 0);
		this.direction.set(0, 0);
	}

	/**
	 * Returns true if this projectile can be destroyed. This is true if the
	 * position coordinates are equal to 0.
	 * 
	 * @return true if can be destroyed; return false otherwise
	 */
	public boolean canBeDestroyed() {
		return position.x == 0 && position.y == 0;
	}

}
