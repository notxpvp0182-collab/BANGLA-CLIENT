package com.banglaclient.module.impl.input;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 098: Mouse Tweaks
 * Improves supported inventory mouse interactions (shift-left-click drag transfer and scroll wheel item transfer)
 * with rate-limiting to stay 100% compliant with multiplayer server inventory packet limits.
 */
public final class MouseTweaks extends AbstractModule {

    private final BooleanSetting shiftClickDrag = registerSetting(new BooleanSetting(
            "shift_click_drag", "Shift + LMB Drag", "Quick-move hovered slots while holding Shift and Left Mouse Button", true
    ));
    private final BooleanSetting wheelSlotTransfer = registerSetting(new BooleanSetting(
            "wheel_slot_transfer", "Scroll Wheel Transfer", "Transfer single items using the mouse wheel over slots", true
    ));
    private final IntegerSetting minTransferIntervalMs = registerSetting(new IntegerSetting(
            "min_transfer_interval_ms", "Safety Delay", "Minimum milliseconds between slot click packets", 35, 15, 100, 5, "ms"
    ));

    private long lastSlotActionMillis = 0L;

    public MouseTweaks() {
        super(
                "mouse_tweaks",
                "Mouse Tweaks",
                "Enhanced inventory dragging and scroll-wheel item management.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                true
        );
    }

    public boolean canPerformSlotAction() {
        long now = System.currentTimeMillis();
        if (now - lastSlotActionMillis >= minTransferIntervalMs.getValue()) {
            lastSlotActionMillis = now;
            return true;
        }
        return false;
    }

    public boolean isShiftClickDrag() {
        return isEnabled() && shiftClickDrag.isEnabled();
    }

    public boolean isWheelSlotTransfer() {
        return isEnabled() && wheelSlotTransfer.isEnabled();
    }
}
