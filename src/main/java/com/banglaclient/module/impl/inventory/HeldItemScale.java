package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 209: Held Item Scale
 * Customizes first-person main-hand and off-hand held item size independently.
 */
public final class HeldItemScale extends AbstractModule {

    private final FloatSetting mainHandScale = registerSetting(new FloatSetting(
            "main_hand_scale", "Main Hand Scale", "Size multiplier for main-hand item", 0.85f, 0.25f, 1.5f, 0.05f, "x"
    ));
    private final FloatSetting offHandScale = registerSetting(new FloatSetting(
            "off_hand_scale", "Offhand Scale", "Size multiplier for off-hand item", 0.80f, 0.25f, 1.5f, 0.05f, "x"
    ));

    public HeldItemScale() {
        super(
                "held_item_scale",
                "Held Item Scale",
                "Customizes held item size for main hand and offhand.",
                ModuleCategory.INVENTORY_ITEMS,
                false
        );
    }

    public float getMainHandScale() { return isEnabled() ? mainHandScale.getValue() : 1.0f; }
    public float getOffHandScale() { return isEnabled() ? offHandScale.getValue() : 1.0f; }
}
