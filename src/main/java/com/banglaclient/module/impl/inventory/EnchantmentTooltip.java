package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.ItemStack;

/**
 * Module 206: Enchantment Tooltip
 * Displays enchantment max-level indicators and clean roman/arabic numerals using
 * Minecraft 1.21.11 DataComponentTypes.ENCHANTMENTS.
 */
public final class EnchantmentTooltip extends AbstractModule {

    private final BooleanSetting showMaxLevelHint = registerSetting(new BooleanSetting(
            "show_max_level_hint", "Show Max Level (e.g. 5/5)", "Append the maximum obtainable level beside enchantments", true
    ));
    private final BooleanSetting highlightMaxedEnchants = registerSetting(new BooleanSetting(
            "highlight_maxed_enchants", "Highlight Maxed Enchants", "Tint max-level enchantments in crimson/gold", true
    ));

    public EnchantmentTooltip() {
        super(
                "enchantment_tooltip",
                "Enchantment Tooltip",
                "Displays enchantment levels and max-level indicators clearly.",
                ModuleCategory.INVENTORY_ITEMS,
                true
        );
    }

    public int getEnchantmentCount(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        ItemEnchantmentsComponent enchants = stack.get(DataComponentTypes.ENCHANTMENTS);
        return enchants != null ? enchants.getSize() : 0;
    }

    public boolean isShowMaxLevelHint() { return isEnabled() && showMaxLevelHint.isEnabled(); }
    public boolean isHighlightMaxedEnchants() { return isEnabled() && highlightMaxedEnchants.isEnabled(); }
}
