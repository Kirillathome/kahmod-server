package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.CustomItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public class ItemFrameMixin extends Entity {
    public ItemFrameMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "getAsItemStack", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<ItemStack> cir) {
        if (isInvisible()) {
            cir.setReturnValue(new ItemStack(CustomItems.INVISIBLE_ITEM_FRAME));
        }
    }

    @Shadow
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Shadow
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Shadow
    public void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Shadow
    public void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
