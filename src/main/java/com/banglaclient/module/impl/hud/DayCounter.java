package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 148: Day Counter
 * Displays the current in-game Minecraft world day number (worldTime / 24000L).
 */
public final class DayCounter extends HudModule {

    public DayCounter() {
        super(
                "day_counter",
                "Day Counter",
                "Displays current Minecraft world day number.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                472.0f,
                78.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        long day = (mc.world != null) ? (mc.world.getTimeOfDay() / 24000L) : 1L;
        drawSimpleLabelValue(context, "Day", String.valueOf(day));
    }
}
