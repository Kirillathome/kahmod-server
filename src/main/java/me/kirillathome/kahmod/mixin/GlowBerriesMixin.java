package me.kirillathome.kahmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GlowBerriesMixin {

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir){
        if (stack.getItem() == Items.GLOW_BERRIES){
            ((LivingEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 120));
        }
    }
}
