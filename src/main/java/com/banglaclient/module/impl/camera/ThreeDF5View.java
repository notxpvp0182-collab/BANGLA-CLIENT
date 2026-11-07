package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 167: 3D F5 View
 * Adds customizable over-the-shoulder third-person viewing offsets and skips the front-facing F5 state if desired.
 */
public final class ThreeDF5View extends AbstractModule {

    private final FloatSetting shoulderOffsetX = registerSetting(new FloatSetting(
            "shoulder_offset_x", "Shoulder Offset X", "Horizontal over-the-shoulder camera offset in F5", 0.0f, -1.5f, 1.5f, 0.05f, "m"
    ));
    private final FloatSetting verticalOffsetY = registerSetting(new FloatSetting(
            "shoulder_offset_y", "Vertical Offset Y", "Vertical camera elevation offset in F5", 0.15f, -1.0f, 1.5f, 0.05f, "m"
    ));
    private final BooleanSetting skipFrontView = registerSetting(new BooleanSetting(
            "skip_front_view", "Skip Front F5", "Cycle directly between First-Person and Third-Person Back", false
    ));

    public ThreeDF5View() {
        super(
                "three_d_f5_view",
                "3D F5 View",
                "Adds customizable third-person shoulder viewing behavior.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getShoulderOffsetX() { return isEnabled() ? shoulderOffsetX.getValue() : 0.0f; }
    public float getVerticalOffsetY() { return isEnabled() ? verticalOffsetY.getValue() : 0.0f; }
    public boolean isSkipFrontView() { return isEnabled() && skipFrontView.isEnabled(); }
}
