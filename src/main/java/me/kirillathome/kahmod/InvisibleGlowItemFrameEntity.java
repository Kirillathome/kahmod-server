package me.kirillathome.kahmod;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InvisibleGlowItemFrameEntity extends GlowItemFrameEntity implements PolymerEntity {

    public InvisibleGlowItemFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    public InvisibleGlowItemFrameEntity(World world, BlockPos blockPos, Direction direction) {
        super(world, blockPos, direction);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.GLOW_ITEM_FRAME;
    }

    @Override
    protected ItemStack getAsItemStack() {
        return new ItemStack(KahMod.INVISIBLE_GLOW_ITEM_FRAME_ITEM);
    }
}
