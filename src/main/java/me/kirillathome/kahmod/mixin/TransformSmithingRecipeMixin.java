package me.kirillathome.kahmod.mixin;

import me.kirillathome.kahmod.enums.HornMaterials;
import me.kirillathome.kahmod.items.CopperHornItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.TransformSmithingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TransformSmithingRecipe.class)
public abstract class TransformSmithingRecipeMixin {
    @Shadow @Final
    ItemStack result;

    @Inject(method = "craft", at = @At("RETURN"), cancellable = true)
    private void onCraft(Inventory inventory, DynamicRegistryManager registryManager, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = this.result.copy();
        if (result.getItem() instanceof CopperHornItem) {
            NbtCompound nbtCompound = inventory.getStack(1).getNbt();
            if (nbtCompound != null) {
               result.setNbt(nbtCompound.copy());
            }
            ItemStack addition = inventory.getStack(2);
            HornMaterials material = HornMaterials.fromItem(addition.getItem());
            if (CopperHornItem.getMaterial(inventory.getStack(1)).equals(material)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
            CopperHornItem.setMaterial(result, material);
            cir.setReturnValue(result);
        }
    }
}
