package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 141: Health Bar HUD
 * Displays player health and absorption hearts on a sleek custom progress bar HUD.
 */
public final class HealthBarHud extends HudModule {

    private final ColorSetting healthColor = registerSetting(new ColorSetting(
            "hp_bar_color", "Health Fill Color", "Primary color of the health bar", ColorData.RED_ACCENT, true
    ));
    private final BooleanSetting showAbsorption = registerSetting(new BooleanSetting(
            "show_absorption", "Include Absorption", "Show golden absorption hearts alongside health", true
    ));

    public HealthBarHud() {
        super(
                "health_bar_hud",
                "Health Bar HUD",
                "Displays player health and absorption in a custom bar.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                302.0f,
                118.0f,
                26.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 118.0f;
        this.unscaledHeight = 26.0f;

        float hp = mc.player != null ? mc.player.getHealth() : 20.0f;
        float maxHp = mc.player != null ? Math.max(1.0f, mc.player.getMaxHealth()) : 20.0f;
        float abs = (mc.player != null && showAbsorption.isEnabled()) ? mc.player.getAbsorptionAmount() : 0.0f;

        String label = abs > 0.0f
                ? String.format("HP: %.1f (+%.1f)", hp, abs)
                : String.format("HP: %.1f / %.1f", hp, maxHp);
        context.drawText(mc.textRenderer, label, 6, 4, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());

        float barW = unscaledWidth - 12.0f;
        float ratio = Math.min(1.0f, Math.max(0.0f, hp / maxHp));
        Render2DUtil.drawRoundedRect(context, 6.0f, 16.0f, barW, 5.0f, 2.0f, 0xFF181820);
        Render2DUtil.drawRoundedRect(context, 6.0f, 16.0f, Math.max(3.0f, barW * ratio), 5.0f, 2.0f,
                healthColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb());
    }
}
