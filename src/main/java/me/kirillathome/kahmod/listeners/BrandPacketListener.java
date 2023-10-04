package me.kirillathome.kahmod.listeners;

import me.kirillathome.kahmod.KahMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class BrandPacketListener {
    public static void registerListener() {
        KahMod.LOGGER.info("Registering the BrandPacketListener");
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("minecraft", "brand"), BrandPacketListener::handleBrandPacket);
    }

    private static void handleBrandPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        String brand = buf.readString();
        String name = player.getName().getString();
        if (name.equals("SouperHamster") || name.equals("zenitsuy")){
            KahMod.LOGGER.info("Suspect {} joined, checking brand!", name);
            if (!brand.equals("vanilla")){
                KahMod.LOGGER.info("Player is modded, disconnecting!");
                handler.disconnect(Text.literal("You are not allowed to use a modded client!"));
                for (ServerPlayerEntity players : server.getPlayerManager().getPlayerList()){
                    players.sendMessage(Text.literal(name).formatted(Formatting.RED).append(Text.literal(" hat versucht zu hacken, was für ein LOSER!").formatted(Formatting.YELLOW)), false);
                }
            }
            else{
                KahMod.LOGGER.info("Player seems legit.");
            }
        }
    }
}
