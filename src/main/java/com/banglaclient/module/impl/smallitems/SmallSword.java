package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public final class SmallSword extends AbstractModule {
    private final FloatSetting swordScale = registerSetting(new FloatSetting(
            "small_sword_scale", "Sword Scale", "Size multiplier when holding any Sword", 0.70f, 0.25f, 1.25f, 0.05f, "x"
    ));

    public SmallSword() {
        super("small_sword", "Small Sword", "Reduces held sword size in first-person view.", ModuleCategory.SMALL_ITEM_SYSTEM, false);
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.isIn(ItemTags.SWORDS);
    }

    public float getSwordScale() {
        return swordScale.getValue();
    }
}
