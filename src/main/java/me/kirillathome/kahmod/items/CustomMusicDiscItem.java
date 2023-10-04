package me.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class CustomMusicDiscItem extends MusicDiscItem implements PolymerItem {

    Item vanillaItem;
    Identifier modelID;
    public CustomMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, int seconds, Item vanillaItem, Identifier modelID) {
        super(comparatorOutput, sound, settings, seconds);
        this.vanillaItem = vanillaItem;
        this.modelID = modelID;
    }
    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return vanillaItem;
    }
    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.requestModel(vanillaItem, modelID).value();
    }
}
