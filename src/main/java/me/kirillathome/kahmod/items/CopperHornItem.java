package me.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.SoundPlayS2CPacket;
import net.minecraft.registry.Holder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CopperHornItem extends Item implements PolymerItem {

    public CopperHornItem(Settings settings) {
        super(settings);
    }
    public static void setSound(ItemStack stack, Identifier id){
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString("sound", id.toString());
    }

    public SoundEvent getSound(ItemStack stack){
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("sound")){
            return SoundEvent.createVariableRangeEvent(Identifier.tryParse(nbt.getString("sound")));
        }
        return null;
    }
    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.requestModel(Items.IRON_NUGGET, new Identifier("kahmod:item/copper_horn")).value();
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
            tooltip.add(Text.translatable("tooltip.copper_horn.".concat(getSound(stack).getId().toTranslationKey())).formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient && world.getServer() != null && getSound(itemStack) != null){
            user.getItemCooldownManager().set(this, 140);
            for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList()){
                player.networkHandler.sendPacket(new SoundPlayS2CPacket(Holder.createDirect(getSound(itemStack)), SoundCategory.RECORDS, user.getX(), user.getEyeY(), user.getZ(), 16, 1, world.getRandom().nextLong()));
                if (player.getName().equals(user.getName())){
                    player.networkHandler.sendPacket(new CooldownUpdateS2CPacket(Items.IRON_NUGGET, 140));
                }
            }
            return TypedActionResult.success(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }
}
