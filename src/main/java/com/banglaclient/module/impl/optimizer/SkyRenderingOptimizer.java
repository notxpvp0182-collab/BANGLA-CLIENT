package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 023: Sky Rendering Optimizer
 * Skips redundant star/celestial dome VBO draws when completely occluded underground or in dense fog.
 */
public final class SkyRenderingOptimizer extends AbstractModule {

    private final BooleanSetting cullSkyInCaves = registerSetting(new BooleanSetting(
            "cull_sky_caves", "Cull Sky In Sealed Caves", "Skip celestial dome draw calls when below Y=40 without sky access", true
    ));
    private final BooleanSetting skipDaytimeStars = registerSetting(new BooleanSetting(
            "skip_daytime_stars", "Skip Daytime Star Buffer", "Ensure star vertex buffer is never bound during full daylight", true
    ));

    public SkyRenderingOptimizer() {
        super(
                "sky_rendering_optimizer",
                "Sky Rendering Optimizer",
                "Optimizes unnecessary sky and star dome draw calls.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldSkipSkyDome() {
        if (!isEnabled() || !cullSkyInCaves.isEnabled() || mc.player == null || mc.world == null) {
            return false;
        }
        return mc.player.getY() < 40.0 && !mc.world.isSkyVisible(mc.player.getBlockPos());
    }

    public boolean isSkipDaytimeStars() {
        return isEnabled() && skipDaytimeStars.isEnabled();
    }
}
