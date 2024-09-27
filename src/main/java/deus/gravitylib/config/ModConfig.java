package deus.gravitylib.config;

import turniplabs.halplibe.util.ConfigHandler;

import java.util.Properties;

import static deus.gravitylib.main.MOD_ID;


public class ModConfig {

	private static ConfigHandler config;

	public int getInt(String prop) {
		return config.getInt(prop);
	}
	public float getFloat(String prop) {
		return Float.parseFloat(config.getString(prop));
	}
	public double getDouble(String prop) {
		return Double.parseDouble(config.getString(prop));
	}
	public boolean getBool(String prop) {return  Boolean.parseBoolean(config.getString(prop));}

	public ModConfig() {

		Properties prop = new Properties();

		prop.setProperty("overworld.y_gravity_scale.value","1.0");
		prop.setProperty("overworld.entity_jump_force.value","0.42");

		config = new ConfigHandler(MOD_ID, prop);
		config.updateConfig();
	}
}
