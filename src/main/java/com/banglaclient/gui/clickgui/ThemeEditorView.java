package com.banglaclient.gui.clickgui;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.theme.ColorData;
import com.banglaclient.theme.Theme;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/**
 * Theme Selector & Custom Theme Palette Editor View (Section 28).
 * Supports Default Red, Dark Red, Black & White + Red, and Custom Theme.
 */
public final class ThemeEditorView {

    private static final ColorData[] ACCENT_SWATCHES = new ColorData[]{
            new ColorData(229, 37, 33, 255),
            new ColorData(184, 29, 26, 255),
            new ColorData(255, 58, 48, 255),
            new ColorData(255, 255, 255, 255),
            new ColorData(230, 126, 34, 255),
            new ColorData(46, 204, 113, 255)
    };

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();
        if (mc.textRenderer == null) return;

        float pad = 14.0f;
        float cx = x + pad;
        float cy = y + pad;
        float contentW = width - pad * 2.0f;

        context.drawText(mc.textRenderer, "BANGLACLIENT THEME ENGINE", Math.round(cx), Math.round(cy), palette.getPrimaryText().getArgb(), true);
        cy += 18.0f;

        List<Theme> themes = ThemeManager.getInstance().getAllThemes();
        Theme active = ThemeManager.getInstance().getActiveTheme();

        float gap = 10.0f;
        float cardW = (contentW - gap * 3.0f) / 4.0f;
        float cardH = 58.0f;

        for (int i = 0; i < themes.size(); i++) {
            Theme t = themes.get(i);
            float tx = cx + i * (cardW + gap);
            boolean isSelected = (t == active);

            Render2DUtil.drawRoundedRect(context, tx, cy, cardW, cardH, 5.0f, t.getPalette().getCard().getArgb());
            Render2DUtil.drawRoundedOutline(context, tx, cy, cardW, cardH, 5.0f, isSelected ? 1.5f : 1.0f,
                    isSelected ? palette.getAccent().getArgb() : palette.getBorder().getArgb());

            context.drawText(mc.textRenderer, t.getName(), Math.round(tx + 8.0f), Math.round(cy + 10.0f), t.getPalette().getPrimaryText().getArgb(), true);

            // Color preview dots
            Render2DUtil.drawRoundedRect(context, tx + 8.0f, cy + 30.0f, 16.0f, 16.0f, 3.0f, t.getPalette().getBackground().getArgb());
            Render2DUtil.drawRoundedRect(context, tx + 28.0f, cy + 30.0f, 16.0f, 16.0f, 3.0f, t.getPalette().getAccent().getArgb());
            Render2DUtil.drawRoundedRect(context, tx + 48.0f, cy + 30.0f, 16.0f, 16.0f, 3.0f, t.getPalette().getPrimaryText().getArgb());
        }

        cy += (cardH + 18.0f);

        // Custom Theme Swatch Customizer
        Render2DUtil.drawRoundedRect(context, cx, cy, contentW, 88.0f, 6.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, cx, cy, contentW, 88.0f, 6.0f, 1.0f, palette.getBorder().getArgb());

        context.drawText(mc.textRenderer, "CUSTOM THEME ACCENT PALETTE", Math.round(cx + 12.0f), Math.round(cy + 12.0f), palette.getPrimaryText().getArgb(), true);
        context.drawText(mc.textRenderer, "Select a custom accent color to dynamically re-style the Custom Theme:", Math.round(cx + 12.0f), Math.round(cy + 26.0f), palette.getSecondaryText().getArgb(), false);

        float sx = cx + 12.0f;
        float sy = cy + 46.0f;
        for (ColorData swatch : ACCENT_SWATCHES) {
            Render2DUtil.drawRoundedRect(context, sx, sy, 32.0f, 26.0f, 4.0f, swatch.getArgb());
            Render2DUtil.drawRoundedOutline(context, sx, sy, 32.0f, 26.0f, 4.0f, 1.0f, 0xFFFFFFFF);
            sx += 42.0f;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        if (button != 0) return false;
        float pad = 14.0f;
        float cx = x + pad;
        float cy = y + pad + 18.0f;
        float contentW = width - pad * 2.0f;

        List<Theme> themes = ThemeManager.getInstance().getAllThemes();
        float gap = 10.0f;
        float cardW = (contentW - gap * 3.0f) / 4.0f;
        float cardH = 58.0f;

        for (int i = 0; i < themes.size(); i++) {
            float tx = cx + i * (cardW + gap);
            if (mouseX >= tx && mouseX <= tx + cardW && mouseY >= cy && mouseY <= cy + cardH) {
                ThemeManager.getInstance().setActiveTheme(themes.get(i).getId());
                ConfigManager.getInstance().saveConfig();
                return true;
            }
        }

        float sy = cy + cardH + 18.0f + 46.0f;
        float sx = cx + 12.0f;
        for (ColorData swatch : ACCENT_SWATCHES) {
            if (mouseX >= sx && mouseX <= sx + 32.0f && mouseY >= sy && mouseY <= sy + 26.0f) {
                Theme custom = ThemeManager.getInstance().getCustomTheme();
                custom.getPalette().setAccent(swatch);
                custom.getPalette().setEnabled(swatch);
                custom.getPalette().setGraph(swatch);
                custom.getPalette().setScrollbar(swatch);
                ThemeManager.getInstance().setActiveTheme("custom");
                ConfigManager.getInstance().saveConfig();
                return true;
            }
            sx += 42.0f;
        }
        return false;
    }
}
