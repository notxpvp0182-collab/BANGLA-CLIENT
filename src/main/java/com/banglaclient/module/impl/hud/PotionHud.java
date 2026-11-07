package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;

import java.util.Collection;

/**
 * Module 143: Potion HUD
 * Displays active potion effects, amplifier levels, and countdown timers on a free-floating HUD list.
 */
public final class PotionHud extends HudModule {

    private final BooleanSetting colorByEffect = registerSetting(new BooleanSetting(
            "color_by_effect", "Use Potion Color", "Tint effect name with the status effect's native color", true
    ));

    public PotionHud() {
        super(
                "potion_hud",
                "Potion HUD",
                "Displays active potion effects and timers.",
                ModuleCategory.HUD_INFORMATION,
                false,
                14.0f,
                340.0f,
                132.0f,
                46.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;

        Collection<StatusEffectInstance> effects = (mc.player != null) ? mc.player.getStatusEffects() : java.util.List.of();
        if (effects.isEmpty()) {
            this.unscaledWidth = 122.0f;
            this.unscaledHeight = 26.0f;
            String demo = isEditing ? "Speed II (01:24)" : "No Active Effects";
            context.drawText(mc.textRenderer, demo, 6, 9, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
            return;
        }

        int maxW = 100;
        int row = 0;
        for (StatusEffectInstance inst : effects) {
            String name = inst.getEffectType().value().getName().getString();
            int amp = inst.getAmplifier() + 1;
            String dur = StatusEffectUtil.getDurationText(inst, 1.0f, 20.0f).getString();
            String line = name + (amp > 1 ? (" " + amp) : "") + " (" + dur + ")";
            int lw = mc.textRenderer.getWidth(line);
            if (lw > maxW) maxW = lw;

            int color = colorByEffect.isEnabled()
                    ? (0xFF000000 | inst.getEffectType().value().getColor())
                    : hudStyle.getTextColor().getArgb();
            context.drawText(mc.textRenderer, line, 6, 5 + row * 12, color, hudStyle.isTextShadow());
            row++;
        }

        this.unscaledWidth = maxW + 14.0f;
        this.unscaledHeight = Math.max(22.0f, 8.0f + row * 12.0f);
    }
}
