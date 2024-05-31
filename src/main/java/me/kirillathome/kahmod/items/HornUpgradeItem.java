package me.kirillathome.kahmod.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.List;

public class HornUpgradeItem extends Item implements PolymerItem {

    private static final Formatting GRAY = Formatting.GRAY;
    private static final Formatting BLUE = Formatting.BLUE;
    private static final Text INGREDIENTS = Text.translatable("item.kahmod.smithing_template.ingredients").formatted(GRAY);
    private static final Text APPLIES_TO = Text.translatable("item.kahmod.smithing_template.applies_to").formatted(GRAY);
    private static final Text HORN_UPGRADE = Text.translatable("item.kahmod.smithing_template.horn_upgrade.upgrade").formatted(GRAY);
    private static final Text HORN_UPGRADE_APPLIES_TO = Text.translatable(
            "item.kahmod.smithing_template.horn_upgrade.applies_to"
            )
            .formatted(BLUE);
    private static final Text HORN_UPGRADE_INGREDIENTS = Text.translatable(
                    "item.kahmod.smithing_template.horn_upgrade.ingredients"
            )
            .formatted(BLUE);

    private final Text appliesToText;
    private final Text ingredientsText;
    private final Text upgradeText;

    public HornUpgradeItem(
            Text appliesToText,
            Text ingredientsText,
            Text upgradeText
    ) {
        super(new QuiltItemSettings());
        this.appliesToText = appliesToText;
        this.ingredientsText = ingredientsText;
        this.upgradeText = upgradeText;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.RAW_COPPER;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.requestModel(Items.RAW_COPPER, new Identifier("kahmod:item/horn_upgrade")).value();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(this.upgradeText);
        tooltip.add(CommonTexts.EMPTY);
        tooltip.add(APPLIES_TO);
        tooltip.add(CommonTexts.space().append(this.appliesToText));
        tooltip.add(INGREDIENTS);
        tooltip.add(CommonTexts.space().append(this.ingredientsText));
    }

    public static HornUpgradeItem createHornUpgrade() {
        return new HornUpgradeItem(
                HORN_UPGRADE_APPLIES_TO,
                HORN_UPGRADE_INGREDIENTS,
                HORN_UPGRADE
        );
    }
}
