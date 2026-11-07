package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public final class SmallAxe extends AbstractModule {
    private final FloatSetting axeScale = registerSetting(new FloatSetting(
            "small_axe_scale", "Axe Scale", "Size multiplier when holding any Axe", 0.70f, 0.25f, 1.25f, 0.05f, "x"
    ));

    public SmallAxe() {
        super("small_axe", "Small Axe", "Reduces held axe size in first-person view.", ModuleCategory.SMALL_ITEM_SYSTEM, false);
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.isIn(ItemTags.AXES);
    }

    public float getAxeScale() {
        return axeScale.getValue();
    }
}
