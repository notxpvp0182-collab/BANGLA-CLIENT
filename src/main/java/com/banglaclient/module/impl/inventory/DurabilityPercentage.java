package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.item.ItemStack;

/**
 * Module 201: Durability Percentage
 * Displays exact item durability percentage on tooltips and/or slot overlays.
 */
public final class DurabilityPercentage extends AbstractModule {

    private final BooleanSetting showExactFraction = registerSetting(new BooleanSetting(
            "show_exact_fraction", "Include Raw Points", "Show both percentage and remaining/max points (e.g., 85% [1728/2031])", true
    ));

    public DurabilityPercentage() {
        super(
                "durability_percentage",
                "Durability Percentage",
                "Displays item durability percentage clearly.",
                ModuleCategory.INVENTORY_ITEMS,
                true
        );
    }

    public String formatDurability(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.isDamageable()) {
            return "";
        }
        int max = Math.max(1, stack.getMaxDamage());
        int rem = Math.max(0, max - stack.getDamage());
        int pct = (rem * 100) / max;
        if (showExactFraction.isEnabled()) {
            return pct + "% (" + rem + "/" + max + ")";
        }
        return pct + "%";
    }
}
