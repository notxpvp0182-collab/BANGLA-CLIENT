package com.banglaclient.module.impl.performance;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 058: Particle Counter
 * Shows active particle count reported by Minecraft's ParticleManager.
 */
public final class ParticleCounter extends HudModule {

    public ParticleCounter() {
        super(
                "particle_counter",
                "Particle Counter",
                "Shows active particle count.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                282.0f,
                92.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        String debugCount = (mc.particleManager != null) ? mc.particleManager.getDebugString() : "0";
        drawSimpleLabelValue(context, "Particles", debugCount);
    }
}
