package dev.kirillathome.kahmod;

import dev.kirillathome.kahmod.item.*;
import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.Set;
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
                            .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Kahmod.identifierOf("smile")))
    );

    public static final Item MUSIC_DISC_BIG_SHOT = registerItem(
            "music_disc_big_shot",
            properties -> new TooltipPolymerItem(properties, Items.COAL, new ArrayList<>()),
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Kahmod.identifierOf("big_shot")))
    );

    public static final Item MUSIC_DISC_LAVA_CHICKEN = registerItem(
            "music_disc_lava_chicken",
            properties -> new TooltipPolymerItem(properties, Items.COAL, new ArrayList<>()),
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Kahmod.identifierOf("lava_chicken")))
    );

    public static final Item MUSIC_DISC_WINDSHIELDS = registerItem(
            "music_disc_windshields",
            properties -> new TooltipPolymerItem(properties, Items.COAL, new ArrayList<>()),
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Kahmod.identifierOf("windshields")))
    );

    // used by both recipe and tag data generators
    public static final Set<Item> MUSIC_DISCS = Set.of(
            MUSIC_DISC_TEMPLATE,
            MUSIC_DISC_BIG_SHOT,
            MUSIC_DISC_LAVA_CHICKEN,
            MUSIC_DISC_WINDSHIELDS
    );

    // creative mode tab
    public static final ResourceKey<CreativeModeTab> KAHMOD_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Kahmod.identifierOf("creative_tab")
    );
    public static final CreativeModeTab KAHMOD_CREATIVE_TAB = PolymerCreativeModeTabUtils.builder()
            .icon(() -> new ItemStack(COPPER_HORN))
            .title(Component.translatable("itemGroup.kahmod"))
            .displayItems(((parameters, output) -> {
                if (parameters.hasPermissions()) {
                    output.accept(CRASH_STICK);
                }

                output.accept(COPPER_HORN);
                output.accept(INVISIBLE_ITEM_FRAME);
                output.accept(INVISIBLE_GLOW_ITEM_FRAME);
                MUSIC_DISCS.forEach(output::accept);
            }))
            .build();


    private static Item registerItem(final String name, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Kahmod.identifierOf(name));
        Item item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    private static void registerGroupedItems() {
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(
                KAHMOD_CREATIVE_TAB_KEY,
                KAHMOD_CREATIVE_TAB
        );
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(
                creativeTab -> creativeTab.accept(CRASH_STICK)
        );
    }

    public static void registerClass() {
        Kahmod.LOGGER.info("Registering the custom items.");
        PolymerItemUtils.enableStonecutterFix();
        Kahmod.LOGGER.info("Stonecutter Fix Enabled: {}", PolymerItemUtils.isStonecutterFixEnabled());

        registerGroupedItems();
    }
}
