package dev.kirillathome.kahmod.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.SingleStackRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SingleStackRecipe.class)
public abstract class SingleStackRecipeMixin {
    @Shadow
    @Final
    private ItemStack result;

    @Inject(method = "craft(Lnet/minecraft/recipe/input/SingleStackRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void injected(SingleStackRecipeInput singleStackRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir) {
        if (result.contains(DataComponentTypes.CUSTOM_DATA) && singleStackRecipeInput.item().contains(DataComponentTypes.CUSTOM_DATA)) {
            ItemStack patched = result.copy();

            NbtCompound nbt = singleStackRecipeInput.item().get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            Optional<Boolean> has = nbt.getBoolean("kahmod:soulbound");

            if (has.isPresent() && has.get()) {
                NbtComponent.set(DataComponentTypes.CUSTOM_DATA, patched, n -> n.putBoolean("kahmod:soulbound", true));
            }

            cir.setReturnValue(patched);
        }
    }
}
