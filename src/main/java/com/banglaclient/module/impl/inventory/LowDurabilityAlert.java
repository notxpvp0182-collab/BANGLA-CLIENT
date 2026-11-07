package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Module 203: Low Durability Alert
 * Warns the player on a free-floating HUD banner when held tools or equipped armor drop below
 * a configurable durability percentage threshold.
 */
public final class LowDurabilityAlert extends HudModule {

    private final IntegerSetting thresholdPercent = registerSetting(new IntegerSetting(
            "low_dura_threshold_pct", "Warning Threshold", "Alert when item durability drops below this percentage", 15, 5, 40, 5, "%"
    ));

    private static final EquipmentSlot[] CHECK_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.MAINHAND,
            EquipmentSlot.OFFHAND,
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public LowDurabilityAlert() {
        super(
                "low_durability_alert",
                "Low Durability Alert",
                "Warns when equipped armor or held tool durability is low.",
                ModuleCategory.INVENTORY_ITEMS,
                true,
                380.0f,
                210.0f,
                148.0f,
                22.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        ItemStack lowestStack = ItemStack.EMPTY;
        int lowestPct = 100;

        if (mc.player != null) {
            for (EquipmentSlot slot : CHECK_SLOTS) {
                ItemStack s = mc.player.getEquippedStack(slot);
                if (!s.isEmpty() && s.isDamageable()) {
                    int max = Math.max(1, s.getMaxDamage());
                    int rem = Math.max(0, max - s.getDamage());
                    int pct = (rem * 100) / max;
                    if (pct <= thresholdPercent.getValue() && pct < lowestPct) {
                        lowestPct = pct;
                        lowestStack = s;
                    }
                }
            }
        }

        if (!lowestStack.isEmpty()) {
            drawSimpleLabelValue(context, "LOW DURABILITY", lowestStack.getName().getString() + " (" + lowestPct + "%)");
        } else if (isEditing) {
            drawSimpleLabelValue(context, "LOW DURABILITY", "Helmet (12%)");
        } else {
            this.unscaledWidth = 0.0f;
            this.unscaledHeight = 0.0f;
        }
    }
}
