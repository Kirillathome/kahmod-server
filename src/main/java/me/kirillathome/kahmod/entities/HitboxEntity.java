package me.kirillathome.kahmod.entities;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HitboxEntity extends Entity implements PolymerEntity {

    private Box hitbox;
    public HitboxEntity(EntityType<?> variant, World world) {
        super(variant, world);
        hitbox = new Box(0, 0, 0, 0, 0, 0);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.ARMOR_STAND;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
    public Box getHitbox() {
        return hitbox;
    }

    public void setHitbox(Box hitbox) {
        this.hitbox = hitbox;
    }
}
