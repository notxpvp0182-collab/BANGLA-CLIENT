package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 018: Render Layer Optimizer
 * Optimizes processing of cutout, cutout-mipped, and solid render layers to reduce OpenGL state changes.
 */
public final class RenderLayerOptimizer extends AbstractModule {

    private final BooleanSetting promoteDistantCutout = registerSetting(new BooleanSetting(
            "promote_distant_cutout", "Fast Distant Cutout", "Use simplified alpha testing for distant cutout blocks", true
    ));
    private final BooleanSetting groupShaderBinds = registerSetting(new BooleanSetting(
            "group_shader_binds", "Minimize State Switches", "Avoid redundant render layer state flushes when empty", true
    ));

    public RenderLayerOptimizer() {
        super(
                "render_layer_optimizer",
                "Render Layer Optimizer",
                "Optimizes processing of solid, cutout, and translucent layers.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isPromoteDistantCutout() {
        return isEnabled() && promoteDistantCutout.isEnabled();
    }

    public boolean isGroupShaderBinds() {
        return isEnabled() && groupShaderBinds.isEnabled();
    }
}
