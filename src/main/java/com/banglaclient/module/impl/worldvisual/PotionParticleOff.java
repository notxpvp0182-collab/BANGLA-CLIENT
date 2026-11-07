package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 194: Potion Particle Off
 * Disables floating status-effect swirl particles on yourself and/or other entities.
 */
public final class PotionParticleOff extends AbstractModule {

    private final BooleanSetting hideSelfParticles = registerSetting(new BooleanSetting(
            "hide_self_potion", "Hide Own Particles", "Hide potion swirls emitted by your own player", true
    ));
    private final BooleanSetting hideOtherPlayerParticles = registerSetting(new BooleanSetting(
            "hide_other_potion", "Hide Other Players", "Hide potion swirls emitted by other players and mobs", false
    ));

    public PotionParticleOff() {
        super(
                "potion_particle_off",
                "Potion Particle Off",
                "Disables potion swirl particles blocking your vision.",
                ModuleCategory.WORLD_VISUAL,
                true
        );
    }

    public boolean isHideSelfParticles() { return isEnabled() && hideSelfParticles.isEnabled(); }
    public boolean isHideOtherPlayerParticles() { return isEnabled() && hideOtherPlayerParticles.isEnabled(); }
}
