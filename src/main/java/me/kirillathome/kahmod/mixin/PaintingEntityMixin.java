package me.kirillathome.kahmod.mixin;

import me.kirillathome.kahmod.KahMod;
import me.kirillathome.kahmod.entities.CustomPaintingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(PaintingEntity.class)
public abstract class PaintingEntityMixin extends AbstractDecorationEntity {
    protected PaintingEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }
    @Unique
    @Mutable
    public CustomPaintingEntity owner;
    @Unique
    private final String nbt_owner_string = "HitboxOwner";
    @Inject(method = "onBreak", at = @At("TAIL"))
    private void onBreakInject(@Nullable Entity entity, CallbackInfo ci){
        if (owner != null) {
            owner.handleBreak(entity);
        }
    }
    @Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/painting/PaintingEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity dropItemRedirect(PaintingEntity instance, ItemConvertible itemConvertible){
        return owner != null ? instance.dropItem(Items.AIR) : instance.dropItem(Items.PAINTING);
    }
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeNbtInject(NbtCompound nbt, CallbackInfo ci){
        KahMod.LOGGER.info("Written NBT: %s".formatted(nbt));
        if (owner != null){
            nbt.putUuid(nbt_owner_string, owner.getUuid());
        }
    }
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readNbtInject(NbtCompound nbt, CallbackInfo ci){
        KahMod.LOGGER.info("Loaded NBT: %s".formatted(nbt));
        if (nbt.containsUuid(nbt_owner_string)) {
            UUID owner_uuid = nbt.getUuid(nbt_owner_string);
            KahMod.LOGGER.info("Found UUID: %s".formatted(owner_uuid));
            ServerWorld world = getWorld().getServer().getWorld(getWorld().getRegistryKey());
            Entity entity = world.getEntity(owner_uuid);
            if (entity != null){
                owner = (CustomPaintingEntity) entity;
            }
        }
    }
}
