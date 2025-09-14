package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.Kahmod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.ArmorDyeRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(ArmorDyeRecipe.class)
public abstract class ArmorDyeRecipeMixin {
    @Inject(method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void injected(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir) {
        List<DyeItem> list = new ArrayList<>();
        ItemStack itemStack = ItemStack.EMPTY;

        for (int i = 0; i < craftingRecipeInput.size(); i++) {
            ItemStack itemStack2 = craftingRecipeInput.getStackInSlot(i);
            if (!itemStack2.isEmpty()) {
                if (itemStack2.isIn(ItemTags.DYEABLE)) {
                    if (!itemStack.isEmpty()) {
                        cir.setReturnValue(ItemStack.EMPTY);
                        return;
                    }

                    itemStack = itemStack2.copy();
                } else {
                    if (itemStack2.getItem() instanceof DyeItem dyeItem) {
                        list.add(dyeItem);
                    }

                }
            }
        }

        if (!itemStack.isEmpty() && !list.isEmpty()) {
            if (itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
                NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                Optional<NbtCompound> chestplate = nbt.getCompound("armored_elytra:chestplate");

                if (chestplate.isPresent()) {

                    ItemStack embedded = ItemStack.CODEC.parse(NbtOps.INSTANCE, chestplate.get()).getOrThrow();
                    //embedded.set(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(stack.get(DataComponentTypes.EQUIPPABLE).slot()).model(asset).build());
                    embedded = DyedColorComponent.setColor(embedded, list);
                    nbt.put("armored_elytra:chestplate", ItemStack.CODEC, embedded);
                    NbtComponent.set(DataComponentTypes.CUSTOM_DATA, itemStack, nbt);
                    itemStack = DyedColorComponent.setColor(itemStack, list);
                    cir.setReturnValue(itemStack);
                }
            }
        }
    }
}
