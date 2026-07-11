package dev.kirillathome.kahmod.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class KahmodSoundsProvider extends FabricSoundsProvider {
    public KahmodSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.@NonNull Provider registryLookup, @NonNull SoundExporter exporter) {

    }

    @Override
    public @NonNull String getName() {
        return "KahmodSoundsProvider";
    }
}
