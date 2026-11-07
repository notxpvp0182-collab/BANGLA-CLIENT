package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 031: Fast Hotbar Optimizer
 * Improves hotbar switching visual responsiveness and reduces held-item equip bobbing lag
 * without changing legitimate server-validated slot packets.
 */
public final class FastHotbarOptimizer extends AbstractModule {

    private final BooleanSetting instantEquipTransition = registerSetting(new BooleanSetting(
            "instant_equip_transition", "Instant Slot Visuals", "Reduce visual equip dip delay when rapidly switching hotbar slots", true
    ));
    private final BooleanSetting skipRedundantTooltipRebuilds = registerSetting(new BooleanSetting(
            "skip_tooltip_rebuilds", "Cache Hotbar Item Name", "Avoid recalculating item name text width every frame", true
    ));

    public FastHotbarOptimizer() {
        super(
                "fast_hotbar_optimizer",
                "Fast Hotbar Optimizer",
                "Improves hotbar switching responsiveness and held-item transitions.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isInstantEquipTransition() {
        return isEnabled() && instantEquipTransition.isEnabled();
    }

    public boolean isSkipRedundantTooltipRebuilds() {
        return isEnabled() && skipRedundantTooltipRebuilds.isEnabled();
    }
}
