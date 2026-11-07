package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;

/**
 * Module 117: Shield Indicator
 * Displays your shield state (Active Blocking, Ready, Axe Cooldown % or Missing) on a free-floating HUD.
 */
public final class ShieldIndicator extends HudModule {

    public ShieldIndicator() {
        super(
                "shield_indicator",
                "Shield Indicator",
                "Displays current shield blocking and cooldown state.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                316.0f,
                104.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null) {
            drawSimpleLabelValue(context, "Shield", "Ready");
            return;
        }
        boolean hasShield = mc.player.getOffHandStack().isOf(Items.SHIELD) || mc.player.getMainHandStack().isOf(Items.SHIELD);
        if (!hasShield) {
            drawSimpleLabelValue(context, "Shield", "None");
            return;
        }
        float cd = mc.player.getItemCooldownManager().getCooldownProgress(Items.SHIELD.getDefaultStack(), tickDelta);
        if (cd > 0.0f) {
            drawSimpleLabelValue(context, "Shield", String.format("Disabled (%.0f%%)", cd * 100.0f));
        } else if (mc.player.isBlocking()) {
            drawSimpleLabelValue(context, "Shield", "BLOCKING");
        } else {
            drawSimpleLabelValue(context, "Shield", "READY");
        }
    }
}
