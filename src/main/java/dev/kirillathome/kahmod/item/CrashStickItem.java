package dev.kirillathome.kahmod.item;

import dev.kirillathome.kahmod.CustomSoundEvents;
import dev.kirillathome.kahmod.util.SoundHelper;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class CrashStickItem extends Item implements PolymerItem {
    public CrashStickItem(Properties properties) {
        super(properties);
    }

//    @Override
//    public @NonNull ItemStack getDefaultInstance() {
//        ItemStack itemStack = new ItemStack(this);
//        //NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt -> nbt.putBoolean("kahmod:soulbound", true));
//        itemStack.applyComponents(DataComponentMap.builder()
//                .set(
//                        DataComponents.CUSTOM_DATA,
//                        itemStack.getComponents().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).update((nbt) -> nbt.putBoolean("kahmod:soulbound", true))
//                    )
//                .build());
//        return itemStack;
//    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.STICK;
    }

    @Override
    public void modifyClientTooltip(List<Component> tooltip, ItemStack stack, PacketContext context) {
        tooltip.add(Component.translatable("tooltip.kahmod.crash_stick"));
    }

    @Override
    public void hurtEnemy(final @NonNull ItemStack itemStack, final @NonNull LivingEntity mob, final @NonNull LivingEntity attacker) {
        if (mob instanceof ServerPlayer player) {
            player.connection.send(new ClientboundExplodePacket(
                    new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE),
                    Float.MAX_VALUE,
                    Integer.MAX_VALUE,
                    Optional.of(new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)),
                    ParticleTypes.EXPLOSION,
                    SoundEvents.GENERIC_EXPLODE,
                    WeightedList.<ExplosionParticleInfo>builder().build()
            ));
        } else {
            mob.discard();
        }

        SoundHelper.playSound(
                CustomSoundEvents.DISAPPEAR,
                SoundSource.MASTER,
                mob.level(),
                mob.getX(),
                mob.getY(),
                mob.getZ()
        );
    }
}
