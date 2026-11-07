package com.banglaclient.gui.loading;

import com.banglaclient.animation.Easing;
import com.banglaclient.animation.SmoothFloat;
import com.banglaclient.core.ClientCore;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.ClientResourceManager;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Custom BANGLACLIENT Loading Screen renderer.
 *
 * Flow:
 * Game starts -> BANGLACLIENT loading screen -> large centered logo -> smooth interpolated
 * red progress bar -> reaches 100% -> holds briefly and executes a smooth fade/scale transition
 * into the BANGLACLIENT Main Menu.
 */
public final class BanglaLoadingOverlay {

    private static final BanglaLoadingOverlay INSTANCE = new BanglaLoadingOverlay();

    private final SmoothFloat smoothProgress = new SmoothFloat(0.0f, 8.0f);
    private long completionTimestampMillis = 0L;
    private static final long COMPLETION_TRANSITION_MS = 420L;

    private BanglaLoadingOverlay() {
    }

    public static BanglaLoadingOverlay getInstance() {
        return INSTANCE;
    }

    public void reset() {
        smoothProgress.setImmediate(0.0f);
        completionTimestampMillis = 0L;
    }

    /**
     * Renders the custom loading screen and returns true while the transition is still active.
     */
    public boolean render(DrawContext context, int width, int height, float rawReloadProgress, boolean reloadComplete) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();

        smoothProgress.setTarget(reloadComplete ? 1.0f : Math.max(0.02f, Math.min(1.0f, rawReloadProgress)));
        float displayedProgress = smoothProgress.updateAndGet();

        float alphaFactor = 1.0f;
        float scaleOffset = 1.0f;

        if (reloadComplete && displayedProgress >= 0.99f) {
            if (completionTimestampMillis == 0L) {
                completionTimestampMillis = System.currentTimeMillis();
            }
            long elapsed = System.currentTimeMillis() - completionTimestampMillis;
            float t = Math.min(1.0f, (float) elapsed / (float) COMPLETION_TRANSITION_MS);
            float eased = Easing.EASE_IN_OUT_SMOOTH.apply(t);
            alphaFactor = 1.0f - eased;
            scaleOffset = 1.0f + (0.06f * eased);

            if (t >= 1.0f) {
                completionTimestampMillis = 0L;
                return false; // Signal SplashOverlayMixin that transition has finished
            }
        }

        int bgAlpha = Math.round(255 * alphaFactor);
        int bgColor = (bgAlpha << 24) | 0x09090C;
        context.fill(0, 0, width, height, bgColor);

        int centerX = width / 2;
        int centerY = height / 2;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(scaleOffset, scaleOffset);
        context.getMatrices().translate(-centerX, -centerY);

        // Centered large BANGLACLIENT Logo
        int logoSize = 68;
        ClientResourceManager.drawBrandLogo(context, centerX - logoSize / 2, centerY - 68, logoSize);

        // Brand Title
        if (mc.textRenderer != null) {
            String title = ClientCore.MOD_NAME;
            int titleW = mc.textRenderer.getWidth(title);
            int textWhite = palette.getPrimaryText().withAlphaFactor(alphaFactor).getArgb();
            int textGray = palette.getSecondaryText().withAlphaFactor(alphaFactor).getArgb();

            context.drawText(mc.textRenderer, title, centerX - titleW / 2, centerY + 10, textWhite, true);

            // Progress Bar Track
            int barWidth = Math.min(260, width - 80);
            int barHeight = 6;
            int barX = centerX - barWidth / 2;
            int barY = centerY + 32;

            int trackColor = palette.getCard().withAlphaFactor(alphaFactor).getArgb();
            int borderColor = palette.getBorder().withAlphaFactor(alphaFactor).getArgb();
            int fillColor = palette.getAccent().withAlphaFactor(alphaFactor).getArgb();

            Render2DUtil.drawRoundedRect(context, barX, barY, barWidth, barHeight, 3.0f, trackColor);
            Render2DUtil.drawRoundedOutline(context, barX, barY, barWidth, barHeight, 3.0f, 1.0f, borderColor);

            float filledW = Math.max(4.0f, barWidth * displayedProgress);
            Render2DUtil.drawRoundedRect(context, barX, barY, filledW, barHeight, 3.0f, fillColor);

            // Percentage & Stage text
            int pct = Math.min(100, Math.round(displayedProgress * 100.0f));
            String statusText = pct < 100 ? ("Loading client resources... " + pct + "%") : "Ready";
            int statusW = mc.textRenderer.getWidth(statusText);
            context.drawText(mc.textRenderer, statusText, centerX - statusW / 2, barY + 14, textGray, false);
        }

        context.getMatrices().popMatrix();
        return true;
    }
}
