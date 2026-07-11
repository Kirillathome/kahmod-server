package dev.kirillathome.kahmod.datagen;

import dev.kirillathome.kahmod.CustomItems;
import dev.kirillathome.kahmod.Kahmod;
import dev.kirillathome.kahmod.CustomSoundEvents;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
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
    public static final Set<Item> dyeableArmor = Set.of(
            Items.LEATHER_HELMET,
            Items.LEATHER_CHESTPLATE,
            Items.LEATHER_LEGGINGS,
            Items.LEATHER_BOOTS,
            Items.COPPER_HELMET,
            Items.COPPER_CHESTPLATE,
            Items.COPPER_LEGGINGS,
            Items.COPPER_BOOTS,
            Items.IRON_HELMET,
            Items.IRON_CHESTPLATE,
            Items.IRON_LEGGINGS,
            Items.IRON_BOOTS,
            Items.CHAINMAIL_HELMET,
            Items.CHAINMAIL_CHESTPLATE,
            Items.CHAINMAIL_LEGGINGS,
            Items.CHAINMAIL_BOOTS,
            Items.GOLDEN_HELMET,
            Items.GOLDEN_CHESTPLATE,
            Items.GOLDEN_LEGGINGS,
            Items.GOLDEN_BOOTS,
            Items.DIAMOND_HELMET,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_BOOTS,
            Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_BOOTS
        // y'know? this is worse than the mixin.
    );

    public KahmodRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
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

                // build music disc stonecutting recipes
                buildMusicDiscs();
            }

            private void buildCopperHorns() {
                for (CustomSoundEvents sounds : CustomSoundEvents.values()) {
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
                            Kahmod.identifierOf("copper_".concat(soundPath.replace('.', '_')))
                    );

                    // advancement builder
                    RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
                    advancementBuilder.unlockedBy(getHasName(CustomItems.COPPER_HORN), has(CustomItems.COPPER_HORN));

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

            private void buildMusicDiscs() {
                for (Item disc : CustomItems.MUSIC_DISCS) {
                    if (disc == CustomItems.MUSIC_DISC_TEMPLATE) {
                        continue;
                    }

                    StonecutterRecipe recipe = new StonecutterRecipe(
                            new Recipe.CommonInfo(true),
                            Ingredient.of(CustomItems.MUSIC_DISC_TEMPLATE),
                            new ItemStackTemplate(disc)
                    );

                    // resource key
                    ResourceKey<Recipe<?>> id = ResourceKey.create(
                            Registries.RECIPE,
                            Kahmod.identifierOf(getItemName(disc))
                    );

                    // advancement builder
                    RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
                    advancementBuilder.unlockedBy(getHasName(CustomItems.MUSIC_DISC_TEMPLATE), has(CustomItems.MUSIC_DISC_TEMPLATE));

                    // actually registering recipe
                    output.accept(
                            id,
                            recipe,
                            advancementBuilder.build(
                                    output,
                                    id,
                                    RecipeCategory.MISC
                            )
                    );
                }
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return "KahmodRecipeProvider";
    }
}
