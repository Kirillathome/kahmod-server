package me.kirillathome.kahmod.mixin;

import me.kirillathome.kahmod.items.CustomMusicDiscItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.SoundPlayS2CPacket;
import net.minecraft.network.packet.s2c.play.SoundStopS2CPacket;
import net.minecraft.registry.Holder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.quiltmc.loader.api.minecraft.DedicatedServerOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@DedicatedServerOnly
@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxEntityMixin extends BlockEntity {

    public JukeboxEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Shadow public abstract ItemStack getStack(int slot);
    @Inject(method = "startPlaying", at = @At("TAIL"))
    private void injectStartPlaying(CallbackInfo ci) {
        if (getStack(0).getItem() instanceof CustomMusicDiscItem customMusicDiscItem){
            for (ServerPlayerEntity player : getWorld().getServer().getPlayerManager().getPlayerList()) {
                player.networkHandler.sendPacket(new SoundPlayS2CPacket(Holder.createDirect(customMusicDiscItem.getSound()), SoundCategory.RECORDS, pos.getX(), pos.getY(), pos.getZ(), 1f, 1f, world.getRandom().nextLong()));
                player.sendMessage(Text.literal("Now Playing: ").append(Text.translatable(customMusicDiscItem.getTranslationKey() + ".desc")).formatted(Formatting.AQUA), true);
            }
        }
    }
    @Inject(method = "dropDisc", at = @At("HEAD"))
    private void injectDropDisc(CallbackInfo ci) {
        if (this.world != null && !this.world.isClient){
            ItemStack itemStack = getStack(0);
            if (itemStack.getItem() instanceof CustomMusicDiscItem && world.getServer() != null){
                for (ServerPlayerEntity serverPlayer : world.getServer().getPlayerManager().getPlayerList()){
                    serverPlayer.networkHandler.sendPacket(new SoundStopS2CPacket(((CustomMusicDiscItem) itemStack.getItem()).getSound().getId(), SoundCategory.RECORDS));
                }
            }
        }
    }
}
