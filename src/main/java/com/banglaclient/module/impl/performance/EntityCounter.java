package com.banglaclient.module.impl.performance;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 057: Entity Counter
 * Shows loaded world entity count on a free-floating HUD element.
 */
public final class EntityCounter extends HudModule {

    public EntityCounter() {
        super(
                "entity_counter",
                "Entity Counter",
                "Shows loaded/rendered entity counts.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                258.0f,
                86.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int count = (mc.world != null) ? mc.world.getRegularEntityCount() : 0;
        drawSimpleLabelValue(context, "Entities", String.valueOf(count));
    }
}
