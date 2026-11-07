package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.setting.ModeSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Module 185: Custom Crosshair
 * Editable Always and Hit-Range crosshairs (Cross, Dot, Circle, Bracket) with dynamic target coloring
 * matching Screenshot 1's "Crosshair" module card.
 */
public final class CustomCrosshair extends AbstractModule {

    private final ModeSetting shape = registerSetting(new ModeSetting(
            "crosshair_shape", "Shape", "Crosshair geometry style",
            "Plus + Dot", "Plus", "Plus + Dot", "Dot Only", "Circle", "Target Brackets"
    ));
    private final IntegerSetting size = registerSetting(new IntegerSetting(
            "crosshair_size", "Line Length", "Arm length in pixels", 4, 2, 12, 1, "px"
    ));
    private final IntegerSetting gap = registerSetting(new IntegerSetting(
            "crosshair_gap", "Center Gap", "Gap from center pixel", 2, 0, 8, 1, "px"
    ));
    private final IntegerSetting thickness = registerSetting(new IntegerSetting(
            "crosshair_thickness", "Thickness", "Line thickness in pixels", 1, 1, 3, 1, "px"
    ));
    private final ColorSetting normalColor = registerSetting(new ColorSetting(
            "crosshair_color", "Default Color", "Standard crosshair color", ColorData.WHITE, true
    ));
    private final BooleanSetting highlightOnTarget = registerSetting(new BooleanSetting(
            "highlight_on_target", "Hit Range Color", "Change crosshair color when aiming at an entity in range", true
    ));
    private final ColorSetting targetColor = registerSetting(new ColorSetting(
            "crosshair_target_color", "Target Color", "Color when aiming at a valid entity target", ColorData.RED_ACCENT, true
    ));
    private final BooleanSetting showOutline = registerSetting(new BooleanSetting(
            "crosshair_outline", "Black Outline", "Draw crisp 1px black outline for contrast on bright blocks", true
    ));

    public CustomCrosshair() {
        super(
                "custom_crosshair",
                "Crosshair",
                "Editable Always and Hit Range crosshairs.",
                ModuleCategory.VISUAL_GRAPHICS,
                true
        );
        targetColor.visibleWhen(highlightOnTarget::isEnabled);
    }

    public void renderCrosshair(DrawContext context, int screenWidth, int screenHeight) {
        int cx = screenWidth / 2;
        int cy = screenHeight / 2;

        boolean aimingEntity = highlightOnTarget.isEnabled() && (mc.crosshairTarget instanceof EntityHitResult);
        int col = (aimingEntity ? targetColor.getValue() : normalColor.getValue()).getArgb();
        int len = size.getValue();
        int g = gap.getValue();
        int t = thickness.getValue();
        String style = shape.getValue();

        if (style.equals("Dot Only") || style.equals("Plus + Dot")) {
            if (showOutline.isEnabled()) {
                context.fill(cx - 1, cy - 1, cx + t + 1, cy + t + 1, 0xCC000000);
            }
            context.fill(cx, cy, cx + t, cy + t, col);
        }

        if (style.equals("Plus") || style.equals("Plus + Dot")) {
            // Left & Right arms
            drawArm(context, cx - g - len, cy, len, t, col);
            drawArm(context, cx + t + g, cy, len, t, col);
            // Top & Bottom arms
            drawArm(context, cx, cy - g - len, t, len, col);
            drawArm(context, cx, cy + t + g, t, len, col);
        } else if (style.equals("Circle")) {
            Render2DUtil.drawCircleOutline(context, cx, cy, g + len, col);
        } else if (style.equals("Target Brackets")) {
            Render2DUtil.drawCornerAccentBracket(context, cx - g - len, cy - g - len, len, t, col);
            context.fill(cx + g, cy - g - len, cx + g + len, cy - g - len + t, col);
            context.fill(cx + g + len - t, cy - g - len, cx + g + len, cy - g, col);
            context.fill(cx - g - len, cy + g + len - t, cx - g, cy + g + len, col);
            context.fill(cx + g, cy + g + len - t, cx + g + len, cy + g + len, col);
        }
    }

    private void drawArm(DrawContext context, int x, int y, int w, int h, int color) {
        if (showOutline.isEnabled()) {
            context.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xAA000000);
        }
        context.fill(x, y, x + w, y + h, color);
    }
}
