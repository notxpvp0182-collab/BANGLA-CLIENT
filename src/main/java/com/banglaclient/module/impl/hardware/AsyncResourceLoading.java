package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Module 085: Async Resource Loading
 * Moves safe client resource pre-checks and JSON parsing work off the main render thread.
 */
public final class AsyncResourceLoading extends AbstractModule {

    private static final ExecutorService IO_POOL = Executors.newFixedThreadPool(
            2,
            r -> {
                Thread t = new Thread(r, "BanglaClient-AsyncIO");
                t.setDaemon(true);
                return t;
            }
    );

    private final BooleanSetting asyncConfigSerialization = registerSetting(new BooleanSetting(
            "async_config_io", "Async Config Disk Write", "Write configuration backups on a dedicated daemon I/O thread", true
    ));

    public AsyncResourceLoading() {
        super(
                "async_resource_loading",
                "Async Resource Loading",
                "Moves safe resource and disk I/O work away from the main thread.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public void submitIoTask(Runnable task) {
        if (task == null) {
            return;
        }
        if (isEnabled() && asyncConfigSerialization.isEnabled()) {
            CompletableFuture.runAsync(task, IO_POOL);
        } else {
            task.run();
        }
    }
}
