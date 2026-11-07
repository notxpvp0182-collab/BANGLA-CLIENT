package com.banglaclient.module.impl.coremod;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 308: Category-Based Settings
 * Organizes all 315 modules into the 19 structured sidebar categories.
 */
public final class CategoryBasedSettings extends AbstractModule {

    private final BooleanSetting rememberLastCategory = registerSetting(new BooleanSetting(
            "remember_last_category", "Remember Selected Category", "Preserve active sidebar category when reopening the Click GUI", true
    ));

    public CategoryBasedSettings() {
        super(
                "category_based_settings",
                "Category-Based Settings",
                "Organizes all 315 client features into 19 structured categories.",
                ModuleCategory.BANGLACLIENT_CORE,
                true
        );
    }

    public boolean isRememberLastCategory() {
        return isEnabled() && rememberLastCategory.isEnabled();
    }
}
