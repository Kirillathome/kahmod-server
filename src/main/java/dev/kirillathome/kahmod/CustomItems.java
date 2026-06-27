package dev.kirillathome.kahmod;

import dev.kirillathome.kahmod.items.*;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.function.Function;

public class CustomItems {
    public static final Item CRASH_STICK = registerItem(
            "crash_stick",
            CrashStickItem::new,
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .component(DataComponents.CUSTOM_DATA, CustomData.EMPTY.update(compoundTag -> compoundTag.putBoolean("kahmod:soulbound", true)))
    );

    public static final Item COPPER_HORN = registerItem(
            "copper_horn",
            CopperHornItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
    );

    public static final Item INVISIBLE_ITEM_FRAME = registerItem(
            "invisible_item_frame",
            p -> new InvisibleItemFrameItem(EntityTypes.ITEM_FRAME, p),
            new Item.Properties()
    );

    public static final Item INVISIBLE_GLOW_ITEM_FRAME = registerItem(
            "invisible_glow_item_frame",
            p -> new InvisibleItemFrameItem(EntityTypes.GLOW_ITEM_FRAME, p),
            new Item.Properties()
    );

    public static final Item MUSIC_DISC_TEMPLATE = registerItem(
            "music_disc_template",
            MusicDiscTemplateItem::new,
            new Item.Properties()
                    .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "smile")))
    );

    public static final Item MUSIC_DISC_BIG_SHOT = registerItem(
            "music_disc_big_shot",
            properties -> new TooltipPolymerItem(properties, Items.COAL, new ArrayList<>()),
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "big_shot")))
    );

    public static final Item MUSIC_DISC_LAVA_CHICKEN = registerItem(
            "music_disc_lava_chicken",
            properties -> new TooltipPolymerItem(properties, Items.COAL, new ArrayList<>()),
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "lava_chicken")))
    );

    public static final Item MUSIC_DISC_WINDSHIELDS = registerItem(
            "music_disc_windshields",
            properties -> new TooltipPolymerItem(properties, Items.COAL, new ArrayList<>()),
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "windshields")))
    );
    /*public static final Item CRASH_STICK = registerItem(
            new CrashStickItem(
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "crash_stick")))
                            //.modelId(Identifier.fromNamespaceAndPath("minecraft", "stick"))
            ),
            Identifier.fromNamespaceAndPath("kahmod", "crash_stick")
    );
    public static final Item COPPER_HORN = registerItem(
            new CopperHornItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "copper_horn")))
                            //.modelId(Identifier.fromNamespaceAndPath("kahmod", "copper_horn"))
            ),
            Identifier.fromNamespaceAndPath("kahmod", "copper_horn")
    );
    public static final Item INVISIBLE_ITEM_FRAME = registerItem(
            new InvisibleItemFrameItem(
                    EntityType.ITEM_FRAME,
                    new Item.Properties()
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "invisible_item_frame")))
                            //.modelId(Identifier.fromNamespaceAndPath("kahmod", "invisible_item_frame"))
            ),
            Identifier.fromNamespaceAndPath("kahmod", "invisible_item_frame")
    );
    public static final Item INVISIBLE_GLOW_ITEM_FRAME = registerItem(
            new InvisibleItemFrameItem(
                    EntityType.GLOW_ITEM_FRAME,
                    new Item.Properties()
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "invisible_glow_item_frame")))
                            //.modelId(Identifier.fromNamespaceAndPath("kahmod", "invisible_glow_item_frame"))
            ),
            Identifier.fromNamespaceAndPath("kahmod", "invisible_glow_item_frame")
    );

    public static final Item MUSIC_DISC_TEMPLATE = registerItem(
            new MusicDiscTemplateItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "music_disc_template")))
                            //.modelId(Identifier.fromNamespaceAndPath("kahmod", "music_disc_template"))
                            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "smile")))
                            //.jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "smile")))
            ),
            Identifier.fromNamespaceAndPath("kahmod", "music_disc_template")
    );
    public static final Item MUSIC_DISC_BIG_SHOT = registerItem(
            new TooltipPolymerItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "music_disc_big_shot")))
                            //.modelId(Identifier.fromNamespaceAndPath("kahmod", "music_disc_big_shot"))
                            //.jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "big_shot"))),
                            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "big_shot"))),

                    Items.COAL,
                    new ArrayList<>()
            ),
            Identifier.fromNamespaceAndPath("kahmod", "music_disc_big_shot")
    );
    public static final Item MUSIC_DISC_LAVA_CHICKEN = registerItem(
            new TooltipPolymerItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "music_disc_lava_chicken")))
                            //.modelId(Identifier.fromNamespaceAndPath("kahmod", "music_disc_lava_chicken"))
                            //.jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "lava_chicken"))),
                            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "lava_chicken"))),

                    Items.COAL,
                    new ArrayList<>()
            ),
            Identifier.fromNamespaceAndPath("kahmod", "music_disc_lava_chicken")
    );
    public static final Item MUSIC_DISC_WINDSHIELDS = registerItem(
            new TooltipPolymerItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            //.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.fromNamespaceAndPath("kahmod", "music_disc_windshields")))
                            //.modelId(Identifier.fromNamespaceAndPath("kahmod", "music_disc_windshields"))
                            //.jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "windshields"))),
                            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath("kahmod", "windshields"))),
                    Items.COAL,
                    new ArrayList<>()
            ),
            Identifier.fromNamespaceAndPath("kahmod", "music_disc_windshields")
    );*/

    private static Item registerItem(final String name, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("kahmod", name));
        Item item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

//    public static Item registerItem(Item item, Identifier id) {
//        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
//        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
//    }

    public static void registerClass() {
        Kahmod.LOGGER.info("Registering the custom items.");
        PolymerItemUtils.enableStonecutterFix();
        Kahmod.LOGGER.info("Stonecutter Fix Enabled: {}", PolymerItemUtils.isStonecutterFixEnabled());
    }
}
