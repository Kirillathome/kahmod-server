package dev.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import xyz.nucleoid.packettweaker.PacketContext;

public class InvisibleItemFrameItem extends ItemFrameItem implements PolymerItem {
    private final EntityType<? extends AbstractDecorationEntity> entityType;

    public InvisibleItemFrameItem(EntityType<? extends AbstractDecorationEntity> entityType, net.minecraft.item.Item.Settings settings) {
        super(entityType, settings);
        this.entityType = entityType;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.LEATHER;
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
            ItemFrameEntity abstractDecorationEntity;
            if (this.entityType == EntityType.ITEM_FRAME) {
                abstractDecorationEntity = new ItemFrameEntity(world, blockPos2, direction);
                abstractDecorationEntity.setInvisible(true);

            } else {
                if (this.entityType != EntityType.GLOW_ITEM_FRAME) {
                    return ActionResult.SUCCESS;
                }

                abstractDecorationEntity = new GlowItemFrameEntity(world, blockPos2, direction);
                abstractDecorationEntity.setInvisible(true);

            }

            NbtComponent nbtComponent = itemStack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
            if (!nbtComponent.isEmpty()) {
                EntityType.loadFromEntityNbt(world, playerEntity, abstractDecorationEntity, nbtComponent);
            }

            if (abstractDecorationEntity.canStayAttached()) {
                if (!world.isClient) {
                    abstractDecorationEntity.onPlace();
                    world.emitGameEvent(playerEntity, GameEvent.ENTITY_PLACE, abstractDecorationEntity.getPos());
                    world.spawnEntity(abstractDecorationEntity);
                }

                itemStack.decrement(1);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.CONSUME;
            }
        }
    }

    @Override
    public boolean isPolymerBlockInteraction(BlockState state, ServerPlayerEntity player, Hand hand, ItemStack stack, ServerWorld world, BlockHitResult blockHitResult, ActionResult actionResult) {
        return actionResult == ActionResult.SUCCESS;
    }
}
