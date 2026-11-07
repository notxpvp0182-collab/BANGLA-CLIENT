package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Module 273: Favorite Settings
 * Collects and provides quick access to frequently used settings marked as favorites.
 */
public final class FavoriteSettings extends AbstractModule {

    public FavoriteSettings() {
        super(
                "favorite_settings",
                "Favorite Settings",
                "Collects frequently used settings marked as favorites.",
                ModuleCategory.CLIENT_SETTINGS,
                true
        );
    }

    public List<Setting<?>> getAllFavorites() {
        List<Setting<?>> list = new ArrayList<>();
        for (Module m : ModuleRegistry.getInstance().getAllModules()) {
            for (Setting<?> s : m.getSettings()) {
                if (s.isFavorite()) {
                    list.add(s);
                }
            }
        }
        return list;
    }
}
