package com.banglaclient.module.impl.hardware;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 081: CPU Thread Optimizer
 * Optimizes client background worker thread pool sizing and priorities so the main render/tick
 * thread is never starved on low-core-count CPUs.
 */
public final class CpuThreadOptimizer extends AbstractModule {

    private final IntegerSetting reservedRenderCores = registerSetting(new IntegerSetting(
            "reserved_render_cores", "Reserved Main Cores", "Logical cores kept free from heavy background tasks", 2, 1, 4, 1, " cores"
    ));
    private final BooleanSetting boostMainThreadPriority = registerSetting(new BooleanSetting(
            "boost_main_priority", "Prioritize Render Thread", "Ensure client render thread runs at high JVM priority", true
    ));

    public CpuThreadOptimizer() {
        super(
                "cpu_thread_optimizer",
                "CPU Thread Optimizer",
                "Optimizes client workload distribution across CPU threads.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    @Override
    public void onEnable() {
        if (boostMainThreadPriority.isEnabled()) {
            try {
                Thread current = Thread.currentThread();
                if (current.getPriority() < Thread.NORM_PRIORITY + 1) {
                    current.setPriority(Math.min(Thread.MAX_PRIORITY, Thread.NORM_PRIORITY + 1));
                }
            } catch (SecurityException ignored) {
            }
        }
    }

    public int getOptimalBackgroundWorkerCount() {
        int total = HardwareTelemetry.getInstance().getAvailableProcessors();
        return Math.max(1, total - reservedRenderCores.getValue());
    }
}
