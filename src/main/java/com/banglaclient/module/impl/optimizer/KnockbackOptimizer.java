package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 042: Knockback Optimizer
 * Optimizes client-side knockback camera/visual interpolation without changing actual
 * server-authoritative velocity or knockback physics.
 */
public final class KnockbackOptimizer extends AbstractModule {

    private final BooleanSetting smoothCameraJolt = registerSetting(new BooleanSetting(
            "smooth_camera_jolt", "Smooth Velocity Camera", "Interpolate sudden camera view-bob jolts during rapid hits", true
    ));
    private final FloatSetting visualDampingFactor = registerSetting(new FloatSetting(
            "visual_damping", "Visual Jitter Damping", "Camera roll damping factor during knockback (visual only)", 0.65f, 0.0f, 1.0f, 0.05f, ""
    ));

    public KnockbackOptimizer() {
        super(
                "knockback_optimizer",
                "Knockback Optimizer",
                "Smooths client-side camera jitter during knockback (visual only).",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isSmoothCameraJolt() {
        return isEnabled() && smoothCameraJolt.isEnabled();
    }

    public float getVisualDampingFactor() {
        return isEnabled() ? visualDampingFactor.getValue() : 1.0f;
    }
}
