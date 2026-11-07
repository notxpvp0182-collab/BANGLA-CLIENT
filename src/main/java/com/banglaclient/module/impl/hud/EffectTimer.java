package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;

import java.util.Collection;

/**
 * Module 144: Effect Timer
 * Feather-style compact active effect countdown timer badge (Screenshot 1 reference module).
 */
public final class EffectTimer extends HudModule {

    private final BooleanSetting blinkWhenExpiring = registerSetting(new BooleanSetting(
            "blink_when_expiring", "Blink Under 10s", "Flash timer text when an effect has less than 10 seconds remaining", true
    ));

    public EffectTimer() {
        super(
                "effect_timer",
                "Effect Timer",
                "Feather-style active effect icons and timers.",
                ModuleCategory.HUD_INFORMATION,
                true,
                320.0f,
                332.0f,
                118.0f,
                22.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null || mc.player.getStatusEffects().isEmpty()) {
            drawSimpleLabelValue(context, "Effects", isEditing ? "2 Active (0:45)" : "0 Active");
            return;
        }
        Collection<StatusEffectInstance> effects = mc.player.getStatusEffects();
        StatusEffectInstance shortest = null;
        for (StatusEffectInstance inst : effects) {
            if (shortest == null || inst.getDuration() < shortest.getDuration()) {
                shortest = inst;
            }
        }
        if (shortest != null) {
            if (blinkWhenExpiring.isEnabled() && shortest.getDuration() <= 200 && (System.currentTimeMillis() / 250L) % 2L == 0L) {
                drawSimpleLabelValue(context, "", "EXPIRING SOON!");
                return;
            }
            String name = shortest.getEffectType().value().getName().getString();
            String dur = StatusEffectUtil.getDurationText(shortest, 1.0f, 20.0f).getString();
            drawSimpleLabelValue(context, name, dur);
        }
    }
}
