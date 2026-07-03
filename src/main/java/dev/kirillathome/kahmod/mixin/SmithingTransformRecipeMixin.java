package dev.kirillathome.kahmod.mixin;

import dev.kirillathome.kahmod.CustomEquipmentAssets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.WeatheringCopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(SmithingTransformRecipe.class)
public abstract class SmithingTransformRecipeMixin {
    @Unique
    private static final Map<Item, ResourceKey<EquipmentAsset>> armorMaterials;

    static {
        armorMaterials = new HashMap<>();
        armorMaterials.put(Items.LEATHER, EquipmentAssets.LEATHER);
        armorMaterials.put(Items.COPPER_INGOT, EquipmentAssets.COPPER);
        armorMaterials.put(Items.IRON_INGOT, EquipmentAssets.IRON);
        armorMaterials.put(Items.IRON_CHAIN, EquipmentAssets.CHAINMAIL);
        armorMaterials.put(Items.GOLD_INGOT, EquipmentAssets.GOLD);
        armorMaterials.put(Items.DIAMOND, EquipmentAssets.DIAMOND);
        armorMaterials.put(Items.NETHERITE_INGOT, EquipmentAssets.NETHERITE);
        armorMaterials.put(Items.TURTLE_SCUTE, EquipmentAssets.TURTLE_SCUTE);
        armorMaterials.put(Items.COPPER_CHAIN.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED), CustomEquipmentAssets.CHAINMAIL_COPPER);
        armorMaterials.put(Items.AMETHYST_SHARD, CustomEquipmentAssets.INVISIBLE);
    }

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void injected(SmithingRecipeInput input, CallbackInfoReturnable<ItemStack> cir) {
        if (input.template().isEmpty() && input.base().has(DataComponents.EQUIPPABLE)) {
            ItemStack result = input.base().copy();
            Item material = input.addition().getItem();

            ResourceKey<EquipmentAsset> asset = armorMaterials.get(material);
            if (asset != null) {
                applyModel(result, asset);
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
        if (stack.has(DataComponents.EQUIPPABLE)) {
            stack.set(DataComponents.EQUIPPABLE, Equippable.builder(Objects.requireNonNull(stack.get(DataComponents.EQUIPPABLE)).slot()).setAsset(asset).build());
        }
    }

    @Unique
    private void resetModel(ItemStack stack) {
        if (stack.has(DataComponents.EQUIPPABLE)) {
            stack.set(DataComponents.EQUIPPABLE, stack.getPrototype().get(DataComponents.EQUIPPABLE));
        }
    }
}
