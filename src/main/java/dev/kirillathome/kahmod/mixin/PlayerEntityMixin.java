package dev.kirillathome.kahmod.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Redirect(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void injected(PlayerInventory instance) {

        for (int i = 0; i < instance.size(); i++) {
            ItemStack itemStack = instance.getStack(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.get(DataComponentTypes.CUSTOM_DATA) != null) {
                    NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                    Optional<Boolean> has = nbt.getBoolean("kahmod:soulbound");
                    if (has.isPresent() && has.get()) {
                        continue;
                    }
                }

                // vanilla logic
                instance.player.dropItem(itemStack, true, false);
                instance.setStack(i, ItemStack.EMPTY);

            }
        }
    }

//    @Redirect(method = "dropAll", at = @At(value = 'INVOKE', target = ""))
//    private Object injected(List<Object> instance, int i) {
//        ItemStack stack = (ItemStack) instance.get(i);
//        if (stack.get(DataComponentTypes.CUSTOM_DATA) != null) {
//            NbtCompound nbt = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
//            if (nbt.getBoolean("soulbound")) {
//                return ItemStack.EMPTY;
//            }
//        }
//        return instance.get(i);
//    }

}
