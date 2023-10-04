package me.kirillathome.kahmod.mixin;

import me.kirillathome.kahmod.items.CustomMusicDiscItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.SoundStopS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.loader.api.minecraft.DedicatedServerOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.JukeboxBlock.HAS_RECORD;

@DedicatedServerOnly
@Mixin(JukeboxBlock.class)
public abstract class JukeboxMixin extends Block {
    public JukeboxMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onUse", at = @At("HEAD"))
    private void injectOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (state.get(HAS_RECORD)){
            BlockEntity var8 = world.getBlockEntity(pos);
            if (var8 instanceof JukeboxBlockEntity jukeboxBlockEntity) {
                ItemStack itemStack = jukeboxBlockEntity.getItem();
                if (itemStack.getItem() instanceof CustomMusicDiscItem && world.getServer() != null){
                    for (ServerPlayerEntity serverPlayer : world.getServer().getPlayerManager().getPlayerList()){
                        serverPlayer.networkHandler.sendPacket(new SoundStopS2CPacket(((CustomMusicDiscItem) itemStack.getItem()).getSound().getId(), SoundCategory.RECORDS));
                    }
                }
            }
        }
    }
}
