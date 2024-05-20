package me.kirillathome.kahmod.mixin;

import me.kirillathome.kahmod.CustomItems;
import me.kirillathome.kahmod.CustomSounds;
import me.kirillathome.kahmod.util.SoundHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ExplosionOccursS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class CrashStickMixin {
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Inject(method = "attack", at = @At("TAIL"))
    private void onAttack(Entity target, CallbackInfo ci) {
        if (getEquippedStack(EquipmentSlot.MAINHAND).getItem() == CustomItems.CRASH_STICK) {
            if (target instanceof PlayerEntity playerTarget && playerTarget.getServer() != null) {
                Objects.requireNonNull(Objects.requireNonNull(playerTarget.getServer()).getPlayerManager().getPlayer(playerTarget.getUuid())).networkHandler.send(
                        new ExplosionOccursS2CPacket(
                                Double.MAX_VALUE, // this will teach cheaters a lesson
                                Double.MAX_VALUE,
                                Double.MAX_VALUE,
                                Float.MAX_VALUE,
                                List.of(),
                                new Vec3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE),
                                Explosion.DestructionType.DESTROY,
                                ParticleTypes.EXPLOSION,
                                ParticleTypes.EXPLOSION_EMITTER,
                                SoundEvents.ENTITY_GENERIC_EXPLODE)
                );
            }
            else {
                target.discard();
            }
            SoundHelper.playSound(
                CustomSounds.DISAPPEAR,
                SoundCategory.MASTER,
                target.getWorld(),
                target.getX(),
                target.getY(),
                target.getZ(),
                1f,
                1f
            );
        }
    }
}
