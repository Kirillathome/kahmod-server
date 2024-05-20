package me.kirillathome.kahmod;

public class CustomEntities {
    /*public static final EntityType<CustomPaintingEntity> CUSTOM_PAINTING = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("kahmod", "custom_painting"),
            QuiltEntityTypeBuilder.create(SpawnGroup.MISC, CustomPaintingEntity::new)
                    .setDimensions(EntityDimensions.changing(0F, 0F))
                    .maxChunkTrackingRange(10)
                    .trackingTickInterval(Integer.MAX_VALUE)
                    .build()
    );

    public static final EntityType<PolymerPaintingHitbox> PAINTING_HITBOX = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("kahmod", "painting_hitbox"),
            QuiltEntityTypeBuilder.<PolymerPaintingHitbox>create(SpawnGroup.MISC, PolymerPaintingHitbox::new)
                    .setDimensions(EntityDimensions.changing(0.5F, 0.5F))
                    .maxChunkTrackingRange(10)
                    .trackingTickInterval(Integer.MAX_VALUE)
                    .build()
    );*/

    public static void registerClass(){
        KahMod.LOGGER.info("Loading custom Entities");
        /*PolymerEntityUtils.registerType(CUSTOM_PAINTING);
        PolymerEntityUtils.registerType(PAINTING_HITBOX);*/
    }

}
