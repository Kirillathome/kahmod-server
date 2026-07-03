package dev.kirillathome.kahmod;

import dev.kirillathome.kahmod.util.AfkHandler;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.BrandPayload;

import static dev.kirillathome.kahmod.CustomCommands.config;

public class KahmodServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Kahmod.LOGGER.info("Kahmod server init.");

        ServerPlayConnectionEvents.JOIN.register(
                (serverPlayNetworkHandler, _, _) -> {
                    serverPlayNetworkHandler.send(new ClientboundCustomPayloadPacket(new BrandPayload(config.customBrand)));
                    AfkHandler.unsetAfk(serverPlayNetworkHandler.player.level(), serverPlayNetworkHandler.player, true);
        });
    }
}
