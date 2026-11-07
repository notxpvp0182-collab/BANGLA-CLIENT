package com.banglaclient.module.impl.network;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 234: Latency Optimizer
 * Reduces avoidable client-side networking overhead (such as synchronous skin/telemetry stalls during gameplay).
 * Does NOT claim to magically alter physical internet routing latency.
 */
public final class LatencyOptimizer extends AbstractModule {

    private final BooleanSetting deferSkinDownloadsInCombat = registerSetting(new BooleanSetting(
            "defer_skin_downloads", "Throttle Skin HTTP In Combat", "Avoid saturating bandwidth with tablist skin fetches during PvP", true
    ));

    public LatencyOptimizer() {
        super(
                "latency_optimizer",
                "Latency Optimizer",
                "Reduces avoidable client-side bandwidth spikes during combat.",
                ModuleCategory.NETWORK_SERVER,
                true
        );
    }

    public boolean shouldDeferSkinDownload() {
        if (!isEnabled() || !deferSkinDownloadsInCombat.isEnabled() || mc.player == null) {
            return false;
        }
        return mc.player.handSwinging || mc.player.hurtTime > 0;
    }
}
