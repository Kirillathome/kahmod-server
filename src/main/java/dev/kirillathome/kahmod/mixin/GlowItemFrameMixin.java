package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.CustomItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlowItemFrame.class)
public class GlowItemFrameMixin extends ItemFrame {
    public GlowItemFrameMixin(EntityType<? extends ItemFrame> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "getFrameItemStack", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<ItemStack> cir) {
        if (isInvisible()) {
            cir.setReturnValue(new ItemStack(CustomItems.INVISIBLE_GLOW_ITEM_FRAME));
        }
    }


}
