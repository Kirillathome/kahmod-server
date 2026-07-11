package dev.kirillathome.kahmod.datagen;

import dev.kirillathome.kahmod.CustomItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class KahmodItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public KahmodItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {
        Stream.Builder<ResourceKey<Item>> discs = Stream.builder();

        CustomItems.MUSIC_DISCS.forEach(disc -> {
            Optional<ResourceKey<Item>> itemKey = BuiltInRegistries.ITEM.getResourceKey(disc);
            // this looks cursed, thank you, IntelliJ IDEA Community Edition...?
            itemKey.ifPresent(discs);
        });

        builder(ItemTags.CREEPER_DROP_MUSIC_DISCS).addAll(discs.build());
    }
}
