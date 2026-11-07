package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

/**
 * Module 215: Small Food
 * Reduces held food visual size in first-person view so eating Golden Apples or Steak doesn't block center vision.
 */
public final class SmallFood extends AbstractModule {

    private final FloatSetting foodScale = registerSetting(new FloatSetting(
            "small_food_scale", "Food Scale", "Size multiplier when holding edible items", 0.65f, 0.25f, 1.25f, 0.05f, "x"
    ));

    public SmallFood() {
        super(
                "small_food",
                "Small Food",
                "Reduces held food visual size while eating.",
                ModuleCategory.SMALL_ITEM_SYSTEM,
                false
        );
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.get(DataComponentTypes.FOOD) != null;
    }

    public float getFoodScale() {
        return foodScale.getValue();
    }
}
