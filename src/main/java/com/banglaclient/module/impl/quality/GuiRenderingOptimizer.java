package com.banglaclient.module.impl.quality;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 298: GUI Rendering Optimizer
 * Optimizes Click GUI and menu rendering by culling off-screen cards outside scissor bounds
 * and reusing card widget instances.
 */
public final class GuiRenderingOptimizer extends AbstractModule {

    private final BooleanSetting scissorCardCulling = registerSetting(new BooleanSetting(
            "scissor_card_culling", "Viewport Scissor Culling", "Skip rendering module cards scrolled outside the visible viewport", true
    ));
    private final BooleanSetting disableMenuBlurOnLowFps = registerSetting(new BooleanSetting(
            "disable_blur_low_fps", "Skip Screen Blur Under 60 FPS", "Avoid expensive Gaussian screen blur passes on low-end GPUs", true
    ));

    public GuiRenderingOptimizer() {
        super(
                "gui_rendering_optimizer",
                "GUI Rendering Optimizer",
                "Optimizes Click GUI and menu rendering performance.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
    }

    public boolean isScissorCardCulling() { return isEnabled() && scissorCardCulling.isEnabled(); }
    public boolean isDisableMenuBlurOnLowFps() { return isEnabled() && disableMenuBlurOnLowFps.isEnabled(); }
}
