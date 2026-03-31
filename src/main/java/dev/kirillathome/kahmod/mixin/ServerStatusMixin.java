package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(ServerStatus.class)
public class ServerStatusMixin {
    @Inject(method = "description", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Component> cir) {
        cir.setReturnValue(Component.literal(ConfigManager.getServerConfig().getRandomMotd()));
    }
}
