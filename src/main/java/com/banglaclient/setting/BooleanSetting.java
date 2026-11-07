package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Boolean toggle setting (ON / OFF).
 */
public final class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String id, String name, String description, boolean defaultValue) {
        super(id, name, description, defaultValue);
    }

    public void toggle() {
        setValue(!getValue());
    }

    public boolean isEnabled() {
        return Boolean.TRUE.equals(getValue());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            setValue(element.getAsBoolean());
        }
    }
}
