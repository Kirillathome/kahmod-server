package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.config.ConfigManager;
import dev.kirillathome.kahmod.util.AfkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow
    @NotNull
    public abstract GameMode getGameMode();

    @Unique
    private boolean afk = false;
    @Unique
    private int counter = 0;

    @Inject(method = "setMovement", at = @At("HEAD"))
    private void injected(Vec3d movement, CallbackInfo ci) {
        int timeout = ConfigManager.getServerConfig().afkTimeout;
        if (timeout < 0) {
            return;
        }

        if (movement.equals(Vec3d.ZERO)) {
            counter++;
            if (counter > timeout && !afk && !getGameMode().equals(GameMode.SPECTATOR)) {
                AfkHandler.setAfk((ServerPlayerEntity) (Object) this);
                afk = true;
            }
        } else {
            counter = 0;
            if (afk) {
                AfkHandler.unsetAfk((ServerPlayerEntity) (Object) this);
                afk = false;
            }
        }
    }
}
