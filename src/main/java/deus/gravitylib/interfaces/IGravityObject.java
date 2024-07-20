package deus.gravitylib.interfaces;

/**
 * Interface for objects that are affected by custom gravity in the system.
 * Provides methods to get and set the vertical gravity scale.
 */
public interface IGravityObject {

	/**
	 * Gets the current vertical gravity scale for the object.
	 *
	 * @return The current value of the vertical gravity scale.
	 */
	double gravityLib$getYGravityScale();

	/**
	 * Sets the vertical gravity scale for the object.
	 *
	 * @param value The new value for the vertical gravity scale.
	 */
	void gravityLib$setYGravityScale(double value);
}
