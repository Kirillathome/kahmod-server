package dev.kirillathome.kahmod;

import dev.kirillathome.kahmod.items.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;

public class CustomItems {
    public static final Item CRASH_STICK = new CrashStickItem(
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.EPIC)
                    .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "crash_stick")))
                    .modelId(Identifier.of("stick"))
    );
    public static final Item COPPER_HORN  = new CopperHornItem(
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "copper_horn")))
                    .modelId(Identifier.of("kahmod", "copper_horn"))
    );
    public static final Item INVISIBLE_ITEM_FRAME = new InvisibleItemFrameItem(
            EntityType.ITEM_FRAME,
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "invisible_item_frame")))
                    .modelId(Identifier.of("kahmod", "invisible_item_frame"))
    );
    public static final Item INVISIBLE_GLOW_ITEM_FRAME = new InvisibleItemFrameItem(
            EntityType.GLOW_ITEM_FRAME,
            new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "invisible_glow_item_frame")))
                    .modelId(Identifier.of("kahmod", "invisible_glow_item_frame"))
    );

    public static final Item MUSIC_DISC_TEMPLATE = new MusicDiscTemplateItem(
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "music_disc_template")))
                    .modelId(Identifier.of("kahmod", "music_disc_template"))
                    .jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of("kahmod", "smile")))
    );
    public static final Item MUSIC_DISC_BIG_SHOT = new TooltipPolymerItem(
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "music_disc_big_shot")))
                    .modelId(Identifier.of("kahmod", "music_disc_big_shot"))
                    .jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of("kahmod", "big_shot"))),
            Items.COAL,
            new ArrayList<>()
    );
    public static final Item MUSIC_DISC_LAVA_CHICKEN = new TooltipPolymerItem(
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "music_disc_lava_chicken")))
                    .modelId(Identifier.of("kahmod", "music_disc_lava_chicken"))
                    .jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of("kahmod", "lava_chicken"))),
            Items.COAL,
            new ArrayList<>()
    );
    public static final Item MUSIC_DISC_WINDSHIELDS = new TooltipPolymerItem(
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of("kahmod", "music_disc_windshields")))
                    .modelId(Identifier.of("kahmod", "music_disc_windshields"))
                    .jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of("kahmod", "windshields"))),
            Items.COAL,
            new ArrayList<>()
    );

    public static void registerClass() {
        Kahmod.LOGGER.info("Registering the custom items.");

        Registry.register(Registries.ITEM, Identifier.of("kahmod", "crash_stick"), CRASH_STICK);
        Registry.register(Registries.ITEM, Identifier.of("kahmod", "copper_horn"), COPPER_HORN);
        Registry.register(Registries.ITEM, Identifier.of("kahmod", "invisible_item_frame"), INVISIBLE_ITEM_FRAME);
        Registry.register(Registries.ITEM, Identifier.of("kahmod", "invisible_glow_item_frame"), INVISIBLE_GLOW_ITEM_FRAME);
        Registry.register(Registries.ITEM, Identifier.of("kahmod", "music_disc_template"), MUSIC_DISC_TEMPLATE);
        Registry.register(Registries.ITEM, Identifier.of("kahmod", "music_disc_big_shot"), MUSIC_DISC_BIG_SHOT);
        Registry.register(Registries.ITEM, Identifier.of("kahmod", "music_disc_lava_chicken"), MUSIC_DISC_LAVA_CHICKEN);
        Registry.register(Registries.ITEM, Identifier.of("kahmod", "music_disc_windshields"), MUSIC_DISC_WINDSHIELDS);
    }
}
