package com.banglaclient.module.impl.network;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 236: Connection Status
 * Displays live connection state, ping, and packet activity on a free-floating HUD element.
 */
public final class ConnectionStatus extends HudModule {

    public ConnectionStatus() {
        super(
                "connection_status",
                "Connection Status",
                "Displays live multiplayer connection status.",
                ModuleCategory.NETWORK_SERVER,
                false,
                460.0f,
                14.0f,
                132.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.getNetworkHandler() == null) {
            drawSimpleLabelValue(context, "Status", "Offline");
            return;
        }
        if (mc.isInSingleplayer()) {
            drawSimpleLabelValue(context, "Status", "Integrated Server");
            return;
        }
        int ping = PerformanceTracker.getInstance().getCurrentPing();
        drawSimpleLabelValue(context, "Connected", ping + " ms");
    }
}
