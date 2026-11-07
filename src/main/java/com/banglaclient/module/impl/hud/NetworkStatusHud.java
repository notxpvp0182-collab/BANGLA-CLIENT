package com.banglaclient.module.impl.hud;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 138: Network Status HUD
 * Displays current network connection state (Local Singleplayer, Stable, Elevated Ping, or Lag Spike).
 */
public final class NetworkStatusHud extends HudModule {

    public NetworkStatusHud() {
        super(
                "network_status_hud",
                "Network Status HUD",
                "Displays connection stability and network state.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                230.0f,
                114.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.isInSingleplayer()) {
            drawSimpleLabelValue(context, "Net", "Singleplayer");
            return;
        }
        int ping = PerformanceTracker.getInstance().getCurrentPing();
        float tps = PerformanceTracker.getInstance().getEstimatedServerTps();
        String state;
        if (tps < 14.0f) {
            state = "Server Lag";
        } else if (ping > 220) {
            state = "High Ping";
        } else if (ping > 120) {
            state = "Moderate";
        } else {
            state = "Stable";
        }
        drawSimpleLabelValue(context, "Net", state);
    }
}
