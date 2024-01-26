package me.kirillathome.kahmod;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import me.kirillathome.kahmod.entities.CustomPaintingEntity;
import me.kirillathome.kahmod.entities.HitboxEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

public class CustomEntities {
    public static final EntityType<CustomPaintingEntity> CUSTOM_PAINTING = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("kahmod", "custom_painting"),
            QuiltEntityTypeBuilder.create(SpawnGroup.MISC, CustomPaintingEntity::new)
                    .setDimensions(EntityDimensions.changing(0F, 0F))
                    .maxChunkTrackingRange(10)
                    .trackingTickInterval(Integer.MAX_VALUE)
                    .build()
    );

    public static void registerClass(){
        KahMod.LOGGER.info("Loading custom Entities");
        PolymerEntityUtils.registerType(CUSTOM_PAINTING);
    }

}
