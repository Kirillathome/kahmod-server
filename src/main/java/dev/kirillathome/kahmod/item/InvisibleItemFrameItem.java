package dev.kirillathome.kahmod.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFrameItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;


public class InvisibleItemFrameItem extends ItemFrameItem implements PolymerItem {
    private final EntityType<? extends HangingEntity> entityType;

    public InvisibleItemFrameItem(EntityType<? extends HangingEntity> entityType, Properties properties) {
        super(entityType, properties);
        this.entityType = entityType;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.LEATHER;
    }

    @Override
    public @NonNull InteractionResult useOn(final UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos blockPos = pos.relative(clickedFace);
        Player player = context.getPlayer();
        ItemStack itemInHand = context.getItemInHand();
        if (player != null && !this.mayPlace(player, clickedFace, itemInHand, blockPos)) {
            return InteractionResult.FAIL;
        } else {
            Level level = context.getLevel();
            HangingEntity entity;
            if (entityType == EntityTypes.ITEM_FRAME) {
                entity = new ItemFrame(level, blockPos, clickedFace);
                entity.setInvisible(true);
            } else {
                if (entityType != EntityTypes.GLOW_ITEM_FRAME) {
                    return InteractionResult.SUCCESS;
                }

                entity = new GlowItemFrame(level, blockPos, clickedFace);
                entity.setInvisible(true);
            }

            EntityType.createDefaultStackConfig(level, itemInHand, player).apply(entity);
            if (entity.survives()) {
                if (!level.isClientSide()) {
                    entity.playPlacementSound();
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
                    level.addFreshEntity(entity);
                }

                itemInHand.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    @Override
    public boolean isPolymerBlockInteraction(BlockState state, ServerPlayer player, InteractionHand hand, ItemStack stack, ServerLevel world, BlockHitResult blockHitResult, InteractionResult actionResult) {
        return actionResult == InteractionResult.SUCCESS;
    }
}
