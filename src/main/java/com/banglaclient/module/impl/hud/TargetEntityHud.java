package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Module 132: Target Entity HUD
 * Displays the name, health bar, exact HP, and distance of the entity targeted by your crosshair.
 */
public final class TargetEntityHud extends HudModule {

    private final ColorSetting healthBarColor = registerSetting(new ColorSetting(
            "target_hp_color", "Health Bar Color", "Color of the target health bar", ColorData.RED_ACCENT, true
    ));

    private LivingEntity lastTargetedLiving = null;
    private long lastTargetTimestampMillis = 0L;

    public TargetEntityHud() {
        super(
                "target_entity_hud",
                "Target Entity HUD",
                "Displays health and distance of the targeted entity.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                48.0f,
                142.0f,
                34.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 142.0f;
        this.unscaledHeight = 34.0f;

        if (mc.crosshairTarget instanceof EntityHitResult ehr) {
            Entity entity = ehr.getEntity();
            if (entity instanceof LivingEntity living) {
                lastTargetedLiving = living;
                lastTargetTimestampMillis = System.currentTimeMillis();
            }
        }

        boolean active = lastTargetedLiving != null
                && lastTargetedLiving.isAlive()
                && (System.currentTimeMillis() - lastTargetTimestampMillis) <= 2500L;

        if (!active && !isEditing) {
            drawSimpleLabelValue(context, "Target", "None");
            return;
        }

        String name = active ? lastTargetedLiving.getName().getString() : "BanglaSMP_Player";
        float hp = active ? lastTargetedLiving.getHealth() : 20.0f;
        float maxHp = active ? Math.max(1.0f, lastTargetedLiving.getMaxHealth()) : 20.0f;
        double dist = (active && mc.player != null) ? mc.player.distanceTo(lastTargetedLiving) : 2.85;

        String header = mc.textRenderer.trimToWidth(name, 90) + String.format(" (%.1fm)", dist);
        context.drawText(mc.textRenderer, header, 6, 5, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());

        String hpText = String.format("%.1f / %.1f HP", hp, maxHp);
        context.drawText(mc.textRenderer, hpText, 6, 15, 0xFF9E9EA8, false);

        float barW = unscaledWidth - 12.0f;
        float ratio = Math.min(1.0f, Math.max(0.0f, hp / maxHp));
        Render2DUtil.drawRoundedRect(context, 6.0f, 25.0f, barW, 5.0f, 2.0f, 0xFF181820);
        Render2DUtil.drawRoundedRect(context, 6.0f, 25.0f, Math.max(3.0f, barW * ratio), 5.0f, 2.0f,
                healthBarColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb());
    }
}
