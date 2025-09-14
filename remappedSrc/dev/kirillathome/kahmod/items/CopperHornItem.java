package dev.kirillathome.kahmod.items;

import dev.kirillathome.kahmod.enums.CustomSounds;
import dev.kirillathome.kahmod.util.SoundHelper;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class CopperHornItem extends Item implements PolymerItem {
    public CopperHornItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Override
    public void modifyClientTooltip(List<Text> tooltip, ItemStack stack, PacketContext context) {
        CustomSounds instrument = getInstrument(stack);
        if (instrument != null) {
            tooltip.add(Text.translatable("tooltip.kahmod.".concat(instrument.toSoundEvent().id().getPath())).formatted(Formatting.GRAY));
        }
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.IRON_NUGGET;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        CustomSounds sound = getInstrument(itemStack);
        if (sound != null) {
            user.setCurrentHand(hand);
            SoundHelper.playSound(sound, SoundCategory.RECORDS, world, user.getX(), user.getY(), user.getZ());
            user.getItemCooldownManager().set(itemStack, 160);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return ActionResult.CONSUME;
        }
        return ActionResult.FAIL;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 160;
    }

    private @Nullable CustomSounds getInstrument(ItemStack stack) {
        CustomSounds s = null;
        if (stack.get(DataComponentTypes.CUSTOM_DATA) != null) {
            NbtCompound nbt = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            String sound = nbt.getString("CopperHornInstrument");
            for (CustomSounds customSound : CustomSounds.values()) {
                if (customSound.toSoundEvent().id().getPath().equals(sound)) {
                    s = customSound;
                    break;
                }
            }
        }

        return s;
    }
}
