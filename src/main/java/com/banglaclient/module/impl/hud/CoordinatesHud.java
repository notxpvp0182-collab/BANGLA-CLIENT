package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 127: Coordinates HUD
 * Displays current X/Y/Z coordinates (and optional Nether/Overworld counterpart coordinates)
 * on a continuous, free-floating HUD element.
 */
public final class CoordinatesHud extends HudModule {

    private final BooleanSetting showDecimal = registerSetting(new BooleanSetting(
            "show_decimal", "Precise Decimals", "Show 1 decimal place instead of rounded block integers", false
    ));
    private final BooleanSetting compactSingleLine = registerSetting(new BooleanSetting(
            "compact_single_line", "Single-Line Layout", "Display X, Y, Z on a single compact line", true
    ));

    public CoordinatesHud() {
        super(
                "coordinates_hud",
                "Coordinates",
                "Shows your current X/Y/Z position.",
                ModuleCategory.HUD_INFORMATION,
                false,
                14.0f,
                604.0f,
                138.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        double x = mc.player != null ? mc.player.getX() : 0.0;
        double y = mc.player != null ? mc.player.getY() : 64.0;
        double z = mc.player != null ? mc.player.getZ() : 0.0;

        if (compactSingleLine.isEnabled()) {
            String text = showDecimal.isEnabled()
                    ? String.format("XYZ: %.1f, %.1f, %.1f", x, y, z)
                    : String.format("XYZ: %d, %d, %d", (int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
            drawSimpleLabelValue(context, "", text);
        } else if (mc.textRenderer != null) {
            this.unscaledWidth = 86.0f;
            this.unscaledHeight = 36.0f;
            int col = hudStyle.getTextColor().getArgb();
            boolean sh = hudStyle.isTextShadow();
            context.drawText(mc.textRenderer, "X: " + (int) Math.floor(x), 6, 4, col, sh);
            context.drawText(mc.textRenderer, "Y: " + (int) Math.floor(y), 6, 14, col, sh);
            context.drawText(mc.textRenderer, "Z: " + (int) Math.floor(z), 6, 24, col, sh);
        }
    }
}
