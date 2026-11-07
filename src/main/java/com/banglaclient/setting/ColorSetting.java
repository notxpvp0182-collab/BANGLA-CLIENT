package com.banglaclient.setting;

import com.banglaclient.theme.ColorData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Configurable color setting supporting RGB, HEX, and Alpha transparency.
 */
public final class ColorSetting extends Setting<ColorData> {

    private final boolean allowAlpha;

    public ColorSetting(String id, String name, String description, ColorData defaultValue, boolean allowAlpha) {
        super(id, name, description, defaultValue != null ? defaultValue : ColorData.RED_ACCENT);
        this.allowAlpha = allowAlpha;
    }

    public ColorSetting(String id, String name, String description, ColorData defaultValue) {
        this(id, name, description, defaultValue, true);
    }

    public boolean isAllowAlpha() {
        return allowAlpha;
    }

    public int getArgb() {
        return getValue().getArgb();
    }

    @Override
    public JsonElement toJson() {
        ColorData color = getValue();
        JsonObject obj = new JsonObject();
        obj.addProperty("r", color.getRed());
        obj.addProperty("g", color.getGreen());
        obj.addProperty("b", color.getBlue());
        obj.addProperty("a", allowAlpha ? color.getAlpha() : 255);
        obj.addProperty("hex", color.toHex(allowAlpha));
        return obj;
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element == null) {
            return;
        }
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("r") && obj.has("g") && obj.has("b")) {
                int r = obj.get("r").getAsInt();
                int g = obj.get("g").getAsInt();
                int b = obj.get("b").getAsInt();
                int a = (allowAlpha && obj.has("a")) ? obj.get("a").getAsInt() : 255;
                setValue(new ColorData(r, g, b, a));
            } else if (obj.has("hex")) {
                setValue(ColorData.fromHex(obj.get("hex").getAsString()));
            }
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            setValue(ColorData.fromHex(element.getAsString()));
        }
    }
}
