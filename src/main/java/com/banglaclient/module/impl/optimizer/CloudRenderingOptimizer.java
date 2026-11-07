package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.option.CloudRenderMode;

/**
 * Module 022: Cloud Rendering Optimizer
 * Optimizes cloud mesh rendering by switching to Fast 2D clouds or skipping cloud pass when underground.
 */
public final class CloudRenderingOptimizer extends AbstractModule {

    private final BooleanSetting preferFastClouds = registerSetting(new BooleanSetting(
            "prefer_fast_clouds", "Use Fast 2D Clouds", "Switch fancy 3D cloud geometry to fast 2D quad pass", true
    ));
    private final BooleanSetting skipUnderground = registerSetting(new BooleanSetting(
            "skip_underground", "Skip Underground Clouds", "Avoid rendering sky cloud pass when player is deep underground", true
    ));

    public CloudRenderingOptimizer() {
        super(
                "cloud_rendering_optimizer",
                "Cloud Rendering Optimizer",
                "Optimizes cloud geometry and skips cloud pass underground.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    @Override
    public void onEnable() {
        if (preferFastClouds.isEnabled() && mc.options != null) {
            if (mc.options.getCloudRenderMode().getValue() == CloudRenderMode.FANCY) {
                mc.options.getCloudRenderMode().setValue(CloudRenderMode.FAST);
            }
        }
    }

    public boolean shouldSkipCloudPass() {
        if (!isEnabled() || !skipUnderground.isEnabled() || mc.player == null || mc.world == null) {
            return false;
        }
        return mc.player.getY() < 48.0 && !mc.world.isSkyVisible(mc.player.getBlockPos());
    }
}
