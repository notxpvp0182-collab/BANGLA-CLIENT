package com.banglaclient.gui.widget;

import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Interactive RGB / HSB / Alpha / Preset Color Picker widget for module settings and Custom Theme editor.
 */
public final class ColorPickerWidget {

    private static final ColorData[] PRESETS = new ColorData[]{
            ColorData.RED_ACCENT,
            ColorData.DARK_RED,
            ColorData.BRIGHT_RED,
            ColorData.WHITE,
            ColorData.GRAY,
            ColorData.BLACK,
            new ColorData(40, 180, 99, 255),
            new ColorData(52, 152, 219, 255)
    };

    private final ColorSetting setting;
    private float hue;
    private float saturation;
    private float brightness;
    private int alpha;

    public ColorPickerWidget(ColorSetting setting) {
        this.setting = setting;
        syncFromSetting();
    }

    public void syncFromSetting() {
        ColorData c = setting.getValue();
        float[] hsb = c.toHSB();
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = c.getAlpha();
    }

    public void render(DrawContext context, float x, float y, float width, float height) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();

        Render2DUtil.drawRoundedRect(context, x, y, width, height, 4.0f, 0xFF14141A);
        Render2DUtil.drawRoundedOutline(context, x, y, width, height, 4.0f, 1.0f, palette.getBorder().getArgb());

        // Hue Bar
        float barX = x + 8.0f;
        float hueY = y + 8.0f;
        float barW = width - 16.0f;
        int segments = 24;
        float segW = barW / segments;
        for (int i = 0; i < segments; i++) {
            float h = (float) i / (float) segments;
            int col = ColorData.fromHSB(h, 1.0f, 1.0f, 255).getArgb();
            Render2DUtil.drawRect(context, barX + i * segW, hueY, segW + 0.5f, 8.0f, col);
        }
        Render2DUtil.drawRect(context, barX + hue * barW - 1.0f, hueY - 1.0f, 3.0f, 10.0f, 0xFFFFFFFF);

        // Saturation Bar
        float satY = hueY + 14.0f;
        int satStart = ColorData.fromHSB(hue, 0.0f, brightness, 255).getArgb();
        int satEnd = ColorData.fromHSB(hue, 1.0f, brightness, 255).getArgb();
        Render2DUtil.drawHorizontalGradient(context, barX, satY, barW, 8.0f, satStart, satEnd);
        Render2DUtil.drawRect(context, barX + saturation * barW - 1.0f, satY - 1.0f, 3.0f, 10.0f, 0xFFFFFFFF);

        // Brightness Bar
        float briY = satY + 14.0f;
        int briStart = 0xFF000000;
        int briEnd = ColorData.fromHSB(hue, saturation, 1.0f, 255).getArgb();
        Render2DUtil.drawHorizontalGradient(context, barX, briY, barW, 8.0f, briStart, briEnd);
        Render2DUtil.drawRect(context, barX + brightness * barW - 1.0f, briY - 1.0f, 3.0f, 10.0f, 0xFFFFFFFF);

        // Alpha Bar
        float alphaY = briY + 14.0f;
        int aStart = ColorData.fromHSB(hue, saturation, brightness, 20).getArgb();
        int aEnd = ColorData.fromHSB(hue, saturation, brightness, 255).getArgb();
        Render2DUtil.drawHorizontalGradient(context, barX, alphaY, barW, 8.0f, aStart, aEnd);
        Render2DUtil.drawRect(context, barX + (alpha / 255.0f) * barW - 1.0f, alphaY - 1.0f, 3.0f, 10.0f, 0xFFFFFFFF);

        // Preset Swatches + HEX display
        float swatchY = alphaY + 14.0f;
        float swatchSize = 12.0f;
        for (int i = 0; i < PRESETS.length; i++) {
            float sx = barX + i * (swatchSize + 4.0f);
            Render2DUtil.drawRoundedRect(context, sx, swatchY, swatchSize, swatchSize, 2.0f, PRESETS[i].getArgb());
            Render2DUtil.drawRoundedOutline(context, sx, swatchY, swatchSize, swatchSize, 2.0f, 1.0f, 0xFF32323C);
        }

        if (mc.textRenderer != null) {
            String hexText = setting.getValue().toHex(setting.isAllowAlpha());
            context.drawText(mc.textRenderer, hexText, Math.round(x + width - mc.textRenderer.getWidth(hexText) - 8.0f), Math.round(swatchY + 2.0f), palette.getPrimaryText().getArgb(), false);
        }
    }

    public boolean mouseInteraction(double mouseX, double mouseY, float x, float y, float width) {
        float barX = x + 8.0f;
        float barW = width - 16.0f;
        float ratio = (float) Math.max(0.0, Math.min(1.0, (mouseX - barX) / barW));

        float hueY = y + 8.0f;
        float satY = hueY + 14.0f;
        float briY = satY + 14.0f;
        float alphaY = briY + 14.0f;
        float swatchY = alphaY + 14.0f;

        if (mouseX >= barX && mouseX <= barX + barW) {
            if (mouseY >= hueY && mouseY <= hueY + 10.0f) {
                hue = ratio;
                pushColorUpdate();
                return true;
            }
            if (mouseY >= satY && mouseY <= satY + 10.0f) {
                saturation = ratio;
                pushColorUpdate();
                return true;
            }
            if (mouseY >= briY && mouseY <= briY + 10.0f) {
                brightness = ratio;
                pushColorUpdate();
                return true;
            }
            if (mouseY >= alphaY && mouseY <= alphaY + 10.0f && setting.isAllowAlpha()) {
                alpha = Math.round(ratio * 255.0f);
                pushColorUpdate();
                return true;
            }
        }

        // Preset clicks
        float swatchSize = 12.0f;
        for (int i = 0; i < PRESETS.length; i++) {
            float sx = barX + i * (swatchSize + 4.0f);
            if (mouseX >= sx && mouseX <= sx + swatchSize && mouseY >= swatchY && mouseY <= swatchY + swatchSize) {
                setting.setValue(PRESETS[i]);
                syncFromSetting();
                return true;
            }
        }
        return false;
    }

    private void pushColorUpdate() {
        setting.setValue(ColorData.fromHSB(hue, saturation, brightness, alpha));
    }
}
