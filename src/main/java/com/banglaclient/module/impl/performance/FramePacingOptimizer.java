package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 062: Frame Pacing Optimizer
 * Reduces uneven frame delivery and micro-stutter variance by smoothing render-queue flushes.
 */
public final class FramePacingOptimizer extends AbstractModule {

    private final BooleanSetting yieldOnOvershoot = registerSetting(new BooleanSetting(
            "yield_on_overshoot", "Sub-Millisecond Pacing", "Stabilize frame sleep intervals to reduce frame-time variance", true
    ));
    private final IntegerSetting maxQueuedFrames = registerSetting(new IntegerSetting(
            "max_queued_frames", "Target Queue Depth", "Preferred frame queue target for low input latency", 1, 1, 3, 1, ""
    ));

    public FramePacingOptimizer() {
        super(
                "frame_pacing_optimizer",
                "Frame Pacing Optimizer",
                "Reduces uneven frame pacing and micro-stutters.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public boolean isYieldOnOvershoot() {
        return isEnabled() && yieldOnOvershoot.isEnabled();
    }

    public int getMaxQueuedFrames() {
        return maxQueuedFrames.getValue();
    }
}
