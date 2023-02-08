package me.kirillathome.kahmod;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InvisibleItemFrameEntity extends ItemFrameEntity implements PolymerEntity {


    public InvisibleItemFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    public InvisibleItemFrameEntity(World world, BlockPos pos, Direction facing) {
        super(world, pos, facing);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.ITEM_FRAME;
    }
    @Override
    protected ItemStack getAsItemStack() {
        return new ItemStack(KahMod.INVISIBLE_ITEM_FRAME_ITEM);
    }
}
