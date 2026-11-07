package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 001: Input Optimizer
 * Optimizes client keyboard/mouse input handling and reduces unnecessary input processing latency
 * without bypassing Minecraft's legitimate input rules.
 */
public final class InputOptimizer extends AbstractModule {

    private final BooleanSetting rawMousePolling = registerSetting(new BooleanSetting(
            "raw_mouse_polling", "Raw Mouse Polling", "Ensure raw mouse input is prioritized when supported by OS", true
    ));
    private final BooleanSetting skipRedundantKeyRepeats = registerSetting(new BooleanSetting(
            "skip_key_repeats", "Skip Redundant Repeats", "Filter duplicate GLFW_REPEAT events during active gameplay", true
    ));
    private final BooleanSetting flushInputOnRender = registerSetting(new BooleanSetting(
            "flush_input_render", "Immediate Input Dispatch", "Process queued mouse deltas before camera matrix setup", true
    ));

    public InputOptimizer() {
        super(
                "input_optimizer",
                "Input Optimizer",
                "Optimizes keyboard/mouse input handling and reduces latency.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    @Override
    public void onEnable() {
        if (rawMousePolling.isEnabled() && mc.options != null) {
            mc.options.getRawMouseInput().setValue(true);
        }
    }

    public boolean shouldSkipGameplayRepeat(int glfwAction) {
        return isEnabled() && skipRedundantKeyRepeats.isEnabled() && mc.currentScreen == null && glfwAction == 2;
    }

    public boolean isFlushInputOnRender() {
        return isEnabled() && flushInputOnRender.isEnabled();
    }
}
