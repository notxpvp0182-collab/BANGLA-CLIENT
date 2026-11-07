package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 165: Death Camera Effect Off
 * Controls or disables the 90-degree sideways camera tilt when the player dies.
 */
public final class DeathCameraEffectOff extends AbstractModule {

    private final BooleanSetting disableDeathRoll = registerSetting(new BooleanSetting(
            "disable_death_roll", "Keep Camera Level On Death", "Prevent the camera from tilting sideways on the death screen", true
    ));

    public DeathCameraEffectOff() {
        super(
                "death_camera_effect_off",
                "Death Camera Effect Off",
                "Prevents sideways camera tilt on the death screen.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public boolean shouldCancelDeathRoll() {
        return isEnabled() && disableDeathRoll.isEnabled();
    }
}
