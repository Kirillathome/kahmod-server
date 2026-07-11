package dev.kirillathome.kahmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class KahmodDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(KahmodRecipeProvider::new); // copper horns & cosmetic armor dyeing
        pack.addProvider(KahmodItemTagProvider::new); // music disc tag
    }
}
