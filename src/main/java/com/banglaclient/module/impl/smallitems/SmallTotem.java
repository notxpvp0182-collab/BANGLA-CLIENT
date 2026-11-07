package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Module 218: Small Totem
 * Reduces held Totem of Undying visual size in the offhand/mainhand.
 */
public final class SmallTotem extends AbstractModule {

    private final FloatSetting totemScale = registerSetting(new FloatSetting(
            "small_totem_scale", "Totem Scale", "Size multiplier when holding a Totem of Undying", 0.65f, 0.20f, 1.1f, 0.05f, "x"
    ));
    private final FloatSetting verticalDrop = registerSetting(new FloatSetting(
            "small_totem_drop", "Vertical Offset", "Lower offhand Totem vertically", -0.15f, -0.50f, 0.1f, 0.05f, ""
    ));

    public SmallTotem() {
        super(
                "small_totem",
                "Small Totem",
                "Reduces held Totem visual size in first-person view.",
                ModuleCategory.SMALL_ITEM_SYSTEM,
                true
        );
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.isOf(Items.TOTEM_OF_UNDYING);
    }

    public float getTotemScale() { return totemScale.getValue(); }
    public float getVerticalDrop() { return verticalDrop.getValue(); }
}
