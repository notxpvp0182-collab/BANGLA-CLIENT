package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

/**
 * Module 108: Weapon Cooldown HUD
 * Displays the currently held weapon icon alongside its exact charge percentage and item cooldown status.
 */
public final class WeaponCooldownHud extends HudModule {

    public WeaponCooldownHud() {
        super(
                "weapon_cooldown_hud",
                "Weapon Cooldown HUD",
                "Shows current weapon icon and attack readiness.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                288.0f,
                92.0f,
                24.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 92.0f;
        this.unscaledHeight = 24.0f;

        if (mc.player == null || mc.textRenderer == null) {
            return;
        }
        ItemStack held = mc.player.getMainHandStack();
        if (!held.isEmpty()) {
            context.drawItem(held, 4, 4);
        }
        float charge = mc.player.getAttackCooldownProgress(tickDelta);
        String text = charge >= 0.99f ? "100% Ready" : String.format("%.0f%%", charge * 100.0f);
        context.drawText(mc.textRenderer, text, 24, 8, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
    }
}
