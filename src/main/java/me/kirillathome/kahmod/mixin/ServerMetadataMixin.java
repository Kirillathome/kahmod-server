package me.kirillathome.kahmod.mixin;

import me.kirillathome.kahmod.config.ConfigManager;
import net.minecraft.server.ServerMetadata;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.minecraft.DedicatedServerOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@DedicatedServerOnly
@Mixin(ServerMetadata.class)
public abstract class ServerMetadataMixin {
    @Inject(method = "description", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Text> cir){
        Text patched_description = ConfigManager.getServerConfig().getRandomMotd();
        cir.setReturnValue(patched_description);
    }
}
