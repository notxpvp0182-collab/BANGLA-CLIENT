package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 169: Resolution Control
 * Controls configurable GUI scale and fullscreen resolution target.
 */
public final class ResolutionControl extends AbstractModule {

    private final IntegerSetting preferredGuiScale = registerSetting(new IntegerSetting(
            "preferred_gui_scale", "Preferred GUI Scale", "Minecraft GUI scale factor (0 = Auto, 2 = Competitive Standard)", 2, 0, 4, 1, "x"
    ));

    public ResolutionControl() {
        super(
                "resolution_control",
                "Resolution Control",
                "Controls configurable render and GUI resolution scale.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
        preferredGuiScale.onChange((oldV, newV) -> applyScale(newV));
    }

    @Override
    public void onEnable() {
        applyScale(preferredGuiScale.getValue());
    }

    private void applyScale(int scale) {
        if (mc.options != null) {
            mc.options.getGuiScale().setValue(scale);
            mc.onResolutionChanged();
        }
    }
}
