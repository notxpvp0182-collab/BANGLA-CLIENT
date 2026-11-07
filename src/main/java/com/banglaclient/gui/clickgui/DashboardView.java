package com.banglaclient.gui.clickgui;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.profile.ProfileManager;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Dedicated Performance & Client Overview Dashboard (Section 12).
 *
 * Displays:
 * - Current FPS, 1% Low FPS, Frame Time (ms)
 * - CPU Usage, GPU Usage, RAM Usage
 * - Loaded Entities, Loaded Chunks, Ping
 * - Current Profile, Active Optimization Count, Performance State
 * - Live FPS & Frame-Time History Graph
 * - Quick Optimization Action Controls
 */
public final class DashboardView {

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();
        PerformanceTracker perf = PerformanceTracker.getInstance();
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();

        if (mc.textRenderer == null) return;

        float pad = 12.0f;
        float cx = x + pad;
        float cy = y + pad;
        float contentW = width - (pad * 2.0f);

        context.drawText(mc.textRenderer, "PERFORMANCE & TELEMETRY DASHBOARD", Math.round(cx), Math.round(cy), palette.getPrimaryText().getArgb(), true);
        cy += 16.0f;

        // Top Row: 4 Primary Telemetry Cards
        float gap = 8.0f;
        float cardW = (contentW - (gap * 3.0f)) / 4.0f;
        float cardH = 54.0f;

        drawMetricCard(context, cx, cy, cardW, cardH, "CURRENT FPS", perf.getCurrentFps() + " FPS",
                "1% Low: " + perf.getOnePercentLowFps() + " FPS", palette);

        drawMetricCard(context, cx + (cardW + gap), cy, cardW, cardH, "FRAME TIME",
                String.format("%.2f ms", perf.getCurrentFrameTimeMs()),
                perf.getCurrentFrameTimeMs() <= 16.7f ? "State: Optimal" : "State: Elevated", palette);

        drawMetricCard(context, cx + (cardW + gap) * 2.0f, cy, cardW, cardH, "CPU / GPU LOAD",
                String.format("CPU: %.0f%%", hw.getSystemCpuLoadPercent()),
                String.format("Est. GPU: %.0f%%", hw.getEstimatedGpuLoadPercent()), palette);

        drawMetricCard(context, cx + (cardW + gap) * 3.0f, cy, cardW, cardH, "JVM MEMORY",
                hw.getUsedMemoryMb() + " / " + hw.getMaxMemoryMb() + " MB",
                "Usage: " + hw.getMemoryUsagePercent() + "%", palette);

        cy += (cardH + 10.0f);

        // Second Row: World & Client Status Cards
        int entities = (mc.world != null) ? mc.world.getRegularEntityCount() : 0;
        int chunks = (mc.world != null) ? mc.world.getChunkManager().getLoadedChunkCount() : 0;
        int ping = perf.getCurrentPing();
        int activeOpts = ModuleRegistry.getInstance().getActiveOptimizationCount();

        drawMetricCard(context, cx, cy, cardW, cardH, "WORLD ENTITIES", String.valueOf(entities), "Loaded Chunks: " + chunks, palette);
        drawMetricCard(context, cx + (cardW + gap), cy, cardW, cardH, "NETWORK PING", ping + " ms",
                String.format("Est. TPS: %.1f", perf.getEstimatedServerTps()), palette);
        drawMetricCard(context, cx + (cardW + gap) * 2.0f, cy, cardW, cardH, "ACTIVE PROFILE",
                ClientCore.getInstance().getClientState().getActiveProfileName(),
                "Enabled Mods: " + ModuleRegistry.getInstance().getEnabledCount(), palette);
        drawMetricCard(context, cx + (cardW + gap) * 3.0f, cy, cardW, cardH, "OPTIMIZATIONS",
                activeOpts + " Active",
                activeOpts > 15 ? "Mode: Boosted" : "Mode: Standard", palette);

        cy += (cardH + 12.0f);

        // Real-Time FPS History Graph Panel
        float graphH = Math.max(76.0f, height - (cy - y) - 52.0f);
        Render2DUtil.drawRoundedRect(context, cx, cy, contentW, graphH, 6.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, cx, cy, contentW, graphH, 6.0f, 1.0f, palette.getBorder().getArgb());

        context.drawText(mc.textRenderer, "REAL-TIME FPS STABILITY GRAPH", Math.round(cx + 10.0f), Math.round(cy + 8.0f), palette.getSecondaryText().getArgb(), false);

        int[] fpsHist = perf.getFpsHistory();
        int cap = perf.getSampleCapacity();
        int writeIdx = perf.getHistoryWriteIndex();
        float gx = cx + 10.0f;
        float gy = cy + 24.0f;
        float gw = contentW - 20.0f;
        float gh = graphH - 32.0f;

        int maxFpsSample = 120;
        for (int v : fpsHist) {
            if (v > maxFpsSample) maxFpsSample = v;
        }

        float stepX = gw / (float) (cap - 1);
        int prevPx = Math.round(gx);
        int prevPy = Math.round(gy + gh);
        for (int i = 0; i < cap; i++) {
            int idx = (writeIdx + i) % cap;
            int val = fpsHist[idx];
            float norm = Math.min(1.0f, Math.max(0.0f, (float) val / (float) maxFpsSample));
            int px = Math.round(gx + i * stepX);
            int py = Math.round(gy + gh - (norm * gh));
            if (i > 0) {
                Render2DUtil.drawLine(context, prevPx, prevPy, px, py, palette.getGraph().getArgb());
            }
            prevPx = px;
            prevPy = py;
        }

        cy += (graphH + 10.0f);

        // Quick Optimization Action Buttons
        float btnW = (contentW - gap * 2.0f) / 3.0f;
        float btnH = 26.0f;
        drawQuickActionButton(context, "ONE-CLICK FPS BOOST", cx, cy, btnW, btnH, mouseX, mouseY, palette);
        drawQuickActionButton(context, "CLEANUP JVM MEMORY", cx + btnW + gap, cy, btnW, btnH, mouseX, mouseY, palette);
        drawQuickActionButton(context, "APPLY PVP PRESET", cx + (btnW + gap) * 2.0f, cy, btnW, btnH, mouseX, mouseY, palette);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        if (button != 0) return false;
        float pad = 12.0f;
        float cx = x + pad;
        float contentW = width - (pad * 2.0f);
        float gap = 8.0f;
        float btnW = (contentW - gap * 2.0f) / 3.0f;
        float btnH = 26.0f;
        float btnY = y + height - 42.0f;

        if (mouseY >= btnY && mouseY <= btnY + btnH) {
            if (mouseX >= cx && mouseX <= cx + btnW) {
                ProfileManager.getInstance().applyProfile("extreme_fps");
                return true;
            }
            if (mouseX >= cx + btnW + gap && mouseX <= cx + btnW * 2.0f + gap) {
                System.gc();
                HardwareTelemetry.getInstance().pollMetrics();
                return true;
            }
            if (mouseX >= cx + (btnW + gap) * 2.0f && mouseX <= cx + contentW) {
                ProfileManager.getInstance().applyProfile("pvp");
                return true;
            }
        }
        return false;
    }

    private void drawMetricCard(DrawContext context, float x, float y, float w, float h, String title, String primary, String secondary, ThemePalette palette) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Render2DUtil.drawRoundedRect(context, x, y, w, h, 5.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, x, y, w, h, 5.0f, 1.0f, palette.getBorder().getArgb());
        Render2DUtil.drawCornerAccentBracket(context, x + 2.0f, y + 2.0f, 9.0f, 1.5f, palette.getAccent().getArgb());

        context.drawText(mc.textRenderer, title, Math.round(x + 8.0f), Math.round(y + 7.0f), palette.getSecondaryText().getArgb(), false);
        context.drawText(mc.textRenderer, primary, Math.round(x + 8.0f), Math.round(y + 21.0f), palette.getPrimaryText().getArgb(), true);
        context.drawText(mc.textRenderer, secondary, Math.round(x + 8.0f), Math.round(y + 36.0f), palette.getAccent().getArgb(), false);
    }

    private void drawQuickActionButton(DrawContext context, String label, float x, float y, float w, float h, int mx, int my, ThemePalette palette) {
        MinecraftClient mc = MinecraftClient.getInstance();
        boolean hover = mx >= x && mx <= x + w && my >= y && my <= y + h;
        Render2DUtil.drawRoundedRect(context, x, y, w, h, 4.0f, hover ? palette.getAccent().getArgb() : palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, x, y, w, h, 4.0f, 1.0f, palette.getAccent().getArgb());
        int lw = mc.textRenderer.getWidth(label);
        context.drawText(mc.textRenderer, label, Math.round(x + (w - lw) / 2.0f), Math.round(y + 9.0f), palette.getPrimaryText().getArgb(), true);
    }
}
