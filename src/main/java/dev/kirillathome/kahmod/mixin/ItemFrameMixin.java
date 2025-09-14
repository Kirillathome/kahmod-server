package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.CustomItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public class ItemFrameMixin extends AbstractDecorationEntity {
    protected ItemFrameMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getAsItemStack", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<ItemStack> cir) {
        if (isInvisible()) {
            cir.setReturnValue(new ItemStack(CustomItems.INVISIBLE_ITEM_FRAME));
        }
    }

    @Shadow
    @Override
    protected Box calculateBoundingBox(BlockPos pos, Direction side) {

        return null;
    }

    @Shadow
    @Override
    public void onPlace() {

    }

    @Shadow
    @Override
    public void onBreak(ServerWorld world, @Nullable Entity breaker) {

    }
}
