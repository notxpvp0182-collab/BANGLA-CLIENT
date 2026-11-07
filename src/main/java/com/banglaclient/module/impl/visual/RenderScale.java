package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 168: Render Scale
 * Adjusts 3D world rendering resolution scale multiplier for low-end GPU fillrate relief.
 */
public final class RenderScale extends AbstractModule {

    private final FloatSetting scaleFactor = registerSetting(new FloatSetting(
            "render_scale_factor", "Resolution Scale", "3D framebuffer resolution multiplier (1.0 = Native 100%)", 1.0f, 0.5f, 1.0f, 0.05f, "x"
    ));

    public RenderScale() {
        super(
                "render_scale",
                "Render Scale",
                "Adjusts 3D rendering resolution scale for low-end GPUs.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getScaleFactor() {
        return isEnabled() ? scaleFactor.getValue() : 1.0f;
    }
}
