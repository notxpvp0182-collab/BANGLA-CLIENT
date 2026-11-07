package com.banglaclient.gui.clickgui;

import com.banglaclient.core.ClientCore;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.ClientResourceManager;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Clean, minimal About Screen (Section 29):
 * - Developed by Ahmed
 * - Short description explaining that BANGLACLIENT is a client-side Minecraft project developed in Bangladesh
 * - Bangla SMP (play.banglaSMP.net)
 */
public final class AboutView {

    public void render(DrawContext context, float x, float y, float width, float height) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();
        if (mc.textRenderer == null) return;

        float cardW = Math.min(380.0f, width - 40.0f);
        float cardH = 190.0f;
        float cx = x + (width - cardW) / 2.0f;
        float cy = y + (height - cardH) / 2.0f;

        Render2DUtil.drawRoundedRect(context, cx, cy, cardW, cardH, 8.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, cx, cy, cardW, cardH, 8.0f, 1.5f, palette.getAccent().getArgb());

        int centerX = Math.round(cx + cardW / 2.0f);
        ClientResourceManager.drawBrandLogo(context, centerX - 22, Math.round(cy + 16.0f), 44);

        drawCentered(context, mc, ClientCore.MOD_NAME + " v" + ClientCore.MOD_VERSION, centerX, Math.round(cy + 68.0f), palette.getPrimaryText().getArgb(), true);
        drawCentered(context, mc, "Developed by " + ClientCore.DEVELOPER, centerX, Math.round(cy + 84.0f), palette.getAccent().getArgb(), true);

        drawCentered(context, mc, "BANGLACLIENT is a high-performance client-side Minecraft", centerX, Math.round(cy + 106.0f), palette.getSecondaryText().getArgb(), false);
        drawCentered(context, mc, "optimization & HUD project developed in Bangladesh.", centerX, Math.round(cy + 118.0f), palette.getSecondaryText().getArgb(), false);

        Render2DUtil.drawRect(context, cx + 40.0f, cy + 136.0f, cardW - 80.0f, 1.0f, palette.getBorder().getArgb());

        drawCentered(context, mc, "Bangla SMP", centerX, Math.round(cy + 146.0f), palette.getPrimaryText().getArgb(), true);
        drawCentered(context, mc, ClientCore.OFFICIAL_SERVER, centerX, Math.round(cy + 162.0f), palette.getAccent().getArgb(), true);
    }

    private void drawCentered(DrawContext context, MinecraftClient mc, String text, int centerX, int y, int color, boolean shadow) {
        int w = mc.textRenderer.getWidth(text);
        context.drawText(mc.textRenderer, text, centerX - w / 2, y, color, shadow);
    }
}
