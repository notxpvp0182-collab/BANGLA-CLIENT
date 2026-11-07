package com.banglaclient.module.impl.input;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 100: Left Click Delay Control
 * Clears the residual 10-tick left-click miss penalty timer (`attackCooldown`) that vanilla Minecraft
 * applies after missing a swing in the air, improving 1.8/1.21 PvP responsiveness legitimately.
 */
public final class LeftClickDelayControl extends AbstractModule {

    private final BooleanSetting removeMissPenalty = registerSetting(new BooleanSetting(
            "remove_miss_penalty", "Clear Air-Miss Delay", "Remove the 10-tick attack lock after swinging at air", true
    ));
    private final BooleanSetting preserveWeaponCooldown = registerSetting(new BooleanSetting(
            "preserve_weapon_cooldown", "Preserve 1.9+ Cooldown", "Never alter server-authoritative weapon attack speed indicator", true
    ));

    public LeftClickDelayControl() {
        super(
                "left_click_delay_control",
                "Left Click Delay Control",
                "Clears the client air-swing miss delay penalty.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                true
        );
    }

    public boolean shouldClearMissCooldown() {
        return isEnabled() && removeMissPenalty.isEnabled();
    }

    public boolean isPreserveWeaponCooldown() {
        return preserveWeaponCooldown.isEnabled();
    }
}
