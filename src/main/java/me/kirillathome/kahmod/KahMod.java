package me.kirillathome.kahmod;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.Version;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KahMod implements ModInitializer {

	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerRSMPCommand(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerStatusCommand(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerMaulCommand(dispatcher));

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
