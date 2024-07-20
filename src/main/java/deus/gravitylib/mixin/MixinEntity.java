package deus.gravitylib.mixin;

import deus.gravitylib.interfaces.IGravityObject;
import deus.gravitylib.interfaces.IGravityWorld;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class to add custom gravity behavior to entities.
 */
@Mixin(Entity.class)
public class MixinEntity implements IGravityObject {

	/**
	 * Unique field to store the vertical gravity scale.
	 */
	@Unique
	private double yGravityScale = 0.0;

	/**
	 * Shadow field to reference the world the entity is in.
	 */
	@Shadow
	public World world;

	/**
	 * Injects code after the constructor of the Entity class.
	 * Initializes the vertical gravity scale based on the world.
	 *
	 * @param world The world the entity is in.
	 * @param ci    The callback information.
	 */
	@Inject(method = "<init>(Lnet/minecraft/core/world/World;)V", at = @At("RETURN"), remap = false)
	public void afterConstructor(World world, CallbackInfo ci) {
		if (world != null) {
			IGravityWorld gravityWorld = (IGravityWorld) this.world;
			yGravityScale = gravityWorld.gravityLib$getYGravityScale();
		}
	}

	/**
	 * Injects code after the move method of the Entity class.
	 * Adjusts the vertical movement of the entity based on the gravity scale.
	 *
	 * @param xd The movement in the x direction.
	 * @param yd The movement in the y direction.
	 * @param zd The movement in the z direction.
	 * @param ci The callback information.
	 */
	@Inject(method = "move(DDD)V", at = @At("RETURN"), remap = false)
	public void afterMove(double xd, double yd, double zd, CallbackInfo ci) {
		Entity instance = (Entity) (Object) this;
		if (instance instanceof EntityItem) {
			instance.yd *= yGravityScale;
		}
	}

	/**
	 * Gets the current vertical gravity scale.
	 *
	 * @return The current value of the vertical gravity scale.
	 */
	@Override
	public double gravityLib$getYGravityScale() {
		return yGravityScale;
	}

	/**
	 * Sets the vertical gravity scale.
	 *
	 * @param value The new value for the vertical gravity scale.
	 */
	@Override
	public void gravityLib$setYGravityScale(double value) {
		yGravityScale = value;
	}
}
