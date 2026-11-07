package com.banglaclient;

import com.banglaclient.theme.ColorData;
import com.banglaclient.theme.Theme;
import com.banglaclient.theme.ThemeManager;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies theme palette JSON round-trip, HEX parsing and active theme persistence.
 */
public class ThemeSerializationTest {

    @Test
    void verifyCustomPaletteRoundTrip() {
        ThemeManager manager = ThemeManager.getInstance();
        Theme custom = manager.getCustomTheme();
        ColorData originalAccent = custom.getPalette().getAccent();

        try {
            custom.getPalette().setAccent(new ColorData(12, 200, 99, 180));
            manager.setActiveTheme(custom.getId());

            JsonObject saved = manager.saveToJson();

            custom.getPalette().setAccent(ColorData.WHITE);
            manager.setActiveTheme("default_red");

            manager.loadFromJson(saved);

            assertEquals(custom.getId(), manager.getActiveTheme().getId(), "Active theme id must be restored");
            ColorData restored = manager.getCustomTheme().getPalette().getAccent();
            assertEquals(12, restored.getRed());
            assertEquals(200, restored.getGreen());
            assertEquals(99, restored.getBlue());
            assertEquals(180, restored.getAlpha());
        } finally {
            custom.getPalette().setAccent(originalAccent);
        }
    }

    @Test
    void verifyHexParsingAndFormatting() {
        ColorData parsed = ColorData.fromHex("#E52521");
        assertEquals(229, parsed.getRed());
        assertEquals(37, parsed.getGreen());
        assertEquals(33, parsed.getBlue());
        assertEquals(255, parsed.getAlpha());
    }

    @Test
    void verifyThemeListContainsBuiltInAndCustom() {
        assertTrue(ThemeManager.getInstance().getAllThemes().size() >= 4,
                "Must include Default Red, Dark Red, Black & White + Red and Custom themes");
    }
}
