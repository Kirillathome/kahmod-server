package me.kirillathome.kahmod.mixin;

import me.kirillathome.kahmod.config.ConfigManager;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.listener.AbstractServerPacketHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.payload.BrandPayload;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractServerPacketHandler.class)
public abstract class BrandMixin{

    @Shadow public abstract void send(Packet<?> packet, @Nullable PacketSendListener listener);

    @Redirect(method = "send*", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/listener/AbstractServerPacketHandler;send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketSendListener;)V"))
    private void onSend(AbstractServerPacketHandler handler, Packet<?> packet, @Nullable PacketSendListener listener){
        if (packet instanceof CustomPayloadS2CPacket customPayloadS2CPacket) {
            if (customPayloadS2CPacket.payload() instanceof BrandPayload) {
                String brand = ConfigManager.getServerConfig().customBrand;
                CustomPayloadS2CPacket patched_packet = new CustomPayloadS2CPacket(new BrandPayload(brand));
                this.send(patched_packet, null);
                //KahMod.LOGGER.info("Patched Packet: " + patched_packet.getData().readString());
            } else {
                this.send(packet, null);
            }
        }
        else {
            this.send(packet, null);
        }
    }
}
