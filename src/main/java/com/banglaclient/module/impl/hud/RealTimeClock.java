package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Module 150: Real-Time Clock
 * Displays real-world local system time in 12-hour or 24-hour format.
 */
public final class RealTimeClock extends HudModule {

    private final BooleanSetting use24HourFormat = registerSetting(new BooleanSetting(
            "use_24h_format", "24-Hour Format", "Use 24-hour clock instead of 12-hour AM/PM", false
    ));
    private final BooleanSetting showSeconds = registerSetting(new BooleanSetting(
            "show_seconds", "Show Seconds", "Include seconds in the clock display", true
    ));

    private static final DateTimeFormatter FMT_12_SEC = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private static final DateTimeFormatter FMT_12_MIN = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter FMT_24_SEC = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FMT_24_MIN = DateTimeFormatter.ofPattern("HH:mm");

    public RealTimeClock() {
        super(
                "real_time_clock",
                "Real-Time Clock",
                "Displays real-world local time.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                520.0f,
                96.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        DateTimeFormatter fmt;
        if (use24HourFormat.isEnabled()) {
            fmt = showSeconds.isEnabled() ? FMT_24_SEC : FMT_24_MIN;
        } else {
            fmt = showSeconds.isEnabled() ? FMT_12_SEC : FMT_12_MIN;
        }
        drawSimpleLabelValue(context, "", LocalTime.now().format(fmt));
    }
}
