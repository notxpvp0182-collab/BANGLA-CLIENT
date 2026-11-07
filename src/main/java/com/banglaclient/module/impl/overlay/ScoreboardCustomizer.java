package com.banglaclient.module.impl.overlay;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 256: Scoreboard Customizer
 * Customizes sidebar scoreboard position, scale, red score numbers, and background appearance.
 */
public final class ScoreboardCustomizer extends AbstractModule {

    private final BooleanSetting hideRedNumbers = registerSetting(new BooleanSetting(
            "hide_red_numbers", "Hide Red Score Numbers", "Hide the right-hand red line numbers on sidebar scoreboards", true
    ));
    private final FloatSetting scoreboardScale = registerSetting(new FloatSetting(
            "scoreboard_scale", "Scoreboard Scale", "Size multiplier for the sidebar scoreboard", 1.0f, 0.5f, 1.5f, 0.05f, "x"
    ));
    private final IntegerSetting verticalOffset = registerSetting(new IntegerSetting(
            "scoreboard_y_offset", "Vertical Offset", "Vertical pixel offset from center-right", 0, -200, 200, 4, "px"
    ));
    private final ColorSetting backgroundColor = registerSetting(new ColorSetting(
            "scoreboard_bg_color", "Background Color", "Backdrop color for the sidebar scoreboard", new ColorData(14, 14, 18, 160), true
    ));

    public ScoreboardCustomizer() {
        super(
                "scoreboard_customizer",
                "Scoreboard Customizer",
                "Customizes scoreboard position, scale, and red numbers.",
                ModuleCategory.SCOREBOARD_TABLIST_NAMETAG,
                true
        );
    }

    public boolean isHideRedNumbers() { return isEnabled() && hideRedNumbers.isEnabled(); }
    public float getScoreboardScale() { return isEnabled() ? scoreboardScale.getValue() : 1.0f; }
    public int getVerticalOffset() { return isEnabled() ? verticalOffset.getValue() : 0; }
    public ColorData getBackgroundColor() { return backgroundColor.getValue(); }
}
