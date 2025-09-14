package dev.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class MusicDiscTemplateItem extends Item implements PolymerItem {
    public MusicDiscTemplateItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.COAL;
    }

    @Override
    public void modifyClientTooltip(List<Text> tooltip, ItemStack stack, PacketContext context) {
        tooltip.removeLast();
        tooltip.add(Text.translatable("tooltip.kahmod.music_disc_template").formatted(Formatting.GRAY));
    }
}
