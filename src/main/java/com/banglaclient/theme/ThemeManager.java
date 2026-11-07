package com.banglaclient.theme;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages BANGLACLIENT themes:
 * 1. Default Red (Signature Black + White + Crimson Red matching Screenshot 1)
 * 2. Dark Red (Deep Blood-Red & Obsidian)
 * 3. Black & White + Red (High-contrast monochrome with sharp red accents)
 * 4. Custom Theme (Fully editable 15-color palette)
 */
public final class ThemeManager {

    private static final ThemeManager INSTANCE = new ThemeManager();

    private final Map<String, Theme> themes = new LinkedHashMap<>();
    private Theme activeTheme;
    private Theme customTheme;

    private ThemeManager() {
        registerBuiltInThemes();
    }

    public static ThemeManager getInstance() {
        return INSTANCE;
    }

    private void registerBuiltInThemes() {
        ThemePalette defaultRedPalette = new ThemePalette(
                new ColorData(11, 11, 14, 248),     // background
                new ColorData(17, 17, 22, 255),     // panel
                new ColorData(23, 23, 29, 255),     // card
                new ColorData(42, 42, 52, 255),     // border
                new ColorData(229, 37, 33, 255),    // accent
                new ColorData(184, 29, 26, 255),    // hover
                new ColorData(235, 58, 36, 255),    // enabled
                new ColorData(28, 28, 35, 255),     // disabled
                new ColorData(255, 255, 255, 255),  // primaryText
                new ColorData(156, 158, 172, 255),  // secondaryText
                new ColorData(229, 37, 33, 210),    // scrollbar
                new ColorData(255, 255, 255, 255),  // icon
                new ColorData(235, 58, 36, 255),    // graph
                new ColorData(255, 170, 40, 255),   // warning
                new ColorData(235, 58, 36, 255)     // success
        );

        ThemePalette darkRedPalette = new ThemePalette(
                new ColorData(8, 8, 10, 250),
                new ColorData(14, 12, 14, 255),
                new ColorData(20, 16, 18, 255),
                new ColorData(60, 22, 24, 255),
                new ColorData(175, 20, 24, 255),
                new ColorData(135, 15, 18, 255),
                new ColorData(190, 24, 28, 255),
                new ColorData(24, 20, 22, 255),
                new ColorData(245, 245, 247, 255),
                new ColorData(145, 138, 142, 255),
                new ColorData(175, 20, 24, 210),
                new ColorData(245, 245, 247, 255),
                new ColorData(190, 24, 28, 255),
                new ColorData(240, 140, 30, 255),
                new ColorData(190, 24, 28, 255)
        );

        ThemePalette bwRedPalette = new ThemePalette(
                new ColorData(5, 5, 6, 252),
                new ColorData(12, 12, 14, 255),
                new ColorData(18, 18, 20, 255),
                new ColorData(68, 68, 74, 255),
                new ColorData(255, 42, 38, 255),
                new ColorData(200, 30, 28, 255),
                new ColorData(255, 42, 38, 255),
                new ColorData(24, 24, 28, 255),
                new ColorData(255, 255, 255, 255),
                new ColorData(180, 180, 188, 255),
                new ColorData(255, 255, 255, 200),
                new ColorData(255, 255, 255, 255),
                new ColorData(255, 42, 38, 255),
                new ColorData(255, 185, 50, 255),
                new ColorData(255, 255, 255, 255)
        );

        Theme defaultRed = new Theme("default_red", "Default Red", true, defaultRedPalette);
        Theme darkRed = new Theme("dark_red", "Dark Red", true, darkRedPalette);
        Theme bwRed = new Theme("bw_red", "Black & White + Red", true, bwRedPalette);
        this.customTheme = new Theme("custom", "Custom Theme", false, defaultRedPalette.copy());

        themes.put(defaultRed.getId(), defaultRed);
        themes.put(darkRed.getId(), darkRed);
        themes.put(bwRed.getId(), bwRed);
        themes.put(customTheme.getId(), customTheme);

        this.activeTheme = defaultRed;
    }

    public Theme getActiveTheme() {
        return activeTheme;
    }

    public ThemePalette getPalette() {
        return activeTheme.getPalette();
    }

    public Theme getCustomTheme() {
        return customTheme;
    }

    public void setActiveTheme(String themeId) {
        if (themeId != null && themes.containsKey(themeId)) {
            this.activeTheme = themes.get(themeId);
        }
    }

    public List<Theme> getAllThemes() {
        return Collections.unmodifiableList(new ArrayList<>(themes.values()));
    }

    public JsonObject saveToJson() {
        JsonObject root = new JsonObject();
        root.addProperty("activeTheme", activeTheme.getId());
        root.add("customPalette", customTheme.getPalette().toJson());
        return root;
    }

    public void loadFromJson(JsonObject root) {
        if (root == null) return;
        if (root.has("customPalette") && root.get("customPalette").isJsonObject()) {
            customTheme.getPalette().fromJson(root.getAsJsonObject("customPalette"));
        }
        if (root.has("activeTheme") && root.get("activeTheme").isJsonPrimitive()) {
            setActiveTheme(root.get("activeTheme").getAsString());
        }
    }
}
