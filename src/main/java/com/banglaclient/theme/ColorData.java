package com.banglaclient.theme;

import java.awt.Color;

/**
 * Immutable ARGB color container supporting RGB, HSB, Alpha, and HEX conversions.
 * Designed for zero-allocation color math and theme/HUD customization.
 */
public final class ColorData {

    public static final ColorData BLACK = new ColorData(11, 11, 13, 255);
    public static final ColorData PANEL_BLACK = new ColorData(20, 20, 24, 255);
    public static final ColorData CARD_BLACK = new ColorData(26, 26, 32, 255);
    public static final ColorData WHITE = new ColorData(255, 255, 255, 255);
    public static final ColorData GRAY = new ColorData(158, 158, 168, 255);
    public static final ColorData RED_ACCENT = new ColorData(229, 37, 33, 255);
    public static final ColorData DARK_RED = new ColorData(184, 29, 26, 255);
    public static final ColorData BRIGHT_RED = new ColorData(255, 46, 42, 255);

    private final int r;
    private final int g;
    private final int b;
    private final int a;
    private final int argb;

    public ColorData(int r, int g, int b, int a) {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.a = clamp(a);
        this.argb = ((this.a & 0xFF) << 24)
                | ((this.r & 0xFF) << 16)
                | ((this.g & 0xFF) << 8)
                | (this.b & 0xFF);
    }

    public ColorData(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public ColorData(int argb) {
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8) & 0xFF;
        this.b = argb & 0xFF;
        this.argb = argb;
    }

    public static ColorData fromHex(String hex) {
        if (hex == null || hex.isBlank()) {
            return RED_ACCENT;
        }
        String cleaned = hex.trim();
        if (cleaned.startsWith("#")) {
            cleaned = cleaned.substring(1);
        }
        try {
            if (cleaned.length() == 6) {
                int rgb = Integer.parseInt(cleaned, 16);
                return new ColorData((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, 255);
            } else if (cleaned.length() == 8) {
                long rgba = Long.parseLong(cleaned, 16);
                return new ColorData(
                        (int) ((rgba >> 24) & 0xFF),
                        (int) ((rgba >> 16) & 0xFF),
                        (int) ((rgba >> 8) & 0xFF),
                        (int) (rgba & 0xFF)
                );
            }
        } catch (NumberFormatException ignored) {
        }
        return RED_ACCENT;
    }

    public static ColorData fromHSB(float hue, float saturation, float brightness, int alpha) {
        int rgb = Color.HSBtoRGB(hue, clamp01(saturation), clamp01(brightness));
        return new ColorData((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, clamp(alpha));
    }

    public ColorData withAlpha(int newAlpha) {
        return new ColorData(r, g, b, newAlpha);
    }

    public ColorData withAlphaFactor(float factor) {
        return new ColorData(r, g, b, Math.round(a * clamp01(factor)));
    }

    public ColorData interpolate(ColorData target, float progress) {
        float t = clamp01(progress);
        int nr = Math.round(this.r + (target.r - this.r) * t);
        int ng = Math.round(this.g + (target.g - this.g) * t);
        int nb = Math.round(this.b + (target.b - this.b) * t);
        int na = Math.round(this.a + (target.a - this.a) * t);
        return new ColorData(nr, ng, nb, na);
    }

    public String toHex(boolean includeAlpha) {
        if (includeAlpha) {
            return String.format("#%02X%02X%02X%02X", r, g, b, a);
        }
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public float[] toHSB() {
        return Color.RGBtoHSB(r, g, b, null);
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public int getAlpha() {
        return a;
    }

    public int getArgb() {
        return argb;
    }

    private static int clamp(int channel) {
        return Math.max(0, Math.min(255, channel));
    }

    private static float clamp01(float val) {
        return Math.max(0.0f, Math.min(1.0f, val));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ColorData other)) return false;
        return this.argb == other.argb;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(argb);
    }
}
