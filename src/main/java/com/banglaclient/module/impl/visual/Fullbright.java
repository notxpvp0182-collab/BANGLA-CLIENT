package com.banglaclient.module.impl.visual;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.setting.ModeSetting;
import org.lwjgl.glfw.GLFW;

/**
 * Module 174: Fullbright
 * Improves dark-area and cave visibility through client-side lightmap/gamma adjustments (Screenshot 1 module).
 */
public final class Fullbright extends AbstractModule {

    private final ModeSetting mode = registerSetting(new ModeSetting(
            "fullbright_mode", "Mode", "Method used to illuminate dark areas", "Lightmap Gamma", "Lightmap Gamma", "Ambient Boost"
    ));
    private final FloatSetting strength = registerSetting(new FloatSetting(
            "fullbright_strength", "Strength", "Illumination strength in dark caves", 10.0f, 2.0f, 16.0f, 1.0f, "x"
    ));

    public Fullbright() {
        super(
                "fullbright",
                "Fullbright",
                "Increases game brightness for clear cave visibility.",
                ModuleCategory.VISUAL_GRAPHICS,
                true
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_G));
    }

    public float getEffectiveGamma() {
        return isEnabled() ? strength.getValue() : 1.0f;
    }

    public String getMode() {
        return mode.getValue();
    }
}
