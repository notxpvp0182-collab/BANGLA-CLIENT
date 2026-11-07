package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 082: Render Thread Optimizer
 * Reduces unnecessary render-thread workload by avoiding redundant matrix/state validations
 * and deferring non-render telemetry polling outside the draw pass.
 */
public final class RenderThreadOptimizer extends AbstractModule {

    private final BooleanSetting deferTelemetryToTick = registerSetting(new BooleanSetting(
            "defer_telemetry_to_tick", "Defer OS Telemetry", "Poll JMX CPU/RAM metrics only on tick intervals instead of draw calls", true
    ));
    private final BooleanSetting batchGuiDrawCalls = registerSetting(new BooleanSetting(
            "batch_gui_draws", "Minimize HUD State Flushes", "Group solid HUD panel fills before text rendering", true
    ));

    public RenderThreadOptimizer() {
        super(
                "render_thread_optimizer",
                "Render Thread Optimizer",
                "Reduces unnecessary render-thread overhead during frame draw.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public boolean isDeferTelemetryToTick() {
        return isEnabled() && deferTelemetryToTick.isEnabled();
    }

    public boolean isBatchGuiDrawCalls() {
        return isEnabled() && batchGuiDrawCalls.isEnabled();
    }
}
