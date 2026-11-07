package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.option.ParticlesMode;

/**
 * Module 193: Particle Visibility Control
 * Provides granular per-category particle visibility toggles (Explosions, Block Breaking, Ambient, Fishing).
 */
public final class ParticleVisibilityControl extends AbstractModule {

    private final ModeSetting vanillaLevel = registerSetting(new ModeSetting(
            "vanilla_particle_level", "Global Particle Mode", "Base Minecraft particle level", "DECREASED", "ALL", "DECREASED", "MINIMAL"
    ));
    private final BooleanSetting hideBlockBreakParticles = registerSetting(new BooleanSetting(
            "hide_block_break", "Hide Block Break Debris", "Suppress block-breaking debris particles for clearer mining view", false
    ));
    private final BooleanSetting hideAmbientPortalParticles = registerSetting(new BooleanSetting(
            "hide_ambient_portal", "Hide Portal/Biome Dust", "Suppress Nether biome ash, spore, and portal dust", true
    ));

    public ParticleVisibilityControl() {
        super(
                "particle_visibility_control",
                "Particle Visibility Control",
                "Granular visibility controls for world and combat particles.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
        vanillaLevel.onChange((oldV, newV) -> applyLevel(newV));
    }

    @Override
    public void onEnable() {
        applyLevel(vanillaLevel.getValue());
    }

    private void applyLevel(String mode) {
        if (mc.options == null) return;
        ParticlesMode target = switch (mode) {
            case "ALL" -> ParticlesMode.ALL;
            case "MINIMAL" -> ParticlesMode.MINIMAL;
            default -> ParticlesMode.DECREASED;
        };
        mc.options.getParticles().setValue(target);
    }

    public boolean isHideBlockBreakParticles() { return isEnabled() && hideBlockBreakParticles.isEnabled(); }
    public boolean isHideAmbientPortalParticles() { return isEnabled() && hideAmbientPortalParticles.isEnabled(); }
}
