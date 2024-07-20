package deus.gravitylib.mixin;

import deus.gravitylib.interfaces.IGravityWorld;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static deus.gravitylib.main.*;

/**
 * Mixin class to add custom gravity behavior to the world.
 */
@Mixin(World.class)
public class MixinWorld implements IGravityWorld {

	/**
	 * Unique field to store the global gravity scale.
	 */
	@Unique
	private double gravity_scale = default_overworld_y_gravity_scale;

	/**
	 * Unique field to store the entity jump force.
	 */
	@Unique
	private double entity_jump_force = default_overworld_jump_force;

	/**
	 * Gets the current vertical gravity scale for the world.
	 *
	 * @return The current value of the vertical gravity scale.
	 */
	@Override
	public double gravityLib$getYGravityScale() {
		return gravity_scale;
	}

	/**
	 * Sets the vertical gravity scale for the world.
	 *
	 * @param value The new value for the vertical gravity scale.
	 */
	@Override
	public void gravityLib$setYGravityScale(double value) {
		gravity_scale = value;
	}

	/**
	 * Gets the current jump force for entities in the world.
	 *
	 * @return The current value of the entity jump force.
	 */
	@Override
	public double gravityLib$getEntityJumpForce() {
		return entity_jump_force;
	}

	/**
	 * Sets the jump force for entities in the world.
	 *
	 * @param value The new value for the entity jump force.
	 */
	@Override
	public void gravityLib$setEntityJumpForce(double value) {
		entity_jump_force = value;
	}
}
