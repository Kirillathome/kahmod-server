package me.kirillathome.kahmod;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import me.kirillathome.kahmod.config.ConfigManager;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.Version;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KahMod implements ModInitializer {

	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		boolean assets = PolymerResourcePackUtils.addModAssets("kahmod");
		if (assets) LOGGER.info("Successfully registered mod assets!");
		ConfigManager.init();
		CustomItems.registerClass();
		CustomEntities.registerClass();
	}
	public static final Logger LOGGER = LoggerFactory.getLogger("Kahmod");

	public static Version getVersion(){
		ModContainer modContainer = QuiltLoader.getModContainer("kahmod").orElse(null);
		if (modContainer != null) {
			return modContainer.metadata().version();
		}
		else{
			return Version.of("Unknown");
		}
	}
}
