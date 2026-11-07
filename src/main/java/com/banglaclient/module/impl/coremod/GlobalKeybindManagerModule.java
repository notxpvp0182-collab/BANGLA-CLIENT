package com.banglaclient.module.impl.coremod;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 311: Global Keybind Manager
 * Manages all module keybindings and provides a safe unbind-all reset action.
 */
public final class GlobalKeybindManagerModule extends AbstractModule {

    public GlobalKeybindManagerModule() {
        super(
                "global_keybind_manager",
                "Global Keybind Manager",
                "Manages all client keybinds and conflict resolution.",
                ModuleCategory.BANGLACLIENT_CORE,
                true
        );

        registerSetting(new ActionSetting(
                "unbind_all_module_keys",
                "Clear Module Keybinds",
                "Unbind all secondary module keybinds while keeping the Click GUI key",
                "Unbind All Modules",
                () -> {
                    for (Module m : ModuleRegistry.getInstance().getKeybindCapableModules()) {
                        m.getKeybindSetting().ifPresent(s -> s.setValue(Keybind.UNBOUND));
                    }
                }
        ));
    }
}
