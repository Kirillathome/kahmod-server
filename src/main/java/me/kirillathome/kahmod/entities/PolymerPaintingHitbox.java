package me.kirillathome.kahmod.entities;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import me.kirillathome.kahmod.CustomEntities;
import me.kirillathome.kahmod.CustomItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PolymerPaintingHitbox extends AbstractDecorationEntity implements PolymerEntity {

    private CustomPaintingEntity owner;

    @Override
    public int getWidthPixels() {
        return 16;
    }

    @Override
    public int getHeightPixels() {
        return 16;
    }

    @Override
    public void onPlace() {

    }

    public PolymerPaintingHitbox(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }
    public PolymerPaintingHitbox(World world, BlockPos pos, Direction direction, CustomPaintingEntity owner) {
        super(CustomEntities.PAINTING_HITBOX, world, pos);
        setFacing(direction);
        setOwner(owner);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, this.facing.getId(), this.getDecorationBlockPos());
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.PAINTING;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(CustomItems.CUSTOM_PAINTING);
    }

    @Override
    public void onBreak(@Nullable Entity entity) {
        if (owner != null){
            owner.handleBreak(entity);
        }
    }

    public void setOwner(CustomPaintingEntity owner) {
        this.owner = owner;
    }
}
