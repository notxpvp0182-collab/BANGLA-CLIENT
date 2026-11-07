package com.banglaclient.module.impl.hud;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 147: Play Time Counter
 * Displays elapsed session play time in HH:MM:SS format.
 */
public final class PlayTimeCounter extends HudModule {

    public PlayTimeCounter() {
        super(
                "play_time_counter",
                "Play Time Counter",
                "Displays current session play time.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                448.0f,
                104.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        long totalSeconds = ClientCore.getInstance().getSessionDurationSeconds();
        long hours = totalSeconds / 3600L;
        long minutes = (totalSeconds % 3600L) / 60L;
        long seconds = totalSeconds % 60L;
        String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        drawSimpleLabelValue(context, "Session", formatted);
    }
}
