package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Configurable text setting for custom formats, AutoGG messages, chat filters,
 * and server profile filters.
 */
public final class StringSetting extends Setting<String> {

    private final int maxLength;

    public StringSetting(String id, String name, String description, String defaultValue, int maxLength) {
        super(id, name, description, defaultValue != null ? defaultValue : "");
        this.maxLength = Math.max(1, maxLength);
    }

    public StringSetting(String id, String name, String description, String defaultValue) {
        this(id, name, description, defaultValue, 256);
    }

    @Override
    public void setValue(String newValue) {
        if (newValue == null) {
            return;
        }
        String trimmed = newValue.length() > maxLength ? newValue.substring(0, maxLength) : newValue;
        super.setValue(trimmed);
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            setValue(element.getAsString());
        }
    }
}
