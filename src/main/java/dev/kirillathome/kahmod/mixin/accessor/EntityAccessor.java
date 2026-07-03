package dev.kirillathome.kahmod.mixin.accessor;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("DATA_SHARED_FLAGS_ID")
    static EntityDataAccessor<Byte> getSharedFlagsID() {
        return null;
    }

    @Accessor("FLAG_GLOWING")
    static int getFlagGlowing() {
        return 6;
    }

}
