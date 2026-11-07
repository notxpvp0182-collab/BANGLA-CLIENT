package com.banglaclient.module.impl.hud;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 136: Ping Display
 * Displays current network latency in milliseconds on a free-floating HUD element.
 */
public final class PingDisplay extends HudModule {

    private final ModeSetting format = registerSetting(new ModeSetting(
            "ping_format", "Format", "Text format for latency display", "42 ms", "42 ms", "Ping: 42ms", "[42 ms]"
    ));

    public PingDisplay() {
        super(
                "ping_display",
                "Ping Display",
                "Displays current network latency in milliseconds.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                160.0f,
                78.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int ping = PerformanceTracker.getInstance().getCurrentPing();
        String text = switch (format.getValue()) {
            case "Ping: 42ms" -> "Ping: " + ping + "ms";
            case "[42 ms]" -> "[" + ping + " ms]";
            default -> ping + " ms";
        };
        drawSimpleLabelValue(context, "", text);
    }
}
