package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 083: Background Task Optimizer
 * Controls non-critical background work (such as skin/profile lookups and auto-saves) during active combat.
 */
public final class BackgroundTaskOptimizer extends AbstractModule {

    private final BooleanSetting throttleDuringCombat = registerSetting(new BooleanSetting(
            "throttle_during_combat", "Defer Tasks In Combat", "Delay non-critical config writes and skin fetches during active PvP", true
    ));
    private final IntegerSetting combatCooldownSeconds = registerSetting(new IntegerSetting(
            "combat_cooldown_sec", "Combat Window", "Seconds after last hit to keep background tasks throttled", 5, 2, 15, 1, "s"
    ));

    public BackgroundTaskOptimizer() {
        super(
                "background_task_optimizer",
                "Background Task Optimizer",
                "Controls non-critical background work during gameplay.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public boolean shouldDeferNonCriticalTask() {
        if (!isEnabled() || !throttleDuringCombat.isEnabled() || mc.player == null) {
            return false;
        }
        return mc.player.handSwinging || mc.player.hurtTime > 0;
    }

    public int getCombatCooldownSeconds() {
        return combatCooldownSeconds.getValue();
    }
}
