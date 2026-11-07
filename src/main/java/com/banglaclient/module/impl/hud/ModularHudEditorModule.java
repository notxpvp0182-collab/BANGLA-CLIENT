package com.banglaclient.module.impl.hud;

import com.banglaclient.gui.hudeditor.HudEditorScreen;
import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;
import org.lwjgl.glfw.GLFW;

/**
 * Module 123: Modular HUD Editor
 * Provides direct access and keybind launching for the free-positioning, non-snapping HUD Editor.
 */
public final class ModularHudEditorModule extends AbstractModule {

    public ModularHudEditorModule() {
        super(
                "modular_hud_editor",
                "Modular HUD Editor",
                "Opens the free-positioning, non-snapping HUD Editor.",
                ModuleCategory.HUD_INFORMATION,
                true
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_H));

        registerSetting(new ActionSetting(
                "open_hud_editor_btn",
                "Launch HUD Editor",
                "Open the interactive free-drag HUD Editor screen",
                "Open HUD Editor",
                () -> {
                    if (mc != null) {
                        mc.setScreen(new HudEditorScreen(mc.currentScreen));
                    }
                }
        ));
    }
}
