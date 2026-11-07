package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 035: Totem Optimizer
 * Optimizes Totem of Undying pop animations and yellow/green particle clouds so screen visibility
 * and frame rates remain stable during intense crystal/mace PvP.
 */
public final class TotemOptimizer extends AbstractModule {

    private final IntegerSetting totemParticlePercent = registerSetting(new IntegerSetting(
            "totem_particle_pct", "Totem Particle Density", "Percentage of Totem pop emitter particles to render", 35, 0, 100, 5, "%"
    ));
    private final BooleanSetting fastOverlayFade = registerSetting(new BooleanSetting(
            "fast_overlay_fade", "Fast Pop Animation", "Shorten the floating 3D Totem screen obstruction duration", true
    ));

    public TotemOptimizer() {
        super(
                "totem_optimizer",
                "Totem Optimizer",
                "Optimizes Totem pop particle bursts and screen overlay duration.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public int getTotemParticlePercent() {
        return isEnabled() ? totemParticlePercent.getValue() : 100;
    }

    public boolean isFastOverlayFade() {
        return isEnabled() && fastOverlayFade.isEnabled();
    }
}
