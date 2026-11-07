package com.banglaclient.module.impl.input;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 099: Break Delay Control
 * Provides client-side control over the post-break block-mining cooldown counter (Vanilla = 5 ticks)
 * without modifying block hardness or bypassing server-side mining speed validation.
 */
public final class BreakDelayControl extends AbstractModule {

    private final IntegerSetting cooldownTicks = registerSetting(new IntegerSetting(
            "break_cooldown_ticks", "Post-Break Delay", "Client delay ticks between breaking consecutive blocks (Vanilla = 5)", 4, 1, 5, 1, " ticks"
    ));
    private final BooleanSetting serverSafeMode = registerSetting(new BooleanSetting(
            "server_safe_mode", "Server-Safe Mode", "Keep minimum 3-tick interval on multiplayer servers", true
    ));

    public BreakDelayControl() {
        super(
                "break_delay_control",
                "Break Delay Control",
                "Controls client-side post-break input timing safely.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
    }

    public int getEffectiveBreakCooldown() {
        if (!isEnabled()) {
            return 5;
        }
        int configured = cooldownTicks.getValue();
        if (serverSafeMode.isEnabled() && !mc.isInSingleplayer()) {
            return Math.max(3, configured);
        }
        return configured;
    }
}
