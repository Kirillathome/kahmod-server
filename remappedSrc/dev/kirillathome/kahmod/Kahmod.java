package dev.kirillathome.kahmod;

import dev.kirillathome.kahmod.config.ConfigManager;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kahmod implements ModInitializer {
	public static final String MOD_ID = "kahmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Obligatory log spam by yours truly :D");

		// Register Polymer assets
		boolean assets = PolymerResourcePackUtils.addModAssets("kahmod");
		if (assets) LOGGER.info("Successfully registered mod assets!");

		// Load config
		ConfigManager.init();

		// Register custom items
		CustomItems.registerClass();

		// Misc server shenanigans
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerRSMPCommand(dispatcher));
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerStatusCommand(dispatcher));
		ServerPlayConnectionEvents.JOIN.register(((serverPlayNetworkHandler, packetSender, minecraftServer) -> packetSender.sendPacket(new CustomPayloadS2CPacket(new BrandCustomPayload(ConfigManager.getServerConfig().customBrand)))));
	}

	public static Version getVersion(){
		ModContainer modContainer = FabricLoader.getInstance().getModContainer("kahmod").get();
		if (modContainer.getContainingMod().isPresent()) {
			return modContainer.getMetadata().getVersion();
		}
		else{
			return null;
		}
	}
}