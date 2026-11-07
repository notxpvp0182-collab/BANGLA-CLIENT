package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public final class SmallShovel extends AbstractModule {
    private final FloatSetting shovelScale = registerSetting(new FloatSetting(
            "small_shovel_scale", "Shovel Scale", "Size multiplier when holding any Shovel", 0.70f, 0.25f, 1.25f, 0.05f, "x"
    ));

    public SmallShovel() {
        super("small_shovel", "Small Shovel", "Reduces held shovel size in first-person view.", ModuleCategory.SMALL_ITEM_SYSTEM, false);
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.isIn(ItemTags.SHOVELS);
    }

    public float getShovelScale() {
        return shovelScale.getValue();
    }
}
