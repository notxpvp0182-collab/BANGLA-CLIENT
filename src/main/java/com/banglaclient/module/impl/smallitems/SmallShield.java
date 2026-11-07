package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Module 217: Small Shield
 * Reduces shield screen coverage and lowers the shield vertical offset in first-person view.
 */
public final class SmallShield extends AbstractModule {

    private final FloatSetting shieldScale = registerSetting(new FloatSetting(
            "small_shield_scale", "Shield Scale", "Size multiplier when holding a Shield", 0.75f, 0.30f, 1.1f, 0.05f, "x"
    ));
    private final FloatSetting verticalDrop = registerSetting(new FloatSetting(
            "small_shield_drop", "Vertical Drop", "Lower the shield vertically so it never blocks crosshair sightlines", -0.20f, -0.50f, 0.1f, 0.05f, ""
    ));

    public SmallShield() {
        super(
                "small_shield",
                "Small Shield",
                "Reduces shield screen coverage and lowers it out of sight.",
                ModuleCategory.SMALL_ITEM_SYSTEM,
                true
        );
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.isOf(Items.SHIELD);
    }

    public float getShieldScale() { return shieldScale.getValue(); }
    public float getVerticalDrop() { return verticalDrop.getValue(); }
}
