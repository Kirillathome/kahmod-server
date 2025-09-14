package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.ServerMetadata;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(ServerMetadata.class)
public class ServerMetadataMixin {
    @Inject(method = "description", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Text> cir){
        Text patched_description = ConfigManager.getServerConfig().getRandomMotd();
        cir.setReturnValue(patched_description);
    }
}
