package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 210: Small Items
 * Reduces the visual size of all held items and dropped ground items for a cleaner screen view.
 */
public final class SmallItems extends AbstractModule {

    private final FloatSetting globalItemScale = registerSetting(new FloatSetting(
            "global_small_item_scale", "Global Item Scale", "Uniform scale reduction for held items", 0.70f, 0.30f, 1.0f, 0.05f, "x"
    ));

    public SmallItems() {
        super(
                "small_items",
                "Small Items",
                "Reduces held item visual size for cleaner screen visibility.",
                ModuleCategory.INVENTORY_ITEMS,
                false
        );
    }

    public float getGlobalItemScale() {
        return isEnabled() ? globalItemScale.getValue() : 1.0f;
    }
}
