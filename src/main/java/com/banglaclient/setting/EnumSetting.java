package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Strongly-typed Enum selector setting supporting forward/backward cycling.
 *
 * @param <E> Enum type.
 */
public final class EnumSetting<E extends Enum<E>> extends Setting<E> {

    private final E[] constants;

    public EnumSetting(String id, String name, String description, E defaultValue) {
        super(id, name, description, defaultValue);
        this.constants = defaultValue.getDeclaringClass().getEnumConstants();
    }

    public void cycleNext() {
        int nextOrdinal = (getValue().ordinal() + 1) % constants.length;
        setValue(constants[nextOrdinal]);
    }

    public void cyclePrevious() {
        int prevOrdinal = (getValue().ordinal() - 1 + constants.length) % constants.length;
        setValue(constants[prevOrdinal]);
    }

    public E[] getConstants() {
        return constants;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue().name());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String raw = element.getAsString();
            for (E constant : constants) {
                if (constant.name().equalsIgnoreCase(raw)) {
                    setValue(constant);
                    return;
                }
            }
        }
    }
}
