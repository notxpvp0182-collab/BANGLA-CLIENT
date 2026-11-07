package com.banglaclient.module.impl.input;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

/**
 * Module 094: Fast XP Bottle
 * Improves client-side input cooldown responsiveness when holding right-click with Experience Bottles
 * without bypassing server-side validation rules. Includes a Server-Safe Mode lock enabled by default.
 */
public final class FastXpBottle extends AbstractModule {

    private final IntegerSetting itemUseDelayTicks = registerSetting(new IntegerSetting(
            "xp_use_delay_ticks", "Throw Interval", "Client right-click cooldown ticks when holding XP Bottles (Vanilla = 4)", 2, 1, 4, 1, " ticks"
    ));
    private final BooleanSetting serverSafeLock = registerSetting(new BooleanSetting(
            "server_safe_lock", "Server-Safe Mode", "Never drop below 1 tick interval to respect server anti-cheat rules", true
    ));

    public FastXpBottle() {
        super(
                "fast_xp_bottle",
                "Fast XP Bottle",
                "Improves input responsiveness when mending armor with XP bottles.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_UNKNOWN));
    }

    public boolean isHoldingXpBottle() {
        if (!isEnabled() || mc.player == null) {
            return false;
        }
        return mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)
                || mc.player.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE);
    }

    public int getTargetUseCooldown() {
        int val = itemUseDelayTicks.getValue();
        return serverSafeLock.isEnabled() ? Math.max(1, val) : val;
    }
}
