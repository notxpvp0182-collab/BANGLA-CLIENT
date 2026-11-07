package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 166: Viewmodel Editor
 * Controls first-person held-item position (X/Y/Z), rotation (Pitch/Yaw/Roll), and uniform scale.
 */
public final class ViewmodelEditor extends AbstractModule {

    private final FloatSetting offsetX = registerSetting(new FloatSetting(
            "vm_offset_x", "Offset X", "Horizontal held-item offset", 0.0f, -1.5f, 1.5f, 0.05f, ""
    ));
    private final FloatSetting offsetY = registerSetting(new FloatSetting(
            "vm_offset_y", "Offset Y", "Vertical held-item offset", 0.0f, -1.5f, 1.5f, 0.05f, ""
    ));
    private final FloatSetting offsetZ = registerSetting(new FloatSetting(
            "vm_offset_z", "Offset Z", "Depth held-item offset", 0.0f, -1.5f, 1.5f, 0.05f, ""
    ));
    private final FloatSetting scale = registerSetting(new FloatSetting(
            "vm_scale", "Overall Scale", "Uniform size multiplier for held items", 1.0f, 0.25f, 2.0f, 0.05f, "x"
    ));
    private final FloatSetting rotPitch = registerSetting(new FloatSetting(
            "vm_rot_pitch", "Rotation Pitch", "Pitch rotation in degrees", 0.0f, -180.0f, 180.0f, 2.0f, "°"
    ));
    private final FloatSetting rotYaw = registerSetting(new FloatSetting(
            "vm_rot_yaw", "Rotation Yaw", "Yaw rotation in degrees", 0.0f, -180.0f, 180.0f, 2.0f, "°"
    ));
    private final FloatSetting rotRoll = registerSetting(new FloatSetting(
            "vm_rot_roll", "Rotation Roll", "Roll rotation in degrees", 0.0f, -180.0f, 180.0f, 2.0f, "°"
    ));

    public ViewmodelEditor() {
        super(
                "viewmodel_editor",
                "Viewmodel Editor",
                "Controls held-item position, rotation, and size.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getOffsetX() { return offsetX.getValue(); }
    public float getOffsetY() { return offsetY.getValue(); }
    public float getOffsetZ() { return offsetZ.getValue(); }
    public float getScale() { return scale.getValue(); }
    public float getRotPitch() { return rotPitch.getValue(); }
    public float getRotYaw() { return rotYaw.getValue(); }
    public float getRotRoll() { return rotRoll.getValue(); }
}
