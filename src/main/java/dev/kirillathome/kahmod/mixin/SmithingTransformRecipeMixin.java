package dev.kirillathome.kahmod.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SmithingTransformRecipe.class)
public abstract class SmithingTransformRecipeMixin {

    @Inject(method = "craft(Lnet/minecraft/recipe/input/SmithingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void injected(SmithingRecipeInput smithingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir) {
//        if (smithingRecipeInput.base().isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "")))) {
//
//        }
        if (smithingRecipeInput.template().isEmpty() && smithingRecipeInput.base().contains(DataComponentTypes.EQUIPPABLE)) {
            ItemStack result = smithingRecipeInput.base().copy();

            Item material = smithingRecipeInput.addition().getItem();


            if (material == Items.LEATHER) {
                applyModel(result, EquipmentAssetKeys.LEATHER);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.IRON_INGOT) {
                applyModel(result, EquipmentAssetKeys.IRON);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.GOLD_INGOT) {
                applyModel(result, EquipmentAssetKeys.GOLD);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.CHAIN) {
                applyModel(result, EquipmentAssetKeys.CHAINMAIL);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.DIAMOND) {
                applyModel(result, EquipmentAssetKeys.DIAMOND);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.NETHERITE_INGOT) {
                applyModel(result, EquipmentAssetKeys.NETHERITE);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.PHANTOM_MEMBRANE) {
                applyModel(result, EquipmentAssetKeys.ELYTRA);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.TURTLE_SCUTE) {
                applyModel(result, EquipmentAssetKeys.TURTLE_SCUTE);
                cir.setReturnValue(result);
                return;
            }

            if (material == Items.GLASS_PANE) {
                resetModel(result);
                cir.setReturnValue(result);
                return;
            }

            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Unique
    private void applyModel(ItemStack stack, RegistryKey<EquipmentAsset> asset) {
        if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            NbtCompound nbt = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            Optional<NbtCompound> chestplate = nbt.getCompound("armored_elytra:chestplate");

            if (chestplate.isPresent()) {
                ItemStack embedded = ItemStack.CODEC.parse(NbtOps.INSTANCE, chestplate.get()).getOrThrow();
                embedded.set(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(stack.get(DataComponentTypes.EQUIPPABLE).slot()).model(asset).build());
                nbt.put("armored_elytra:chestplate", ItemStack.CODEC, embedded);
                NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt);
                return;
            }
        }

        if (stack.contains(DataComponentTypes.EQUIPPABLE)) {
            stack.set(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(stack.get(DataComponentTypes.EQUIPPABLE).slot()).model(asset).build());
        }
    }

    @Unique
    private void resetModel(ItemStack stack) {
        if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            NbtCompound nbt = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            Optional<NbtCompound> chestplate = nbt.getCompound("armored_elytra:chestplate");

            if (chestplate.isPresent()) {
                ItemStack embedded = ItemStack.CODEC.parse(NbtOps.INSTANCE, chestplate.get()).getOrThrow();
                embedded.set(DataComponentTypes.EQUIPPABLE, embedded.getDefaultComponents().get(DataComponentTypes.EQUIPPABLE));
                nbt.put("armored_elytra:chestplate", ItemStack.CODEC, embedded);
                NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt);
                return;
            }
        }

        if (stack.contains(DataComponentTypes.EQUIPPABLE)) {
            stack.set(DataComponentTypes.EQUIPPABLE, stack.getDefaultComponents().get(DataComponentTypes.EQUIPPABLE));
        }
    }
}
