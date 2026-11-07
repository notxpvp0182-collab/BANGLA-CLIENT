package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

/**
 * Module 199: Rarity Highlight
 * Renders a subtle colored backdrop behind inventory items based on their Minecraft Rarity
 * (Uncommon = Yellow, Rare = Aqua, Epic = Light Purple).
 */
public final class RarityHighlight extends AbstractModule {

    private final FloatSetting backgroundAlpha = registerSetting(new FloatSetting(
            "rarity_bg_alpha", "Tint Opacity", "Opacity of the slot rarity background", 0.35f, 0.1f, 0.8f, 0.05f, ""
    ));
    private final BooleanSetting includeCommon = registerSetting(new BooleanSetting(
            "include_common_rarity", "Highlight Common Items", "Also draw a subtle gray border on Common rarity items", false
    ));

    public RarityHighlight() {
        super(
                "rarity_highlight",
                "Rarity Highlight",
                "Highlights inventory items based on Uncommon, Rare, or Epic rarity.",
                ModuleCategory.INVENTORY_ITEMS,
                false
        );
    }

    public int getRarityTintArgb(ItemStack stack) {
        if (!isEnabled() || stack == null || stack.isEmpty()) {
            return 0;
        }
        Rarity rarity = stack.getRarity();
        int alpha = Math.round(backgroundAlpha.getValue() * 255.0f) & 0xFF;
        return switch (rarity) {
            case UNCOMMON -> (alpha << 24) | 0xFFFF55;
            case RARE -> (alpha << 24) | 0x55FFFF;
            case EPIC -> (alpha << 24) | 0xFF55FF;
            case COMMON -> includeCommon.isEnabled() ? ((alpha / 2) << 24) | 0x9E9EA8 : 0;
        };
    }
}
