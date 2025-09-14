package dev.kirillathome.kahmod.items;

import dev.kirillathome.kahmod.enums.CustomSounds;
import dev.kirillathome.kahmod.util.SoundHelper;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CrashStickItem extends Item implements PolymerItem {
    public CrashStickItem(Settings settings) {
        super(settings);

//        settings.component(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT.apply(nbt -> nbt.putBoolean("soulbound", true)));
    }

//    @Override
//    public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
//        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt -> nbt.putBoolean("soulbound", true));
//    }

    @Override
    public void postProcessComponents(ItemStack stack) {
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt -> nbt.putBoolean("kahmod:soulbound", true));
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.STICK;
    }

    @Override
    public void modifyClientTooltip(List<Text> tooltip, ItemStack stack, PacketContext context) {
        tooltip.add(Text.translatable("tooltip.kahmod.crash_stick"));
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof PlayerEntity playerTarget && playerTarget.getServer() != null) {
            Objects.requireNonNull(playerTarget.getServer().getPlayerManager().getPlayer(playerTarget.getUuid())).networkHandler.sendPacket(
                    new ExplosionS2CPacket(
                            new Vec3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE),
                            Optional.of(new Vec3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)),
                            ParticleTypes.EXPLOSION,
                            SoundEvents.ENTITY_GENERIC_EXPLODE
                    )
            );
        } else {
            target.discard();
        }

        SoundHelper.playSound(
                CustomSounds.DISAPPEAR,
                SoundCategory.MASTER,
                target.getWorld(),
                target.getX(),
                target.getY(),
                target.getZ()
        );
    }
}
