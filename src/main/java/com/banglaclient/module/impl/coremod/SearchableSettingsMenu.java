package com.banglaclient.module.impl.coremod;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 309: Searchable Settings Menu
 * Powers instant real-time searching across all 315 modules by name, description, and category.
 */
public final class SearchableSettingsMenu extends AbstractModule {

    private final BooleanSetting includeDescriptionsInSearch = registerSetting(new BooleanSetting(
            "include_desc_in_search", "Match Descriptions", "Include module descriptions and category names in search results", true
    ));

    public SearchableSettingsMenu() {
        super(
                "searchable_settings_menu",
                "Searchable Settings Menu",
                "Searches across all 315 modules and categories in real time.",
                ModuleCategory.BANGLACLIENT_CORE,
                true
        );
    }

    public boolean isIncludeDescriptionsInSearch() {
        return isEnabled() && includeDescriptionsInSearch.isEnabled();
    }
}
