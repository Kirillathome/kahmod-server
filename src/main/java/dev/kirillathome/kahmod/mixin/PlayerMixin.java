package dev.kirillathome.kahmod.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;
import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar {

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"))
    private void injected(Inventory instance) {

        for (int i = 0; i < instance.getContainerSize(); i++) {
            ItemStack itemStack = instance.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.get(DataComponents.CUSTOM_DATA) != null) {
                    CompoundTag nbt = Objects.requireNonNull(itemStack.get(DataComponents.CUSTOM_DATA)).copyTag();
                    Optional<Boolean> has = nbt.getBoolean("kahmod:soulbound");
                    if (has.isPresent() && has.get()) {
                        continue;
                    }
                }
                drop(itemStack, true, false);
                instance.setItem(i, ItemStack.EMPTY);
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
