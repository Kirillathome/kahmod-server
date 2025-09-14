package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.CustomItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlowItemFrameEntity.class)
public class GlowItemFrameMixin extends ItemFrameEntity {
    public GlowItemFrameMixin(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getAsItemStack", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<ItemStack> cir) {
        if (isInvisible()) {
            cir.setReturnValue(new ItemStack(CustomItems.INVISIBLE_GLOW_ITEM_FRAME));
        }
    }


}
