package com.banglaclient.module.impl.input;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.keybind.KeybindManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.KeybindSetting;

/**
 * Module 088: Custom Keybind System
 * Configures global Click GUI and HUD Editor shortcuts and enables per-module keybinds.
 */
public final class CustomKeybindSystem extends AbstractModule {

    private final KeybindSetting guiShortcut = registerSetting(new KeybindSetting(
            "gui_shortcut", "Click GUI Keybind", "Key to open and close the BANGLACLIENT Click GUI", KeybindManager.getInstance().getOpenGuiKeybind()
    ));
    private final KeybindSetting hudEditorShortcut = registerSetting(new KeybindSetting(
            "hud_editor_shortcut", "HUD Editor Keybind", "Key to open the free-positioning HUD Editor", KeybindManager.getInstance().getOpenHudEditorKeybind()
    ));
    private final BooleanSetting allowMouseBindings = registerSetting(new BooleanSetting(
            "allow_mouse_bindings", "Allow Mouse Buttons", "Support binding side/middle mouse buttons to modules", true
    ));

    public CustomKeybindSystem() {
        super(
                "custom_keybind_system",
                "Custom Keybind System",
                "Provides per-module and global configurable keybinds.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                true
        );

        guiShortcut.onChange((oldVal, newVal) -> KeybindManager.getInstance().setOpenGuiKeybind(newVal));
        hudEditorShortcut.onChange((oldVal, newVal) -> KeybindManager.getInstance().setOpenHudEditorKeybind(newVal));
    }

    public boolean isAllowMouseBindings() {
        return isEnabled() && allowMouseBindings.isEnabled();
    }
}
