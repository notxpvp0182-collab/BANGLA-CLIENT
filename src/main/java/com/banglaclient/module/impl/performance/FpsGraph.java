package com.banglaclient.module.impl.performance;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 052: FPS Graph
 * Displays real-time rolling FPS history on a continuous free-positioned HUD graph.
 */
public final class FpsGraph extends HudModule {

    private final ColorSetting lineColor = registerSetting(new ColorSetting(
            "graph_line_color", "Graph Line Color", "Color of the FPS waveform line", ColorData.RED_ACCENT, true
    ));
    private final BooleanSetting showHeaderLabel = registerSetting(new BooleanSetting(
            "show_header_label", "Show Header Text", "Display current FPS above the waveform", true
    ));

    public FpsGraph() {
        super(
                "fps_graph",
                "FPS Graph",
                "Displays real-time FPS history.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                86.0f,
                124.0f,
                46.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 124.0f;
        this.unscaledHeight = 46.0f;

        PerformanceTracker perf = PerformanceTracker.getInstance();
        if (showHeaderLabel.isEnabled() && mc.textRenderer != null) {
            context.drawText(
                    mc.textRenderer,
                    "FPS: " + perf.getCurrentFps(),
                    5,
                    4,
                    hudStyle.getTextColor().getArgb(),
                    hudStyle.isTextShadow()
            );
        }

        int[] history = perf.getFpsHistory();
        int cap = perf.getSampleCapacity();
        int writeIdx = perf.getHistoryWriteIndex();

        int maxVal = 120;
        for (int v : history) {
            if (v > maxVal) maxVal = v;
        }

        float gx = 4.0f;
        float gy = showHeaderLabel.isEnabled() ? 15.0f : 4.0f;
        float gw = unscaledWidth - 8.0f;
        float gh = unscaledHeight - gy - 4.0f;
        float stepX = gw / (float) (cap - 1);

        int col = lineColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb();
        int prevX = Math.round(gx);
        int prevY = Math.round(gy + gh);

        for (int i = 0; i < cap; i++) {
            int idx = (writeIdx + i) % cap;
            float norm = Math.min(1.0f, Math.max(0.0f, (float) history[idx] / (float) maxVal));
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
