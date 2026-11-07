package com.banglaclient.module.impl.hud;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 135: Server TPS Display
 * Displays estimated server ticks per second (Est. TPS) derived from legitimate world-time update intervals.
 */
public final class ServerTpsDisplay extends HudModule {

    public ServerTpsDisplay() {
        super(
                "server_tps_display",
                "Server TPS Display",
                "Displays estimated server TPS from world-time packets.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                136.0f,
                96.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        float tps = PerformanceTracker.getInstance().getEstimatedServerTps();
        drawSimpleLabelValue(context, "Est. TPS", String.format("%.1f", tps));
    }
}
