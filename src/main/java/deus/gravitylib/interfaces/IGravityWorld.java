package deus.gravitylib.interfaces;

/**
 * Interface for worlds that are affected by custom gravity in the system.
 * Extends IGravityObject to include methods for getting and setting the entity jump force.
 */
public interface IGravityWorld extends IGravityObject {

	/**
	 * Gets the current jump force for entities in the world.
	 *
	 * @return The current value of the entity jump force.
	 */
	double gravityLib$getEntityJumpForce();

	/**
	 * Sets the jump force for entities in the world.
	 *
	 * @param value The new value for the entity jump force.
	 */
	void gravityLib$setEntityJumpForce(double value);
}
