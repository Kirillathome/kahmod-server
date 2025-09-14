package dev.kirillathome.kahmod.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("TAIL"))
    private void injected(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type, CallbackInfo ci) {

        if (stack.get(DataComponentTypes.CUSTOM_DATA) != null) {
            NbtCompound nbt = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            Optional<Boolean> has = nbt.getBoolean("kahmod:soulbound");
                if (has.isPresent() && has.get()) {
                    textConsumer.accept(Text.translatable("tooltip.kahmod.soulbound").formatted(Formatting.DARK_AQUA).formatted(Formatting.ITALIC));
                }
        }
    }
}
