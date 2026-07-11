package dev.kirillathome.kahmod.item;

import dev.kirillathome.kahmod.CustomSoundEvents;
import dev.kirillathome.kahmod.util.SoundHelper;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class CopperHornItem extends Item implements PolymerItem {
    public CopperHornItem(Properties properties) {
        super(properties);
    }

    @Override
    public void modifyClientTooltip(List<Component> tooltip, ItemStack stack, PacketContext context) {
        CustomSoundEvents instrument = getInstrument(stack);
        if (instrument != null) {
            tooltip.add(Component.translatable("tooltip.kahmod.".concat(instrument.toSoundEvent().location().getPath())).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.IRON_NUGGET;
    }

    @Override
    public @NonNull InteractionResult use(final @NonNull Level level, final @NonNull Player player, final @NonNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        CustomSoundEvents sound = getInstrument(itemStack);
        if (sound != null) {
            player.startUsingItem(hand);
            SoundHelper.playSound(sound, SoundSource.RECORDS, level, player.getX(), player.getY(), player.getZ());
            player.getCooldowns().addCooldown(itemStack, 160);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public int getUseDuration(final @NonNull ItemStack itemStack, final @NonNull LivingEntity user) {
        return 160;
    }

    @Override
    public @NonNull ItemUseAnimation getUseAnimation(final @NonNull ItemStack itemStack) {
        return ItemUseAnimation.TOOT_HORN;
    }

    private @Nullable CustomSoundEvents getInstrument(ItemStack stack) {
        CustomSoundEvents s = null;
        if (stack.get(DataComponents.CUSTOM_DATA) != null) {
            CompoundTag nbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

            Optional<String> sound = nbt.getString("CopperHornInstrument");

            if (sound.isPresent()) {
                for (CustomSoundEvents customSound : CustomSoundEvents.values()) {
                    if (customSound.toSoundEvent().location().getPath().equals(sound.get())) {
                        s = customSound;
                        break;
                    }
                }
            }
        }

        return s;
    }
}
