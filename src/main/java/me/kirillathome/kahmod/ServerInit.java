package me.kirillathome.kahmod;

import me.kirillathome.kahmod.config.ConfigManager;
import me.kirillathome.kahmod.listeners.BrandPacketListener;
import me.kirillathome.kahmod.listeners.ItemCraftingListener;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

public class ServerInit implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer(ModContainer mod) {
        KahMod.LOGGER.info("Initialising server-side mod content!");
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerRSMPCommand(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerStatusCommand(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CustomCommands.registerMaulCommand(dispatcher));
        ItemCraftingListener.registerListener();
        BrandPacketListener.registerListener();

    }
}
