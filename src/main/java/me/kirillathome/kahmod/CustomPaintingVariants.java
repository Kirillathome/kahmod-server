package me.kirillathome.kahmod;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public enum CustomPaintingVariants {
    DUMB_CAT(16, 16, new Identifier("kahmod", "dumbcat")),
    BACKROOMS(16, 16, new Identifier("kahmod", "backrooms")),
    PLINK(32, 16, new Identifier("kahmod", "plink")),
    RICK(16, 32, new Identifier("kahmod", "rick"));

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
