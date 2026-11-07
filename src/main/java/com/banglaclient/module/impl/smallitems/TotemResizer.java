package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 219: Totem Resizer
 * Allows exact percentage-based resizing (10% to 150%) of both held Totems and the floating 3D Totem pop animation.
 */
public final class TotemResizer extends AbstractModule {

    private final IntegerSetting heldPercentage = registerSetting(new IntegerSetting(
            "totem_held_pct", "Held Totem Size", "Percentage size of held Totem of Undying", 60, 15, 150, 5, "%"
    ));
    private final IntegerSetting popAnimationPercentage = registerSetting(new IntegerSetting(
            "totem_pop_anim_pct", "Pop Animation Size", "Percentage size of the screen-center Totem pop model", 50, 10, 120, 5, "%"
    ));

    public TotemResizer() {
        super(
                "totem_resizer",
                "Totem Resizer",
                "Allows percentage-based held and pop-animation Totem resizing.",
                ModuleCategory.SMALL_ITEM_SYSTEM,
                false
        );
    }

    public float getHeldScaleMultiplier() {
        return isEnabled() ? (heldPercentage.getValue() / 100.0f) : 1.0f;
    }

    public float getPopAnimationScaleMultiplier() {
        return isEnabled() ? (popAnimationPercentage.getValue() / 100.0f) : 1.0f;
    }
}
