package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 019: Sign Rendering Optimizer
 * Reduces expensive font/glyph layout rendering work for distant signs and hanging signs.
 */
public final class SignRenderingOptimizer extends AbstractModule {

    private final IntegerSetting textRenderDistance = registerSetting(new IntegerSetting(
            "sign_text_dist", "Sign Text Distance", "Hide sign text glyphs beyond this distance while keeping the wood model", 16, 4, 64, 2, "m"
    ));
    private final IntegerSetting modelCullDistance = registerSetting(new IntegerSetting(
            "sign_model_dist", "Sign Model Distance", "Cull entire sign block entity beyond this distance", 48, 16, 96, 4, "m"
    ));
    private final BooleanSetting disableGlowingSignOutline = registerSetting(new BooleanSetting(
            "disable_glow_outline", "Simplify Glowing Text", "Render glowing sign text in a single pass instead of 8-way outline", true
    ));

    public SignRenderingOptimizer() {
        super(
                "sign_rendering_optimizer",
                "Sign Rendering Optimizer",
                "Reduces expensive text and model rendering for distant signs.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullSignText(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = textRenderDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean shouldCullSignModel(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = modelCullDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean isSimplifyGlowingText() {
        return isEnabled() && disableGlowingSignOutline.isEnabled();
    }
}
