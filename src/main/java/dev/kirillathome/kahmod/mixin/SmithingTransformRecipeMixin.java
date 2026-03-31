package dev.kirillathome.kahmod.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SmithingTransformRecipe.class)
public abstract class SmithingTransformRecipeMixin {

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void injected(SmithingRecipeInput input, CallbackInfoReturnable<ItemStack> cir) {
//        if (input.base().isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "")))) {
//
//        }
        if (input.template().isEmpty() && input.base().has(DataComponents.EQUIPPABLE)) {
            ItemStack result = input.base().copy();

            Item material = input.addition().getItem();


            if (material == Items.LEATHER) {
                applyModel(result, EquipmentAssets.LEATHER);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.IRON_INGOT) {
                applyModel(result, EquipmentAssets.IRON);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.COPPER_INGOT) {
                applyModel(result, EquipmentAssets.COPPER);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.GOLD_INGOT) {
                applyModel(result, EquipmentAssets.GOLD);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.IRON_CHAIN) {
                applyModel(result, EquipmentAssets.CHAINMAIL);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.DIAMOND) {
                applyModel(result, EquipmentAssets.DIAMOND);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.NETHERITE_INGOT) {
                applyModel(result, EquipmentAssets.NETHERITE);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.PHANTOM_MEMBRANE) {
                applyModel(result, EquipmentAssets.ELYTRA);
                cir.setReturnValue(result);
                return;
            }
            if (material == Items.TURTLE_SCUTE) {
                applyModel(result, EquipmentAssets.TURTLE_SCUTE);
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
    private void applyModel(ItemStack stack, ResourceKey<EquipmentAsset> asset) {
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            CompoundTag nbt = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            Optional<CompoundTag> chestplate = nbt.getCompound("armored_elytra:chestplate");

            if (chestplate.isPresent()) {
                // FIXME: reimplement elytra thing
                ItemStack embedded = ItemStack.CODEC.parse(NbtOps.INSTANCE, chestplate.get()).getOrThrow();

//                embedded.set(DataComponents.EQUIPPABLE, EquippableComponent.builder(stack.get(DataComponents.EQUIPPABLE).slot()).model(asset).build());
//                nbt.put("armored_elytra:chestplate", ItemStack.CODEC, embedded);
//                NbtComponent.set(DataComponents.CUSTOM_DATA, stack, nbt);
                return;
            }
        }

        if (stack.has(DataComponents.EQUIPPABLE)) {
            stack.set(DataComponents.EQUIPPABLE, Equippable.builder(stack.get(DataComponents.EQUIPPABLE).slot()).setAsset(asset).build());
        }
    }

    @Unique
    private void resetModel(ItemStack stack) {
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            CompoundTag nbt = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            Optional<CompoundTag> chestplate = nbt.getCompound("armored_elytra:chestplate");

            if (chestplate.isPresent()) {
                ItemStack embedded = ItemStack.CODEC.parse(NbtOps.INSTANCE, chestplate.get()).getOrThrow();
//                embedded.set(DataComponents.EQUIPPABLE, embedded.getDefaultComponents().get(DataComponents.EQUIPPABLE));
//                nbt.put("armored_elytra:chestplate", ItemStack.CODEC, embedded);
//                NbtComponent.set(DataComponents.CUSTOM_DATA, stack, nbt);
                return;
            }
        }

        if (stack.has(DataComponents.EQUIPPABLE)) {
            stack.set(DataComponents.EQUIPPABLE, stack.getPrototype().get(DataComponents.EQUIPPABLE));
        }
    }
}
