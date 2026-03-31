package dev.kirillathome.kahmod.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/TooltipFlag;isAdvanced()Z"))
    private void injected(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.get(DataComponents.CUSTOM_DATA) != null) {
            CompoundTag nbt = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            Optional<Boolean> has = nbt.getBoolean("kahmod:soulbound");
                if (has.isPresent() && has.get()) {
                    builder.accept(Component.translatable("tooltip.kahmod.soulbound").withStyle(ChatFormatting.DARK_AQUA).withStyle(ChatFormatting.ITALIC));
                }
        }
    }
}
