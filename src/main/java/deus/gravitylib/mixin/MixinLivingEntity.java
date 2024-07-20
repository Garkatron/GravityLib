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
 */
@Mixin(EntityLiving.class)
public class MixinLivingEntity implements IGravityObject, ILivingEntity {

	/**
	 * Unique field to store the vertical gravity scale.
	 */
	@Unique
	private double y_gravity_scale = 0.0;

	/**
	 * Unique field to store the jump force.
	 */
	@Unique
	private double jump_force = 0.0;

	/**
	 * Shadow field to reference if the entity is jumping.
	 */
	@Shadow
	protected boolean isJumping;

	/**
	 * Injects code after the constructor of the EntityLiving class.
	 * Initializes the vertical gravity scale and jump force based on the world.
	 *
	 * @param world The world the entity is in.
	 * @param ci    The callback information.
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
	 * Redirects the method to adjust the vertical movement of the entity based on the gravity scale.
	 *
	 * @param instance The living entity instance.
	 * @param yd       The original vertical movement value.
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
	 * Modifies the fall damage based on the gravity scale.
	 *
	 * @param i The original fall damage.
	 * @return The modified fall damage.
	 */
	@ModifyVariable(method = "causeFallDamage(F)V", at = @At(value = "STORE"), ordinal = 0, remap = false)
	private int changeFallDamage(int i) {
		return (int)((i * y_gravity_scale) - (3/y_gravity_scale) + 3);
	}

	/**
	 * Redirects the method to adjust the vertical movement of the entity when jumping.
	 *
	 * @param instance The living entity instance.
	 * @param value    The original vertical movement value.
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
	 * Gets the current vertical gravity scale.
	 *
	 * @return The current value of the vertical gravity scale.
	 */
	@Override
	public double gravityLib$getYGravityScale() {
		return y_gravity_scale;
	}

	/**
	 * Sets the vertical gravity scale.
	 *
	 * @param value The new value for the vertical gravity scale.
	 */
	@Override
	public void gravityLib$setYGravityScale(double value) {
		y_gravity_scale = value;
	}
}
