# GravityLib

**GravityLib** is a library for Minecraft that provides tools and methods to manage gravity within the game. This project is designed to help mod developers manipulate gravitational forces in their creations.

## Features

- **Modify global gravity**: Adjust gravity for the entire Minecraft world.
- **Entity-specific gravity**: Apply different gravitational forces to various entities.
- **Easy integration**: Simple API to integrate with existing mods.
- **Configuration options**: Flexible configuration to fine-tune gravity settings.

## Installation

1. **Download**
2. **Place** the GravityLib `.jar` file in the `mods` folder of your Minecraft directory.

## Usage

### Basic Setup

1. **Create a World Configuration**: 
   You can modify the global gravity scale and entity jump force by configuring the `World` class. These settings will apply to all entities within the world.

```java
World world = ...; // Obtain a reference to the current world
IGravityWorld gravityWorld = (IGravityWorld) world;

// Set global gravity scale
gravityWorld.gravityLib$setYGravityScale(0.5); // Example value

// Set entity jump force
gravityWorld.gravityLib$setEntityJumpForce(1.0); // Example value
```
### Basict entity setup
```java
EntityLiving livingEntity = ...; // Obtain a reference to an EntityLiving instance
ILivingEntity livingEntityAccessor = (ILivingEntity) livingEntity;

// Set custom jump force
livingEntityAccessor.gravityLib$setJumpForce(0.8); // Example value

// Check if the entity is jumping
boolean isJumping = livingEntityAccessor.gravityLib$isJumping();
```
### How to import
##### build.gradle
```gradle
repositories {
   maven { url = "https://jitpack.io" }
}
dependencies {
   modImplementation "com.github.Garkatron:GravityLib:${project.gravity_lib_version}"
}
```
##### gradle.properties
```
mod_version = release_tag_name
```
## Credits

This project builds upon ideas and code from [Lexal1's Cosmical Craft](https://github.com/Lexal1/cosmical-craft). Special thanks for the following code snippet which provided a foundation for gravity modification:

```java
@Inject(method = "moveEntityWithHeading(FF)V", at = @At("HEAD"))
private void getGravity(float moveStrafing, float moveForward, CallbackInfo cbi){
    gravityScale = 1f;
    if (world.getWorldType() instanceof ISpace){
        gravityScale = ((ISpace)world.worldType).getGravityScalar();
    }
}

@Redirect(method = "moveEntityWithHeading(FF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/EntityLiving;yd:D", opcode = Opcodes.PUTFIELD))
private void entityGravity(EntityLiving entity, double yd){ //Probably terrible way of modifying gravity by a scalar
    double offset = -(yd - this.yd);
    if ((0.021 > offset && offset > 0.019) || (0.081 > offset && offset > 0.079)){ // If falling in water or in air
        entity.yd -= offset * gravityScale;
    } else if ((-0.251 < yd && yd < -0.249)) { // Terminal velocity
        entity.yd = yd * gravityScale;
    } else { // Else regular behavior
        entity.yd = yd;
    }
}

@ModifyVariable(method = "causeFallDamage(F)V", at = @At(value = "STORE"), ordinal = 0)
private int changeFallDamage(int i){
    return (int)((i * gravityScale) - (3/gravityScale) + 3);
}
