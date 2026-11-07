package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.StringSetting;
import com.banglaclient.theme.ColorData;
import net.minecraft.item.ItemStack;

import java.util.Locale;

/**
 * Module 198: Item Highlighter
 * Highlights matching inventory and hotbar slots based on a configurable search keyword
 * (e.g., "totem", "pearl", "golden_apple", "mending").
 */
public final class ItemHighlighter extends AbstractModule {

    private final StringSetting highlightQuery = registerSetting(new StringSetting(
            "highlight_query", "Search Filter", "Item name filter to highlight in inventory slots", "totem", 48
    ));
    private final ColorSetting slotHighlightColor = registerSetting(new ColorSetting(
            "slot_highlight_color", "Highlight Color", "Background tint for matching inventory slots", new ColorData(229, 37, 33, 140), true
    ));

    public ItemHighlighter() {
        super(
                "item_highlighter",
                "Item Highlighter",
                "Highlights selected inventory/hotbar items matching your filter.",
                ModuleCategory.INVENTORY_ITEMS,
                false
        );
    }

    public boolean matchesFilter(ItemStack stack) {
        if (!isEnabled() || stack == null || stack.isEmpty()) {
            return false;
        }
        String q = highlightQuery.getValue().trim().toLowerCase(Locale.ROOT);
        if (q.isEmpty()) {
            return false;
        }
        return stack.getName().getString().toLowerCase(Locale.ROOT).contains(q);
    }

    public ColorData getSlotHighlightColor() {
        return slotHighlightColor.getValue();
    }
}
