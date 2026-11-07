package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public final class SmallPickaxe extends AbstractModule {
    private final FloatSetting pickaxeScale = registerSetting(new FloatSetting(
            "small_pickaxe_scale", "Pickaxe Scale", "Size multiplier when holding any Pickaxe", 0.70f, 0.25f, 1.25f, 0.05f, "x"
    ));

    public SmallPickaxe() {
        super("small_pickaxe", "Small Pickaxe", "Reduces held pickaxe size in first-person view.", ModuleCategory.SMALL_ITEM_SYSTEM, false);
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.isIn(ItemTags.PICKAXES);
    }

    public float getPickaxeScale() {
        return pickaxeScale.getValue();
    }
}
