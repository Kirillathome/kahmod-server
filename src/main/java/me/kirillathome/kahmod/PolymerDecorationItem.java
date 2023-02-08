package me.kirillathome.kahmod;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class PolymerDecorationItem extends DecorationItem implements PolymerItem {

    private final Item visualItem;
    private final String name;

    public PolymerDecorationItem(EntityType<? extends AbstractDecorationEntity> type, Item visualItem, Settings settings, String name) {
        super(type, settings);
        this.visualItem = visualItem;
        this.name = name;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.requestModel(visualItem, new Identifier("kahmod", name)).value();
    }
    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.visualItem;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipContext context, @Nullable ServerPlayerEntity player) {
        ItemStack out = PolymerItemUtils.createItemStack(itemStack, context, player);
        out.addEnchantment(Enchantments.AQUA_AFFINITY, 0);
        return out;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockPos blockPos2 = blockPos.offset(direction);
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        if (playerEntity != null && !this.canPlaceOn(playerEntity, direction, itemStack, blockPos2)) {
            return ActionResult.FAIL;
        } else {
            World world = context.getWorld();
            AbstractDecorationEntity abstractDecorationEntity;
            if (visualItem == Items.ITEM_FRAME) {
                abstractDecorationEntity = new InvisibleItemFrameEntity(world, blockPos2, direction);
                abstractDecorationEntity.setInvisible(true);
            } else if (visualItem == Items.GLOW_ITEM_FRAME){
                abstractDecorationEntity = new InvisibleGlowItemFrameEntity(world, blockPos2, direction);
                abstractDecorationEntity.setInvisible(true);
            } else{
                return ActionResult.success(world.isClient);
            }

            NbtCompound nbtCompound = itemStack.getNbt();
            if (nbtCompound != null) {
                EntityType.loadFromEntityNbt(world, playerEntity, abstractDecorationEntity, nbtCompound);
            }

            if (abstractDecorationEntity.canStayAttached()) {
                if (!world.isClient) {
                    abstractDecorationEntity.onPlace();
                    world.emitGameEvent(playerEntity, GameEvent.ENTITY_PLACE, abstractDecorationEntity.getPos());
                    world.spawnEntity(abstractDecorationEntity);
                }

                itemStack.decrement(1);
                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.CONSUME;
            }
        }
    }
    @Override
    protected boolean canPlaceOn(PlayerEntity player, Direction side, ItemStack stack, BlockPos pos) {
        return !player.world.isOutOfHeightLimit(pos) && player.canPlaceOn(pos, side, stack);
    }
}
