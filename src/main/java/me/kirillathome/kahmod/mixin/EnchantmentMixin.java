package me.kirillathome.kahmod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.LoyaltyEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Unique
    private EnchantmentTarget type;
    @Unique
    private Enchantment enchantment;

    protected EnchantmentMixin() {
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(Enchantment.Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes, CallbackInfo ci) {
        this.type = type;
        this.enchantment = (Enchantment) (Object) this;
    }
    @Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
    public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (stack.getItem() instanceof HoeItem && enchantment instanceof LoyaltyEnchantment){
            cir.setReturnValue(true);
        }
        else {
            cir.setReturnValue(type.isAcceptableItem(stack.getItem()));
        }
    }
}
