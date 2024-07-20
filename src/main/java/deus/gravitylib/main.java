package deus.gravitylib;

import deus.gravitylib.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class main implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "gravitylib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ModConfig MOD_CONFIG = new ModConfig();
	public static final double default_overworld_y_gravity_scale = MOD_CONFIG.getDouble("overworld.y_gravity_scale.value");
	public static final double default_overworld_jump_force = MOD_CONFIG.getDouble("overworld.entity_jump_force.value");


	@Override
    public void onInitialize() {
        LOGGER.info("ExampleMod initialized.");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
