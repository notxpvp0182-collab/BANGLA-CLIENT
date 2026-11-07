package com.banglaclient.module.impl.input;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

/**
 * Module 095: Toggle Sprint
 * Toggles sprint state instead of requiring the sprint key to remain held, and optionally renders
 * a free-floating status label ("[Sprinting (Toggled)]").
 */
public final class ToggleSprint extends HudModule {

    private final BooleanSetting showHudStatus = registerSetting(new BooleanSetting(
            "show_hud_status", "Show Sprint Text HUD", "Display current sprint state on the HUD", true
    ));
    private final BooleanSetting disableWhileFlying = registerSetting(new BooleanSetting(
            "disable_while_flying", "Pause In Creative Flight", "Do not force sprint while flying in Creative mode", false
    ));

    private boolean sprintToggled = true;

    public ToggleSprint() {
        super(
                "toggle_sprint",
                "Toggle Sprint",
                "Automatically keeps you sprinting without holding the sprint key.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                true,
                14.0f,
                412.0f,
                118.0f,
                20.0f
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_I));
    }

    @Override
    public void onEnable() {
        sprintToggled = true;
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    @Override
    public void onDisable() {
        if (mc.options != null) {
            mc.options.sprintKey.setPressed(false);
        }
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.START || mc.player == null || mc.options == null) {
            return;
        }
        if (disableWhileFlying.isEnabled() && mc.player.getAbilities().flying) {
            return;
        }
        if (sprintToggled && mc.player.forwardSpeed > 0.0f && !mc.player.isSneaking() && !mc.player.isUsingItem() && !mc.player.horizontalCollision) {
            mc.player.setSprinting(true);
        }
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (!showHudStatus.isEnabled() && !isEditing) {
            return;
        }
        String status = "[Sprinting (Toggled)]";
        if (mc.player != null) {
            if (mc.player.getAbilities().flying) {
                status = "[Flying]";
            } else if (mc.player.isSneaking()) {
                status = "[Sneaking]";
            } else if (mc.options.sprintKey.isPressed()) {
                status = "[Sprinting (Key Held)]";
            }
        }
        drawSimpleLabelValue(context, "", status);
    }
}
