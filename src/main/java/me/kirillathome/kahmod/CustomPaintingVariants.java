package me.kirillathome.kahmod;

public enum CustomPaintingVariants {
    DUMB_CAT(16, 16, 1),
    BACKROOMS(16, 16, 2);



    public final int width;
    public final int height;
    public final int model;
    CustomPaintingVariants(int width, int height, int model) {
        this.width = width;
        this.height = height;
        this.model = model;
    }

    /*public static final ModPaintingVariant DUMB_CAT = new ModPaintingVariant(16, 16, 1);
    public static final ModPaintingVariant BACKROOMS = new ModPaintingVariant(16, 16, 2);

    public static ModPaintingVariant fromId(int id){
        return switch (id) {
            case 1 -> DUMB_CAT;
            case 2 -> BACKROOMS;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }*/

}
