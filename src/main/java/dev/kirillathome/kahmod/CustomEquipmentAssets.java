package dev.kirillathome.kahmod;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

public interface CustomEquipmentAssets {
    ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("kahmod", "equipment_asset"));

    ResourceKey<EquipmentAsset> CHAINMAIL_COPPER = createId("chainmail_copper");
    ResourceKey<EquipmentAsset> INVISIBLE = createId("invisible");

    static ResourceKey<EquipmentAsset> createId(final String name) {
        return ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath("kahmod", name));
    }
}
