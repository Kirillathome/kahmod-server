package me.kirillathome.kahmod;

import me.kirillathome.kahmod.items.CopperHornItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class CustomItems {

    public static final CopperHornItem COPPER_HORN = new CopperHornItem(new QuiltItemSettings().maxCount(1));
    public static void registerClass(){
        KahMod.LOGGER.info("Loading custom Items");
        Registry.register(Registries.ITEM, new Identifier("kahmod", "copper_horn"), COPPER_HORN);
    }
}
