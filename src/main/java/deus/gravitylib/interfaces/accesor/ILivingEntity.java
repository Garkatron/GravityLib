package deus.gravitylib.interfaces.accesor;

import deus.gravitylib.interfaces.IGravityObject;

/**
 * Interface for living entities in the custom gravity system.
 * Provides methods to check and manipulate the jumping state of an entity,
 * as well as to modify and retrieve fall damage-related values.
 */
public interface ILivingEntity extends IGravityObject {

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

	// Getters and Setters for fall damage customization
	/*
	 * Sets the damage reduction factor for modifying fall damage.
	 *
	 * @param factor The new damage reduction factor.
	 */
	void gravityLib$setDamageReductionFactor(float factor);

	/**
	 * Gets the current damage reduction factor used to modify fall damage.
	 *
	 * @return The damage reduction factor.
	 */
	float gravityLib$getDamageReductionFactor();

	/**
	 * Sets the base reduction offset for fall damage calculations.
	 *
	 * @param offset The new base reduction offset.
	 */
	void gravityLib$setBaseReductionOffset(float offset);

	/**
	 * Gets the current base reduction offset used in fall damage calculations.
	 *
	 * @return The base reduction offset.
	 */
	float gravityLib$getBaseReductionOffset();
}
