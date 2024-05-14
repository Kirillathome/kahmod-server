package me.kirillathome.kahmod;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public enum CustomPaintingVariants {
    DUMB_CAT(16, 16, new Identifier("kahmod", "dumbcat")),
    BACKROOMS(16, 16, new Identifier("kahmod", "backrooms")),
    GARBAGE(16, 16, new Identifier("kahmod", "garbage")),
    PLINK(32, 16, new Identifier("kahmod", "plink")),
    RICK(16, 32, new Identifier("kahmod", "rick")),
    MRBEAST(48, 32, new Identifier("kahmod", "mrbeast")),
    CHIPI(48, 32, new Identifier("kahmod", "chipi")),
    CONFUSED(48, 32, new Identifier("kahmod", "confused")),
    BITE(64, 48, new Identifier("kahmod", "bite"));

    public final int width;
    public final int height;
    public final int model;
    CustomPaintingVariants(int width, int height, Identifier model) {
        this.width = width;
        this.height = height;
        model = new Identifier(model.getNamespace(), "item/painting/".concat(model.getPath()));
        this.model = PolymerResourcePackUtils.requestModel(Items.PAPER, model).value();
    }
}
