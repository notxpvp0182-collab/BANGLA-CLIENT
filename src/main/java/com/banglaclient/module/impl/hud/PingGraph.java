package com.banglaclient.module.impl.hud;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 137: Ping Graph
 * Displays rolling network latency history on a free-floating HUD waveform.
 */
public final class PingGraph extends HudModule {

    private final ColorSetting lineColor = registerSetting(new ColorSetting(
            "ping_line_color", "Waveform Color", "Color of the latency waveform line", ColorData.RED_ACCENT, true
    ));

    public PingGraph() {
        super(
                "ping_graph",
                "Ping Graph",
                "Displays real-time network latency history.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                184.0f,
                118.0f,
                42.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 118.0f;
        this.unscaledHeight = 42.0f;

        PerformanceTracker perf = PerformanceTracker.getInstance();
        if (mc.textRenderer != null) {
            context.drawText(mc.textRenderer, "Ping: " + perf.getCurrentPing() + " ms", 5, 4,
                    hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        }

        int[] history = perf.getPingHistory();
        int cap = perf.getSampleCapacity();
        int writeIdx = perf.getHistoryWriteIndex();

        int maxPing = 200;
        for (int p : history) {
            if (p > maxPing) maxPing = p;
        }

        float gx = 4.0f;
        float gy = 14.0f;
        float gw = unscaledWidth - 8.0f;
        float gh = unscaledHeight - gy - 4.0f;
        float stepX = gw / (float) (cap - 1);
        int col = lineColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb();

        int prevX = Math.round(gx);
        int prevY = Math.round(gy + gh);
        for (int i = 0; i < cap; i++) {
            int idx = (writeIdx + i) % cap;
            float norm = Math.min(1.0f, Math.max(0.0f, (float) history[idx] / (float) maxPing));
            int px = Math.round(gx + i * stepX);
            int py = Math.round(gy + gh - norm * gh);
            if (i > 0) {
                Render2DUtil.drawLine(context, prevX, prevY, px, py, col);
            }
            prevX = px;
            prevY = py;
        }
    }
}
