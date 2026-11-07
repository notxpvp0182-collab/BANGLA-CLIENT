package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 154: Camera Collision Fix
 * Prevents the third-person camera from violently snapping forward when passing through
 * non-solid visual blocks such as tall grass, flowers, cobwebs, or open fence gates.
 */
public final class CameraCollisionFix extends AbstractModule {

    private final BooleanSetting ignorePassableFoliage = registerSetting(new BooleanSetting(
            "ignore_passable_foliage", "Ignore Non-Solid Foliage", "Prevent F5 camera snapping on grass, flowers, and vines", true
    ));
    private final BooleanSetting ignoreGlassPanes = registerSetting(new BooleanSetting(
            "ignore_glass_panes", "Smooth Thin Pane Clipping", "Reduce sudden camera jumps near thin glass panes and iron bars", true
    ));

    public CameraCollisionFix() {
        super(
                "camera_collision_fix",
                "Camera Collision Fix",
                "Prevents third-person camera snapping on non-solid foliage.",
                ModuleCategory.CAMERA_VIEWMODEL,
                true
        );
    }

    public boolean isIgnorePassableFoliage() {
        return isEnabled() && ignorePassableFoliage.isEnabled();
    }

    public boolean isIgnoreGlassPanes() {
        return isEnabled() && ignoreGlassPanes.isEnabled();
    }
}
