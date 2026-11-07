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
 * Module 053: Frame Time Graph
 * Displays real-time frame-time stability in milliseconds to visualize micro-stutters.
 */
public final class FrameTimeGraph extends HudModule {

    private final ColorSetting lineColor = registerSetting(new ColorSetting(
            "ft_line_color", "Waveform Color", "Color of the frame-time line", ColorData.RED_ACCENT, true
    ));
    private final BooleanSetting showTargetLine = registerSetting(new BooleanSetting(
            "show_target_line", "Show 16.7ms Reference", "Draw a reference guide line at 16.67ms (60 FPS)", true
    ));

    public FrameTimeGraph() {
        super(
                "frame_time_graph",
                "Frame Time Graph",
                "Displays frame-time stability.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                136.0f,
                124.0f,
                46.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 124.0f;
        this.unscaledHeight = 46.0f;

        PerformanceTracker perf = PerformanceTracker.getInstance();
        if (mc.textRenderer != null) {
            String title = String.format("Frame: %.1f ms", perf.getCurrentFrameTimeMs());
            context.drawText(mc.textRenderer, title, 5, 4, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        }

        float[] history = perf.getFrameTimeHistoryMs();
        int cap = perf.getSampleCapacity();

        float gx = 4.0f;
        float gy = 15.0f;
        float gw = unscaledWidth - 8.0f;
        float gh = unscaledHeight - gy - 4.0f;
        float maxMs = 33.3f;

        if (showTargetLine.isEnabled()) {
            int refY = Math.round(gy + gh - (16.67f / maxMs) * gh);
            Render2DUtil.drawRect(context, gx, refY, gw, 1.0f, 0x55FFFFFF);
        }

        float stepX = gw / (float) (cap - 1);
        int col = lineColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb();
        int prevX = Math.round(gx);
        int prevY = Math.round(gy + gh);

        for (int i = 0; i < cap; i++) {
            float norm = Math.min(1.0f, Math.max(0.0f, history[i] / maxMs));
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
