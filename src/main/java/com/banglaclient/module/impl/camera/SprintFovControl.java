package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 156: Sprint FOV Control
 * Controls how much sprinting widens the camera Field of View without affecting bow or potion FOV.
 */
public final class SprintFovControl extends AbstractModule {

    private final FloatSetting sprintFovMultiplier = registerSetting(new FloatSetting(
            "sprint_fov_mult", "Sprint FOV Scale", "Multiplier for the sprinting FOV boost (0.0 = No Sprint Zoom)", 0.5f, 0.0f, 2.0f, 0.05f, "x"
    ));

    public SprintFovControl() {
        super(
                "sprint_fov_control",
                "Sprint FOV Control",
                "Controls the sprinting Field of View widening effect.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getSprintFovMultiplier() {
        return isEnabled() ? sprintFovMultiplier.getValue() : 1.0f;
    }
}
