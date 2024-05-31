package me.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import me.kirillathome.kahmod.enums.CustomSounds;
import me.kirillathome.kahmod.enums.HornMaterials;
import me.kirillathome.kahmod.util.SoundHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CopperHornItem extends Item implements PolymerItem {

    public static int DEFAULT_MODEL = PolymerResourcePackUtils.requestModel(Items.IRON_NUGGET, new Identifier("kahmod", "item/horn/template")).value();

    public CopperHornItem(Settings settings) {
        super(settings);
    }

    public static void setMaterial(ItemStack stack, HornMaterials material){
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString("material", material.toString().toLowerCase());
    }

    public static CustomSounds getSound(ItemStack stack){
        HornMaterials material = getMaterial(stack);
        if (!material.equals(HornMaterials.EMPTY)) {
            return material.getSound();
        }
        return null;
    }

    public static HornMaterials getMaterial(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("material")) {
            String material = nbt.getString("material");
            for (HornMaterials m : HornMaterials.values()) {
                if (m.toString().toLowerCase().equals(material)) {
                    return m;
                }
            }
        }
        return HornMaterials.EMPTY;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return getMaterial(itemStack).getModel();
    }
    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.IRON_NUGGET;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context){
        if (getSound(stack) != null){
            tooltip.add(Text.translatable("tooltip.copper_horn.kahmod.".concat(getMaterial(stack).toString().toLowerCase())).formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient && world.getServer() != null && getSound(itemStack) != null){
            user.getItemCooldownManager().set(this, 140);
            SoundHelper.playSound(getSound(itemStack), SoundCategory.RECORDS, user.getWorld(), user.getX(), user.getY(), user.getZ());

            return TypedActionResult.success(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }
}
