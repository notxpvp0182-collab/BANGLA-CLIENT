package com.banglaclient.keybind;

import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

/**
 * Processes live keyboard and mouse inputs for BANGLACLIENT:
 * - Opens/closes the Click GUI with the same toggle key (Default: RIGHT SHIFT)
 * - Opens/closes the HUD Editor (Default: H or via GUI button)
 * - Dispatches TOGGLE and HOLD keybinds to registered modules
 */
public final class KeybindManager {

    private static final KeybindManager INSTANCE = new KeybindManager();

    private Keybind openGuiKeybind = Keybind.ofKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    private Keybind openHudEditorKeybind = Keybind.ofKey(GLFW.GLFW_KEY_H);
    private Runnable openOrCloseGuiCallback = () -> {};
    private Runnable openHudEditorCallback = () -> {};

    private KeybindManager() {
    }

    public static KeybindManager getInstance() {
        return INSTANCE;
    }

    public void setScreenCallbacks(Runnable guiCallback, Runnable hudEditorCallback) {
        this.openOrCloseGuiCallback = guiCallback != null ? guiCallback : () -> {};
        this.openHudEditorCallback = hudEditorCallback != null ? hudEditorCallback : () -> {};
    }

    public void onKeyInput(int key, int action) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) {
            return;
        }

        // Allow the same GUI keybind to open AND close the Click GUI
        if (action == GLFW.GLFW_PRESS && openGuiKeybind.matchesKey(key)) {
            if (mc.currentScreen == null || mc.currentScreen.getClass().getSimpleName().equals("BanglaClickGuiScreen")) {
                openOrCloseGuiCallback.run();
                return;
            }
        }

        // Do not trigger gameplay module keybinds while typing in chat or another GUI screen
        if (mc.currentScreen != null) {
            return;
        }

        if (action == GLFW.GLFW_PRESS && openHudEditorKeybind.matchesKey(key)) {
            openHudEditorCallback.run();
            return;
        }

        List<Module> keybindModules = ModuleRegistry.getInstance().getKeybindCapableModules();
        for (int i = 0, size = keybindModules.size(); i < size; i++) {
            Module module = keybindModules.get(i);
            Optional<KeybindSetting> opt = module.getKeybindSetting();
            if (opt.isEmpty()) {
                continue;
            }
            Keybind kb = opt.get().getValue();
            if (!kb.matchesKey(key)) {
                continue;
            }
            if (kb.getTriggerMode() == Keybind.TriggerMode.TOGGLE && action == GLFW.GLFW_PRESS) {
                module.toggle();
            } else if (kb.getTriggerMode() == Keybind.TriggerMode.HOLD) {
                if (action == GLFW.GLFW_PRESS) {
                    module.setEnabled(true);
                } else if (action == GLFW.GLFW_RELEASE) {
                    module.setEnabled(false);
                }
            }
        }
    }

    public void onMouseInput(int button, int action) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.currentScreen != null) {
            return;
        }

        List<Module> keybindModules = ModuleRegistry.getInstance().getKeybindCapableModules();
        for (int i = 0, size = keybindModules.size(); i < size; i++) {
            Module module = keybindModules.get(i);
            Optional<KeybindSetting> opt = module.getKeybindSetting();
            if (opt.isEmpty()) {
                continue;
            }
            Keybind kb = opt.get().getValue();
            if (!kb.matchesMouse(button)) {
                continue;
            }
            if (kb.getTriggerMode() == Keybind.TriggerMode.TOGGLE && action == GLFW.GLFW_PRESS) {
                module.toggle();
            } else if (kb.getTriggerMode() == Keybind.TriggerMode.HOLD) {
                if (action == GLFW.GLFW_PRESS) {
                    module.setEnabled(true);
                } else if (action == GLFW.GLFW_RELEASE) {
                    module.setEnabled(false);
                }
            }
        }
    }

    public Keybind getOpenGuiKeybind() {
        return openGuiKeybind;
    }

    public void setOpenGuiKeybind(Keybind openGuiKeybind) {
        if (openGuiKeybind != null) {
            this.openGuiKeybind = openGuiKeybind;
        }
    }

    public Keybind getOpenHudEditorKeybind() {
        return openHudEditorKeybind;
    }

    public void setOpenHudEditorKeybind(Keybind openHudEditorKeybind) {
        if (openHudEditorKeybind != null) {
            this.openHudEditorKeybind = openHudEditorKeybind;
        }
    }
}
