package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 107: Attack Cooldown HUD
 * Shows a sleek progress bar and percentage for the 1.9+ / 1.21 weapon attack cooldown.
 */
public final class AttackCooldownHud extends HudModule {

    private final ColorSetting barColor = registerSetting(new ColorSetting(
            "cooldown_bar_color", "Charge Bar Color", "Color of the attack readiness progress bar", ColorData.RED_ACCENT, true
    ));

    public AttackCooldownHud() {
        super(
                "attack_cooldown_hud",
                "Attack Cooldown HUD",
                "Shows weapon attack cooldown progress bar.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                260.0f,
                84.0f,
                22.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 84.0f;
        this.unscaledHeight = 22.0f;

        float progress = (mc.player != null) ? mc.player.getAttackCooldownProgress(tickDelta) : 1.0f;
        int pct = Math.round(progress * 100.0f);

        if (mc.textRenderer != null) {
            String label = pct >= 100 ? "READY" : (pct + "%");
            int tw = mc.textRenderer.getWidth(label);
            context.drawText(mc.textRenderer, label, Math.round((unscaledWidth - tw) / 2.0f), 3,
                    hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        }

        Render2DUtil.drawRoundedRect(context, 6.0f, 14.0f, unscaledWidth - 12.0f, 4.0f, 2.0f, 0xFF1A1A22);
        Render2DUtil.drawRoundedRect(context, 6.0f, 14.0f, Math.max(2.0f, (unscaledWidth - 12.0f) * progress), 4.0f, 2.0f,
                barColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb());
    }
}
