package dev.kirillathome.kahmod.datagen;

import dev.kirillathome.kahmod.CustomItems;
import dev.kirillathome.kahmod.enums.CustomSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class KahmodRecipeProvider extends FabricRecipeProvider {
    public List<Item> dyeableArmor = new ArrayList<>();

    public KahmodRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);

//        materialMap.put(Items.LEATHER, EquipmentAssets.LEATHER);
//        materialMap.put(Items.COPPER_INGOT, EquipmentAssets.COPPER); // TODO: copper chains?
//        materialMap.put(Items.IRON_INGOT, EquipmentAssets.IRON);
//        materialMap.put(Items.IRON_CHAIN, EquipmentAssets.CHAINMAIL);
//        materialMap.put(Items.GOLD_INGOT, EquipmentAssets.GOLD);
//        materialMap.put(Items.DIAMOND, EquipmentAssets.DIAMOND);
//        materialMap.put(Items.NETHERITE_INGOT, EquipmentAssets.NETHERITE);
//        materialMap.put(Items.TURTLE_SCUTE, EquipmentAssets.TURTLE_SCUTE);
        // TODO: amethyst for invisible armor?
        // note that glass panes use special handling

        dyeableArmor.add(Items.LEATHER_HELMET);
        dyeableArmor.add(Items.LEATHER_CHESTPLATE);
        dyeableArmor.add(Items.LEATHER_LEGGINGS);
        dyeableArmor.add(Items.LEATHER_BOOTS);
        dyeableArmor.add(Items.COPPER_HELMET);
        dyeableArmor.add(Items.COPPER_CHESTPLATE);
        dyeableArmor.add(Items.COPPER_LEGGINGS);
        dyeableArmor.add(Items.COPPER_BOOTS);
        dyeableArmor.add(Items.IRON_HELMET);
        dyeableArmor.add(Items.IRON_CHESTPLATE);
        dyeableArmor.add(Items.IRON_LEGGINGS);
        dyeableArmor.add(Items.IRON_BOOTS);
        dyeableArmor.add(Items.CHAINMAIL_HELMET);
        dyeableArmor.add(Items.CHAINMAIL_CHESTPLATE);
        dyeableArmor.add(Items.CHAINMAIL_LEGGINGS);
        dyeableArmor.add(Items.CHAINMAIL_BOOTS);
        dyeableArmor.add(Items.GOLDEN_HELMET);
        dyeableArmor.add(Items.GOLDEN_CHESTPLATE);
        dyeableArmor.add(Items.GOLDEN_LEGGINGS);
        dyeableArmor.add(Items.GOLDEN_BOOTS);
        dyeableArmor.add(Items.DIAMOND_HELMET);
        dyeableArmor.add(Items.DIAMOND_CHESTPLATE);
        dyeableArmor.add(Items.DIAMOND_LEGGINGS);
        dyeableArmor.add(Items.DIAMOND_BOOTS);
        dyeableArmor.add(Items.NETHERITE_HELMET);
        dyeableArmor.add(Items.NETHERITE_CHESTPLATE);
        dyeableArmor.add(Items.NETHERITE_LEGGINGS);
        dyeableArmor.add(Items.NETHERITE_BOOTS);
        // y'know? this is worse than the mixin.
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
//                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
//                HolderGetter<Item> builtInLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);

                // build all copper horn recipes
                buildCopperHorns();
                // build only the dyeing recipes for the armor
                buildDyeableArmor();
            }

            private void buildCopperHorns() {
                for (CustomSounds sounds : CustomSounds.values()) {
                    String soundPath = sounds.getId().getPath();
                    if (!soundPath.startsWith("horn.")) {
                        continue; // not a (default) horn sound
                    }

                    // create custom data tag featuring instrument data
                    CompoundTag instrumentTag = new CompoundTag();
                    instrumentTag.putString("CopperHornInstrument", soundPath);

                    // (manually) create stonecutting recipe
                    StonecutterRecipe recipe = new StonecutterRecipe(
                            new Recipe.CommonInfo(true),
                            Ingredient.of(CustomItems.COPPER_HORN),
                            new ItemStackTemplate(CustomItems.COPPER_HORN, DataComponentPatch.builder()
                                    .set(DataComponents.CUSTOM_DATA, CustomData.of(
                                            instrumentTag
                                    ))
                                    .build()
                            )
                    );

                    // resource key
                    ResourceKey<Recipe<?>> id = ResourceKey.create(
                            Registries.RECIPE,
                            Identifier.fromNamespaceAndPath("kahmod", "copper_".concat(soundPath.replace('.', '_')))
                    );

                    // advancement builder
                    RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
                    advancementBuilder.unlockedBy(getHasName(Items.GOAT_HORN), has(Items.GOAT_HORN));

                    // actually registering recipe
                    output.accept(
                            id,
                            recipe,
                            advancementBuilder.build(
                                    output,
                                    id,
                                    RecipeCategory.TOOLS
                            )
                    );
                }
            }

            private void buildDyeableArmor() {
                dyeableArmor.forEach(item -> dyedItem(item, "dyed_armor"));
            }
        };
    }

//            private void buildCosmeticArmor(HolderGetter<Item> builtInLookup) {
//                Optional<HolderSet.Named<Item>> fallbackMaterials = builtInLookup.get(
//                        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("kahmod", "cosmetic_materials"))
//                );
//                if (fallbackMaterials.isEmpty()) {
//                    return; // well shit
//                }
//
//                // chestplates
//                Optional<HolderSet.Named<Item>> chestplates = builtInLookup.get(
//                        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("kahmod", "cosmetic_armor_chest"))
//                );
//
//                Optional<HolderSet.Named<Item>> chestplateMaterials = builtInLookup.get(
//                        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("kahmod", "cosmetic_materials_chest"))
//                );

//            }
//
//            private void buildCosmeticArmorSet(List<Item> armorItems, List<Item> materialItems) {
//                for (Item armor : armorItems) {
//                    // make it dyeable
//                    dyedItem(armor, "dyed_armor");
//
//                    // ALL possible combinations
//                    for (Item material : materialItems) {
//                        Optional<Equippable> equippableTag = getEquippableTag(armor, material);
//                        if (equippableTag.isEmpty()) { // something happened...
//                            continue;
//                        }
// //                        if (material == Items.TURTLE_SCUTE && equippableTag.get().slot() != EquipmentSlot.HEAD) {
// //                            continue;
// //                        }
//                        SmithingTransformRecipe recipe = new SmithingTransformRecipe(
//                                new Recipe.CommonInfo(false),
//                                Optional.empty(),
//                                Ingredient.of(armor),
//                                Optional.of(Ingredient.of(material)),
//                                new ItemStackTemplate(armor, DataComponentPatch.builder()
//                                        .set(DataComponents.EQUIPPABLE, equippableTag.get())
//                                        .build()
//                                )
//                        );
//
//                        ResourceKey<Recipe<?>> id = ResourceKey.create(
//                                Registries.RECIPE,
//                                Identifier.fromNamespaceAndPath("kahmod", String.join(
//                                        "_",
//                                        "smith",
//                                        Objects.requireNonNull(armor.components().get(DataComponents.EQUIPPABLE)).assetId().orElse(EquipmentAssets.ELYTRA).identifier().getPath(),
//                                        equippableTag.get().slot().getName().toLowerCase(Locale.ROOT),
//                                        "to",
//                                        getItemName(material).toLowerCase(Locale.ROOT)
//                                ))
//                        );
//
//                        RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
//                        advancementBuilder.unlockedBy(getHasName(armor), has(armor));
//
//                        output.accept(
//                                id,
//                                recipe,
//                                advancementBuilder.build(
//                                        output,
//                                        id,
//                                        RecipeCategory.MISC
//                                )
//                        );
//                    }
//                }
//            }
//
//            private Optional<Equippable> getEquippableTag(Item armorItem, Item materialItem) {
//                Equippable defaultComponent = armorItem.components().get(DataComponents.EQUIPPABLE);
//                ResourceKey<EquipmentAsset> asset = materialMap.get(materialItem);
//
//                // what the fuck is this
//                if (defaultComponent == null) {
//                    return Optional.empty();
//                }
//
//                // we got a valid asset from the map
//                if (asset != null) {
//                    return Optional.of(
//                            Equippable.builder(defaultComponent.slot())
//                                    .setAsset(asset)
//                                    .build()
//                    );
//                }
//
//                // special handling for resetting the equippable component
//                if (materialItem == Items.GLASS_PANE) {
//                    return Optional.of(defaultComponent);
//                }
//
//                // still don't know wtf this is
//                return Optional.empty();
//            }
//        };
//    }

    @Override
    public @NonNull String getName() {
        return "KahmodRecipeProvider";
    }
}
