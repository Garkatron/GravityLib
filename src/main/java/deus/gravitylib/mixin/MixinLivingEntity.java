package deus.gravitylib.mixin;

import deus.gravitylib.interfaces.IGravityObject;
import deus.gravitylib.interfaces.IGravityWorld;
import deus.gravitylib.interfaces.accesor.ILivingEntity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class to add custom gravity and jump behavior to living entities.
 * This mixin modifies various aspects of entity movement and fall damage
 * by introducing a custom gravity system, allowing for more flexible control
 * of how entities behave in different gravity environments.
 */
@Mixin(EntityLiving.class)
public class MixinLivingEntity implements ILivingEntity {

	/**
	 * Unique field to store the vertical gravity scale.
	 * This value is used to modify the effect of gravity on the entity's vertical movement.
	 * A lower value reduces the effect of gravity, while a higher value increases it.
	 */
	@Unique
	private double y_gravity_scale = 0.0;

	/**
	 * Unique field to store the entity's jump force.
	 * This value determines how high the entity can jump.
	 */
	@Unique
	private double jump_force = 0.0;

	/**
	 * Shadow field to determine if the entity is currently jumping.
	 * This is referenced to apply jump behavior modifications.
	 */
	@Shadow
	protected boolean isJumping;

	/**
	 * Unique field to store the original fall damage value.
	 * This value can be modified based on gravity scale or other custom logic.
	 */
	@Unique protected int originalDamage = 1; // Default value for original fall damage.

	/**
	 * Unique field to store the damage reduction factor.
	 * This multiplier modifies the fall damage by reducing or increasing it based on a custom factor.
	 */
	@Unique protected float damageReductionFactor = 1.0f; // No damage modification by default.

	/**
	 * Unique field to store the base reduction offset.
	 * This value is used to modify the final fall damage calculation by adding or subtracting a fixed amount.
	 */
	@Unique protected float baseReductionOffset = 3.0f; // Standard base offset.

	/**
	 * Injects code after the constructor of the EntityLiving class.
	 * Initializes the vertical gravity scale and jump force for the entity based on the world it's in.
	 *
	 * @param world The world in which the entity exists.
	 * @param ci The callback information.
	 */
	@Inject(method = "<init>(Lnet/minecraft/core/world/World;)V", at = @At("RETURN"), remap = false)
	public void afterConstructor(World world, CallbackInfo ci) {
		if (world != null) {
			IGravityWorld the_world = (IGravityWorld) world;
			y_gravity_scale = the_world.gravityLib$getYGravityScale();
			jump_force = the_world.gravityLib$getEntityJumpForce();
		}
	}

	/**
	 * Redirects the method to adjust the vertical movement of the entity based on the custom gravity scale.
	 * This allows the entity to experience different fall speeds or terminal velocities based on the gravity scale.
	 *
	 * @param instance The living entity instance.
	 * @param yd The original vertical movement value (delta Y).
	 */
	@Redirect(method = "moveEntityWithHeading(FF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/EntityLiving;yd:D", opcode = Opcodes.PUTFIELD), remap = false)
	public void afterMoveEntityWithHeading(EntityLiving instance, double yd) {
		double offset = -(yd - instance.yd);
		if ((0.021 > offset && offset > 0.019) || (0.081 > offset && offset > 0.079)) {  // If falling in water or in air
			instance.yd -= offset * y_gravity_scale;
		} else { // Else regular behavior
			if ((-0.251 < yd && yd < -0.249)) {
				instance.yd = yd * y_gravity_scale; // Terminal velocity.
			} else {
				instance.yd = yd; // Regular behavior.
			}
		}
	}

	/**
	 * Modifies the fall damage based on the custom gravity scale and other modifiers.
	 * The final damage is calculated by applying the gravity scale, damage reduction factor, and base offset.
	 *
	 * @param i The original fall damage value.
	 * @return The modified fall damage value.
	 */
	@ModifyVariable(method = "causeFallDamage(F)V", at = @At(value = "STORE"), ordinal = 0, remap = false)
	private int changeFallDamage(int i) {
		return (int)((i * y_gravity_scale * damageReductionFactor) - (baseReductionOffset / y_gravity_scale) + baseReductionOffset);
	}

	/**
	 * Redirects the method to adjust the entity's vertical movement when jumping based on the custom jump force.
	 *
	 * @param instance The living entity instance.
	 * @param value The original vertical movement value (delta Y).
	 */
	@Redirect(method = "jump()V", at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/EntityLiving;yd:D", opcode = Opcodes.PUTFIELD), remap = false)
	public void atJump(EntityLiving instance, double value) {
		instance.yd = jump_force;
	}

	/**
	 * Checks if the entity is currently jumping.
	 *
	 * @return true if the entity is jumping, false otherwise.
	 */
	@Override
	public boolean gravityLib$isJumping() {
		return this.isJumping;
	}

	/**
	 * Sets the jump force for the entity.
	 *
	 * @param value The new value for the jump force.
	 */
	@Override
	public void gravityLib$setJumpForce(double value) {
		jump_force = value;
	}

	/**
	 * Gets the current jump force for the entity.
	 *
	 * @return The current value of the jump force.
	 */
	@Override
	public double gravityLib$getJumpForce() {
		return jump_force;
	}

	/**
	 * Sets the damage reduction factor for the entity.
	 * This factor modifies how much fall damage the entity takes.
	 *
	 * @param factor The new damage reduction factor.
	 */
	@Override
	public void gravityLib$setDamageReductionFactor(float factor) {
		damageReductionFactor = factor;
	}

	/**
	 * Gets the current damage reduction factor used in fall damage calculations.
	 *
	 * @return The current damage reduction factor.
	 */
	@Override
	public float gravityLib$getDamageReductionFactor() {
		return damageReductionFactor;
	}

	/**
	 * Sets the base reduction offset used in fall damage calculations.
	 * This value adjusts the fall damage by a fixed amount.
	 *
	 * @param offset The new base reduction offset.
	 */
	@Override
	public void gravityLib$setBaseReductionOffset(float offset) {
		baseReductionOffset = offset;
	}

	/**
	 * Gets the current base reduction offset used in fall damage calculations.
	 *
	 * @return The current base reduction offset.
	 */
	@Override
	public float gravityLib$getBaseReductionOffset() {
		return baseReductionOffset;
	}

	/**
	 * Gets the current vertical gravity scale.
	 * This value modifies how strongly gravity affects the entity's vertical movement.
	 *
	 * @return The current value of the vertical gravity scale.
	 */
	@Override
	public double gravityLib$getYGravityScale() {
		return y_gravity_scale;
	}

	/**
	 * Sets the vertical gravity scale for the entity.
	 * A lower value means less gravity effect, while a higher value increases the gravity effect.
	 *
	 * @param value The new value for the vertical gravity scale.
	 */
	@Override
	public void gravityLib$setYGravityScale(double value) {
		y_gravity_scale = value;
	}
}
