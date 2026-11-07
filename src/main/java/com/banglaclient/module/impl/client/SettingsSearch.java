package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.Setting;
import com.banglaclient.setting.StringSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Module 272: Settings Search
 * Searches across all individual settings inside all 315 modules by setting name or description.
 */
public final class SettingsSearch extends AbstractModule {

    private final StringSetting query = registerSetting(new StringSetting(
            "global_setting_query", "Setting Keyword", "Keyword to search across all module settings", "", 64
    ));

    public SettingsSearch() {
        super(
                "settings_search",
                "Settings Search",
                "Searches all individual client settings across every module.",
                ModuleCategory.CLIENT_SETTINGS,
                true
        );
    }

    public List<Setting<?>> findMatchingSettings(String searchKeyword) {
        String q = (searchKeyword != null ? searchKeyword : query.getValue()).trim().toLowerCase(Locale.ROOT);
        List<Setting<?>> matches = new ArrayList<>();
        if (q.isEmpty()) {
            return matches;
        }
        for (Module m : ModuleRegistry.getInstance().getAllModules()) {
            for (Setting<?> s : m.getSettings()) {
                if (s.getName().toLowerCase(Locale.ROOT).contains(q)
                        || s.getDescription().toLowerCase(Locale.ROOT).contains(q)) {
                    matches.add(s);
                }
            }
        }
        return matches;
    }
}
