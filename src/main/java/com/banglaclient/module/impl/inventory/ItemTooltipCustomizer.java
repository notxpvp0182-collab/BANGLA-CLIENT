package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 204: Item Tooltip Customizer
 * Customizes item tooltip background, border accent color, and item registry ID display.
 */
public final class ItemTooltipCustomizer extends AbstractModule {

    private final ColorSetting tooltipBgColor = registerSetting(new ColorSetting(
            "tooltip_bg_color", "Background Color", "Tooltip background panel color", new ColorData(14, 14, 18, 240), true
    ));
    private final ColorSetting tooltipBorderColor = registerSetting(new ColorSetting(
            "tooltip_border_color", "Border Color", "Tooltip outline color", ColorData.RED_ACCENT, true
    ));
    private final BooleanSetting showItemId = registerSetting(new BooleanSetting(
            "show_item_id", "Show Registry ID", "Append item namespace ID at the bottom of tooltips", false
    ));

    public ItemTooltipCustomizer() {
        super(
                "item_tooltip_customizer",
                "Item Tooltip Customizer",
                "Customizes tooltip background, border colors, and metadata.",
                ModuleCategory.INVENTORY_ITEMS,
                true
        );
    }

    public ColorData getTooltipBgColor() { return tooltipBgColor.getValue(); }
    public ColorData getTooltipBorderColor() { return tooltipBorderColor.getValue(); }
    public boolean isShowItemId() { return isEnabled() && showItemId.isEnabled(); }
}
