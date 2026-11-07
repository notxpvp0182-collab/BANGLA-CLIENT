package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 115: Knockback Camera Control
 * Controls visual camera FOV and view-bobbing movement during knockback without altering
 * server-authoritative player velocity.
 */
public final class KnockbackCameraControl extends AbstractModule {

    private final FloatSetting cameraBobMultiplier = registerSetting(new FloatSetting(
            "kb_camera_bob_mult", "Knockback Bob Scale", "Visual camera bobbing multiplier when knocked back", 0.35f, 0.0f, 1.0f, 0.05f, "x"
    ));

    public KnockbackCameraControl() {
        super(
                "knockback_camera_control",
                "Knockback Camera Control",
                "Controls visual camera movement during knockback.",
                ModuleCategory.PVP_COMBAT,
                false
        );
    }

    public float getCameraBobMultiplier() {
        return isEnabled() ? cameraBobMultiplier.getValue() : 1.0f;
    }
}
