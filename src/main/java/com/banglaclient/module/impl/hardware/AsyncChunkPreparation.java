package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 086: Async Chunk Preparation
 * Optimizes chunk preparation and visibility pre-filtering through safe asynchronous worker scheduling.
 */
public final class AsyncChunkPreparation extends AbstractModule {

    private final BooleanSetting preFilterEmptySections = registerSetting(new BooleanSetting(
            "pre_filter_empty_sections", "Pre-Filter Air Sections", "Skip scheduling mesh build tasks for completely empty chunk sections", true
    ));
    private final BooleanSetting asyncLightCheck = registerSetting(new BooleanSetting(
            "async_light_check", "Non-Blocking Light Queue", "Avoid stalling the render thread on distant chunk light updates", true
    ));

    public AsyncChunkPreparation() {
        super(
                "async_chunk_preparation",
                "Async Chunk Preparation",
                "Optimizes chunk preparation through safe asynchronous filtering.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public boolean isPreFilterEmptySections() {
        return isEnabled() && preFilterEmptySections.isEnabled();
    }

    public boolean isAsyncLightCheck() {
        return isEnabled() && asyncLightCheck.isEnabled();
    }
}
