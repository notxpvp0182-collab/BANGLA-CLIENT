package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 149: Moon Phase
 * Displays the current lunar phase name and slime-spawn fullness percentage.
 */
public final class MoonPhase extends HudModule {

    private static final String[] PHASE_NAMES = new String[]{
            "Full Moon (100%)",
            "Waning Gibbous (75%)",
            "Third Quarter (50%)",
            "Waning Crescent (25%)",
            "New Moon (0%)",
            "Waxing Crescent (25%)",
            "First Quarter (50%)",
            "Waxing Gibbous (75%)"
    };

    public MoonPhase() {
        super(
                "moon_phase",
                "Moon Phase",
                "Displays current moon phase and fullness.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                496.0f,
                136.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int phase = (mc.world != null) ? (mc.world.getMoonPhase() & 7) : 0;
        drawSimpleLabelValue(context, "Moon", PHASE_NAMES[phase]);
    }
}
