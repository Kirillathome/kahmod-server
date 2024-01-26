package me.kirillathome.kahmod.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DisplayEntity.ItemDisplayEntity.class)
public interface DisplayEntityAccessor {

    @Mutable
    @Accessor("STACK")
    static TrackedData<ItemStack> getStackData() {
        throw new AssertionError("Untransformed @Accessor");
    }

    @Mutable
    @Accessor("TRANSFORMATION_MODE")
    static TrackedData<Byte> getTransformationData(){
        throw new AssertionError("Untransformed @Accessor");
    }
}
