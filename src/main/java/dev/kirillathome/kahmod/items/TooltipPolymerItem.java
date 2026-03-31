package dev.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TooltipPolymerItem extends Item implements PolymerItem {
    private final Item vanillaItem;
    private final List<Component> tooltip;

    public TooltipPolymerItem(Properties properties, Item vanillaItem, List<Component> tooltip) {
        super(properties);
        this.vanillaItem = vanillaItem;
        this.tooltip = tooltip;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return vanillaItem;
    }

    @Override
    public void modifyClientTooltip(List<Component> tooltip, ItemStack stack, PacketContext context) {
        tooltip.addAll(this.tooltip);
    }
}
