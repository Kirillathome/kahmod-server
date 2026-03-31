package dev.kirillathome.kahmod.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.DyeRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DyeRecipe.class)
public abstract class DyeRecipeMixin {
//    @Redirect(method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z"))
//    private boolean injectedMatches(Ingredient instance, ItemStack input) {
//        boolean matches = input.tags().anyMatch(t -> t.equals(ItemTags.HEAD_ARMOR)) ||
//                input.tags().anyMatch(t -> t.equals(ItemTags.CHEST_ARMOR))||
//                input.tags().anyMatch(t -> t.equals(ItemTags.LEG_ARMOR))||
//                input.tags().anyMatch(t -> t.equals(ItemTags.FOOT_ARMOR));
//        if (matches) {
//            return true;
//        }
//        return instance.test(input);
//    }
//
//    @Redirect(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z"))
//    private boolean injectedAssemble(Ingredient instance, ItemStack input) {
//        boolean matches = input.tags().anyMatch(t -> t.equals(ItemTags.HEAD_ARMOR)) ||
//                input.tags().anyMatch(t -> t.equals(ItemTags.CHEST_ARMOR))||
//                input.tags().anyMatch(t -> t.equals(ItemTags.LEG_ARMOR))||
//                input.tags().anyMatch(t -> t.equals(ItemTags.FOOT_ARMOR));
//        if (matches) {
//            return true;
//        }
//        return instance.test(input);
//    }
//
//    @Redirect(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/TransmuteRecipe;createWithOriginalComponents(Lnet/minecraft/world/item/ItemStackTemplate;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
//    private ItemStack injectedCreate(ItemStackTemplate target, ItemStack input) {
//        return input;
//    }
//    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
//    private void injected(CraftingInput input, CallbackInfoReturnable<ItemStack> cir) {
//        List<DyeItem> list = new ArrayList<>();
//        ItemStack itemStack = ItemStack.EMPTY;
//
//        for (int i = 0; i < input.size(); i++) {
//            ItemStack itemStack2 = input.getItem(i);
//            if (!itemStack2.isEmpty()) {
//                if (itemStack2.tags().anyMatch(ItemTags.DY)) {
//                    if (!itemStack.isEmpty()) {
//                        cir.setReturnValue(ItemStack.EMPTY);
//                        return;
//                    }
//
//                    itemStack = itemStack2.copy();
//                } else {
//                    if (itemStack2.getItem() instanceof DyeItem dyeItem) {
//                        list.add(dyeItem);
//                    }
//
//                }
//            }
//        }
//
//        if (!itemStack.isEmpty() && !list.isEmpty()) {
//            if (itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
//                NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
//                Optional<NbtCompound> chestplate = nbt.getCompound("armored_elytra:chestplate");
//
//                if (chestplate.isPresent()) {
//
//                    ItemStack embedded = ItemStack.CODEC.parse(NbtOps.INSTANCE, chestplate.get()).getOrThrow();
//                    //embedded.set(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(stack.get(DataComponentTypes.EQUIPPABLE).slot()).model(asset).build());
//                    embedded = DyedColorComponent.setColor(embedded, list);
//                    nbt.put("armored_elytra:chestplate", ItemStack.CODEC, embedded);
//                    NbtComponent.set(DataComponentTypes.CUSTOM_DATA, itemStack, nbt);
//                    itemStack = DyedColorComponent.setColor(itemStack, list);
//                    cir.setReturnValue(itemStack);
//                }
//            }
//        }
//    }
}
