package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.config.ConfigManager;
import dev.kirillathome.kahmod.util.AfkHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Unique
    private boolean afk = false;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/TickThrottler;tick()V"))
    private void injected(CallbackInfo ci) {
        int timeout = ConfigManager.getServerConfig().afkTimeout;
        if (timeout <= 0) {
            return;
        }
        if (!afk) {
            if (Util.getMillis() - player.getLastActionTime() > timeout) {
                afk = true;
                AfkHandler.setAfk(player.level(), player);
            }
        } else {
            if (Util.getMillis() - player.getLastActionTime() <= timeout) {
                afk = false;
                AfkHandler.unsetAfk(player.level(), player, false);
            }
        }
    }
}
