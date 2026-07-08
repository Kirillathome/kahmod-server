package dev.kirillathome.kahmod.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleItemRecipe.class)
public abstract class SingleItemRecipeMixin {

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SingleRecipeInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void injected(SingleRecipeInput input, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack item = input.item();
        CustomData inputCustomData = item.get(DataComponents.CUSTOM_DATA);
        if (inputCustomData != null) {
            ItemStack patched = cir.getReturnValue();
            CustomData outputCustomData = patched.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
//            Optional<Boolean> has = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("kahmod:soulbound");
//            if (has.isPresent()) {
//                CustomData customData = patched.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
//                patched.set(DataComponents.CUSTOM_DATA, customData.update(compoundTag -> compoundTag.putBoolean("kahmod:soulbound", has.get())));
//            }

            // merge custom_data tag instead of overriding
            patched.set(
                    DataComponents.CUSTOM_DATA,
                    inputCustomData.update(
                            compoundTag -> compoundTag.merge(outputCustomData.copyTag())
                    )
            );

            cir.setReturnValue(patched);
        }
    }
}
