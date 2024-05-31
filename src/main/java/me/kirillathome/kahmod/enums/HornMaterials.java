package me.kirillathome.kahmod.enums;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import me.kirillathome.kahmod.items.CopperHornItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public enum HornMaterials {
    EMPTY(CustomSounds.DISAPPEAR),
    EMERALD(CustomSounds.HORN_YIPPIE, PolymerResourcePackUtils.requestModel(Items.IRON_NUGGET, new Identifier("kahmod", "item/horn/emerald")).value()),
    GUNPOWDER(CustomSounds.HORN_VINE_BOOM, PolymerResourcePackUtils.requestModel(Items.IRON_NUGGET, new Identifier("kahmod", "item/horn/creeper")).value()),
    RED_DYE(CustomSounds.HORN_SUS, PolymerResourcePackUtils.requestModel(Items.IRON_NUGGET, new Identifier("kahmod", "item/horn/red")).value()),
    GLASS_PANE(CustomSounds.HORN_GASTER, PolymerResourcePackUtils.requestModel(Items.IRON_NUGGET, new Identifier("kahmod", "item/horn/gaster")).value()),
    BONE(CustomSounds.HORN_BONE, PolymerResourcePackUtils.requestModel(Items.IRON_NUGGET, new Identifier("kahmod", "item/horn/bone")).value());

    public final CustomSounds sound;
    public final int model;

    public static HornMaterials fromItem(Item item) {
        if (item.equals(Items.EMERALD)) {
            return EMERALD;
        }
        if (item.equals(Items.GUNPOWDER)) {
            return GUNPOWDER;
        }
        if (item.equals(Items.RED_DYE)) {
            return RED_DYE;
        }
        if (item.equals(Items.GLASS_PANE)) {
            return GLASS_PANE;
        }
        if (item.equals(Items.BONE)) {
            return BONE;
        }
        return EMPTY;
    }

    public CustomSounds getSound() {
        return sound;
    }

    public int getModel() {
        return model;
    }

    HornMaterials(CustomSounds sound) {
        this.sound = sound;
        this.model = CopperHornItem.DEFAULT_MODEL;
    }

    HornMaterials(CustomSounds sound, int model) {
        this.sound = sound;
        this.model = model;
    }
}
