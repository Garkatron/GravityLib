package deus.gravitylib.interfaces.accesor;

/**
 * Interface for living entities in the custom gravity system.
 * Provides methods to check and manipulate the jumping state of an entity.
 */
public interface ILivingEntity {

	/**
	 * Checks if the entity is currently jumping.
	 *
	 * @return true if the entity is jumping, false otherwise.
	 */
	boolean gravityLib$isJumping();

	/**
	 * Sets the jump force of the entity.
	 *
	 * @param value The value of the new jump force.
	 */
	void gravityLib$setJumpForce(double value);

	/**
	 * Gets the jump force of the entity.
	 *
	 * @return The current value of the jump force.
	 */
	double gravityLib$getJumpForce();
}
