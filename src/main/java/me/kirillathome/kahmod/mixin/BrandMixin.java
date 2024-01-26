package me.kirillathome.kahmod.mixin;

import io.netty.buffer.Unpooled;
import me.kirillathome.kahmod.KahMod;
import me.kirillathome.kahmod.config.ConfigManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class BrandMixin {
    @Shadow public void sendPacket(Packet<?> packet, @Nullable PacketSendListener listener){}

    @Redirect(method = "sendPacket(Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketSendListener;)V"))
    private void onSend(ServerPlayNetworkHandler instance, Packet<?> packet, @Nullable PacketSendListener listener){
        if (packet instanceof CustomPayloadS2CPacket customPayloadS2CPacket) {
            if (customPayloadS2CPacket.getChannel().equals(CustomPayloadS2CPacket.BRAND)) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                String brand = ConfigManager.getServerConfig().customBrand;
                buf.writeString(brand);
                CustomPayloadS2CPacket patched_packet = new CustomPayloadS2CPacket(CustomPayloadS2CPacket.BRAND, buf);
                sendPacket(patched_packet, listener);
                KahMod.LOGGER.info("Patched Packet: " + patched_packet.getData().readString());
            } else {
                sendPacket(packet, listener);
            }
        }
        else {
            sendPacket(packet, listener);
        }
    }
}
