package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 159: Damage FOV Control
 * Prevents sudden FOV micro-snaps when taking damage that briefly interrupts sprint state.
 */
public final class DamageFovControl extends AbstractModule {

    private final BooleanSetting lockFovDuringHurtTime = registerSetting(new BooleanSetting(
            "lock_fov_hurt_time", "Stabilize Hurt FOV", "Preserve pre-hit FOV multiplier during hurtTicks to prevent aim snap", true
    ));

    public DamageFovControl() {
        super(
                "damage_fov_control",
                "Damage FOV Control",
                "Stabilizes FOV changes when taking combat damage.",
                ModuleCategory.CAMERA_VIEWMODEL,
                true
        );
    }

    public boolean shouldLockFovOnHurt() {
        return isEnabled() && lockFovDuringHurtTime.isEnabled() && mc.player != null && mc.player.hurtTime > 0;
    }
}
