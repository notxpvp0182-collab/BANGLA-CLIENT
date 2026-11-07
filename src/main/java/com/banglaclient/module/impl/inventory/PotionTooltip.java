package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;

/**
 * Module 207: Potion Tooltip
 * Displays exact potion duration, amplifier, and suspicious stew/tipped arrow effect details
 * via Minecraft 1.21.11 DataComponentTypes.POTION_CONTENTS.
 */
public final class PotionTooltip extends AbstractModule {

    private final BooleanSetting showExactSeconds = registerSetting(new BooleanSetting(
            "show_exact_seconds", "Show Exact Seconds", "Include exact duration in seconds alongside MM:SS", true
    ));

    public PotionTooltip() {
        super(
                "potion_tooltip",
                "Potion Tooltip",
                "Displays clear potion effect durations and amplifiers.",
                ModuleCategory.INVENTORY_ITEMS,
                true
        );
    }

    public boolean hasPotionContents(ItemStack stack) {
        if (!isEnabled() || stack == null || stack.isEmpty()) {
            return false;
        }
        PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        return contents != null && contents.hasEffects();
    }

    public boolean isShowExactSeconds() {
        return isEnabled() && showExactSeconds.isEnabled();
    }
}
