package com.banglaclient.theme;

/**
 * Represents a named BANGLACLIENT theme preset or user-customizable theme.
 */
public final class Theme {

    private final String id;
    private final String name;
    private final boolean builtIn;
    private final ThemePalette palette;

    public Theme(String id, String name, boolean builtIn, ThemePalette palette) {
        this.id = id;
        this.name = name;
        this.builtIn = builtIn;
        this.palette = palette;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isBuiltIn() {
        return builtIn;
    }

    public ThemePalette getPalette() {
        return palette;
    }
}
