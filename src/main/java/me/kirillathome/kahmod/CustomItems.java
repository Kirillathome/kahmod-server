package me.kirillathome.kahmod;

import me.kirillathome.kahmod.items.CopperHornItem;
import me.kirillathome.kahmod.items.CustomMusicDiscItem;
import me.kirillathome.kahmod.items.DummyItem;
import me.kirillathome.kahmod.items.PolymerDecorationItem;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class CustomItems {

    public static final CopperHornItem COPPER_HORN = new CopperHornItem(new QuiltItemSettings().maxCount(1));
    public static final CustomMusicDiscItem MUSIC_DISC_AMOGUS = new CustomMusicDiscItem(1, SoundEvent.createVariableRangeEvent(new Identifier("kahmod", "music_disc.amogus")), new QuiltItemSettings().maxCount(1).rarity(Rarity.RARE), 72, Items.MUSIC_DISC_CAT, new Identifier("kahmod", "item/music_disc_amogus"));
    public static final CustomMusicDiscItem MUSIC_DISC_BIGSHOT = new CustomMusicDiscItem(1, SoundEvent.createVariableRangeEvent(new Identifier("kahmod", "music_disc.mrbigshot")), new QuiltItemSettings().maxCount(1).rarity(Rarity.RARE), 127, Items.MUSIC_DISC_5, new Identifier("kahmod", "item/music_disc_mrbigshot"));
    public static final PolymerDecorationItem CUSTOM_PAINTING = new PolymerDecorationItem(CustomEntities.CUSTOM_PAINTING, new QuiltItemSettings());
    public static final DummyItem DUMMY_ITEM = new DummyItem(new QuiltItemSettings());
    public static void registerClass(){
        KahMod.LOGGER.info("Loading custom Items");
        Registry.register(Registries.ITEM, new Identifier("kahmod", "copper_horn"), COPPER_HORN);
        Registry.register(Registries.ITEM, new Identifier("kahmod", "music_disc_amogus"), MUSIC_DISC_AMOGUS);
        Registry.register(Registries.ITEM, new Identifier("kahmod", "music_disc_mrbigshot"), MUSIC_DISC_BIGSHOT);
        Registry.register(Registries.ITEM, new Identifier("kahmod", "custom_painting"), CUSTOM_PAINTING);
        Registry.register(Registries.ITEM, new Identifier("kahmod", "dummy"), DUMMY_ITEM);
    }
}
