package dev.kirillathome.kahmod.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// thanks awildhooman
@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Unique
    private Inventory savedInventory;

    @Inject(method = "respawn", at = @At("HEAD"))
    private void saveInventory(ServerPlayer serverPlayer, boolean keepAllPlayerData, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        savedInventory = serverPlayer.getInventory();
    }
    @ModifyVariable(method = "respawn", at = @At("TAIL"), name = "player")
    private ServerPlayer setInventory(ServerPlayer value){
//        value.getInventory().clone(savedInventory);
        Inventory newInventory = value.getInventory();
        for (int i = 0; i < savedInventory.getContainerSize(); i++) {
            newInventory.setItem(i, savedInventory.getItem(i));
        }
        return value;
    }
}
