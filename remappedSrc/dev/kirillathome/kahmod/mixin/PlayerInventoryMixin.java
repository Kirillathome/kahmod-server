package dev.kirillathome.kahmod.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(net.minecraft.entity.player.PlayerInventory.class)
public class PlayerInventoryMixin {
    @Redirect(method = "dropAll", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;") )
    private Object injected(List<Object> instance, int i) {
        ItemStack stack = (ItemStack) instance.get(i);
        if (stack.get(DataComponentTypes.CUSTOM_DATA) != null) {
            NbtCompound nbt = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            if (nbt.getBoolean("soulbound")) {
                return ItemStack.EMPTY;
            }
        }
        return instance.get(i);
    }
}
