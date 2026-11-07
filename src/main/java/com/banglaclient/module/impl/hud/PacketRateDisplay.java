package com.banglaclient.module.impl.hud;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 139: Packet Rate Display
 * Displays inbound (RX) and outbound (TX) packets per second measured client-side.
 */
public final class PacketRateDisplay extends HudModule {

    public PacketRateDisplay() {
        super(
                "packet_rate_display",
                "Packet Rate Display",
                "Displays client-side inbound/outbound packet rate.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                254.0f,
                122.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        PerformanceTracker perf = PerformanceTracker.getInstance();
        String text = "RX: " + perf.getLastInboundPacketRate() + "/s | TX: " + perf.getLastOutboundPacketRate() + "/s";
        drawSimpleLabelValue(context, "", text);
    }
}
