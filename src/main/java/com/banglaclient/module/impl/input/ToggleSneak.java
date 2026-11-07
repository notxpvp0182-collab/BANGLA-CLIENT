package com.banglaclient.module.impl.input;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import org.lwjgl.glfw.GLFW;

/**
 * Module 097: Toggle Sneak
 * Toggles sneak state while walking on ledges/bridges and automatically pauses while open GUI screens are active.
 */
public final class ToggleSneak extends AbstractModule {

    private final BooleanSetting releaseInScreens = registerSetting(new BooleanSetting(
            "release_in_screens", "Pause In Inventories", "Do not hold sneak while inventory or chest screens are open", true
    ));
    private final BooleanSetting disableWhenFlying = registerSetting(new BooleanSetting(
            "disable_when_flying", "Disable While Flying", "Prevent descending endlessly when flying in Creative mode", true
    ));

    public ToggleSneak() {
        super(
                "toggle_sneak",
                "Toggle Sneak",
                "Toggles sneak state without holding the sneak key.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_UNKNOWN));
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    @Override
    public void onDisable() {
        if (mc.options != null) {
            mc.options.sneakKey.setPressed(false);
        }
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.START || mc.player == null || mc.options == null) {
            return;
        }
        if (releaseInScreens.isEnabled() && mc.currentScreen != null) {
            mc.options.sneakKey.setPressed(false);
            return;
        }
        if (disableWhenFlying.isEnabled() && mc.player.getAbilities().flying) {
            return;
        }
        mc.options.sneakKey.setPressed(true);
    }
}
