package com.banglaclient.hud;

import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.setting.SettingGroup;
import com.banglaclient.theme.ColorData;

/**
 * Encapsulates customizable visual styling for a HUD element:
 * Scale, Opacity, Background, Outline, Corner Radius, Padding, Text Shadow, and Colors.
 */
public final class HudStyle {

    private final FloatSetting scale;
    private final FloatSetting opacity;
    private final BooleanSetting backgroundEnabled;
    private final ColorSetting backgroundColor;
    private final BooleanSetting outlineEnabled;
    private final ColorSetting outlineColor;
    private final FloatSetting outlineThickness;
    private final ColorSetting textColor;
    private final IntegerSetting padding;
    private final FloatSetting cornerRadius;
    private final BooleanSetting textShadow;

    public HudStyle(SettingGroup styleGroup) {
        this.scale = styleGroup.add(new FloatSetting(
                "hud_scale", "Scale", "Size multiplier of this HUD element", 1.0f, 0.4f, 3.0f, 0.05f, "x"
        ));
        this.opacity = styleGroup.add(new FloatSetting(
                "hud_opacity", "Opacity", "Overall visibility opacity of this HUD element", 1.0f, 0.1f, 1.0f, 0.05f, ""
        ));
        this.backgroundEnabled = styleGroup.add(new BooleanSetting(
                "hud_bg_enabled", "Background", "Render a dark panel behind this HUD element", true
        ));
        this.backgroundColor = styleGroup.add(new ColorSetting(
                "hud_bg_color", "Background Color", "Panel background color and alpha", new ColorData(14, 14, 18, 185), true
        ));
        this.backgroundColor.visibleWhen(backgroundEnabled::isEnabled);

        this.outlineEnabled = styleGroup.add(new BooleanSetting(
                "hud_outline_enabled", "Outline", "Render a border outline around this HUD element", true
        ));
        this.outlineColor = styleGroup.add(new ColorSetting(
                "hud_outline_color", "Outline Color", "Border outline color", new ColorData(229, 37, 33, 190), true
        ));
        this.outlineColor.visibleWhen(outlineEnabled::isEnabled);

        this.outlineThickness = styleGroup.add(new FloatSetting(
                "hud_outline_thickness", "Outline Thickness", "Border width in pixels", 1.0f, 0.5f, 4.0f, 0.5f, "px"
        ));
        this.outlineThickness.visibleWhen(outlineEnabled::isEnabled);

        this.textColor = styleGroup.add(new ColorSetting(
                "hud_text_color", "Text Color", "Primary text color", ColorData.WHITE, true
        ));
        this.padding = styleGroup.add(new IntegerSetting(
                "hud_padding", "Padding", "Inner padding in pixels", 5, 1, 16, 1, "px"
        ));
        this.cornerRadius = styleGroup.add(new FloatSetting(
                "hud_corner_radius", "Corner Radius", "Rounded corner radius", 4.0f, 0.0f, 12.0f, 0.5f, "px"
        ));
        this.textShadow = styleGroup.add(new BooleanSetting(
                "hud_text_shadow", "Text Shadow", "Render drop shadow on HUD text", true
        ));
    }

    public float getScale() {
        return scale.getValue();
    }

    public void setScale(float newScale) {
        scale.setValue(newScale);
    }

    public float getOpacity() {
        return opacity.getValue();
    }

    public void setOpacity(float newOpacity) {
        opacity.setValue(newOpacity);
    }

    public boolean isBackgroundEnabled() {
        return backgroundEnabled.isEnabled();
    }

    public ColorData getBackgroundColor() {
        return backgroundColor.getValue().withAlphaFactor(opacity.getValue());
    }

    public boolean isOutlineEnabled() {
        return outlineEnabled.isEnabled();
    }

    public ColorData getOutlineColor() {
        return outlineColor.getValue().withAlphaFactor(opacity.getValue());
    }

    public float getOutlineThickness() {
        return outlineThickness.getValue();
    }

    public ColorData getTextColor() {
        return textColor.getValue().withAlphaFactor(opacity.getValue());
    }

    public int getPadding() {
        return padding.getValue();
    }

    public float getCornerRadius() {
        return cornerRadius.getValue();
    }

    public boolean isTextShadow() {
        return textShadow.isEnabled();
    }
}
