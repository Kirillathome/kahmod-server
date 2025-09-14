package dev.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class TooltipPolymerItem extends Item implements PolymerItem {
    private final Item vanillaItem;
    private final List<Text> tooltip;

    public TooltipPolymerItem(Settings settings, Item vanillaItem, List<Text> tooltip) {
        super(settings);
        this.vanillaItem = vanillaItem;
        this.tooltip = tooltip;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return vanillaItem;
    }

    @Override
    public void modifyClientTooltip(List<Text> tooltip, ItemStack stack, PacketContext context) {
        tooltip.addAll(this.tooltip);
    }
}
