package archives.tater.blackknife;

import archives.tater.blackknife.registry.BlackKnifeItems;
import archives.tater.blackknife.registry.BlackKnifeParticles;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlackKnife implements ModInitializer {
	public static final String MOD_ID = "blackknife";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("It's Roaring time");
        // It then proceeded to roar all over the place

        BlackKnifeItems.init();
        BlackKnifeParticles.init();
	}
}