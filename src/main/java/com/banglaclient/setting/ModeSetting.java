package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Multi-option string mode selector setting (e.g., "Minimal", "Modern", "Compact").
 */
public final class ModeSetting extends Setting<String> {

    private final List<String> modes;

    public ModeSetting(String id, String name, String description, String defaultMode, String... modes) {
        super(id, name, description, defaultMode);
        if (modes == null || modes.length == 0) {
            this.modes = Collections.singletonList(defaultMode);
        } else {
            this.modes = Arrays.asList(modes);
        }
    }

    @Override
    protected boolean isValid(String candidate) {
        if (candidate == null) {
            return false;
        }
        for (String mode : modes) {
            if (mode.equalsIgnoreCase(candidate)) {
                return true;
            }
        }
        return false;
    }

    public void cycleNext() {
        int idx = indexOf(getValue());
        int next = (idx + 1) % modes.size();
        setValue(modes.get(next));
    }

    public void cyclePrevious() {
        int idx = indexOf(getValue());
        int prev = (idx - 1 + modes.size()) % modes.size();
        setValue(modes.get(prev));
    }

    public boolean isMode(String modeName) {
        return getValue() != null && getValue().equalsIgnoreCase(modeName);
    }

    private int indexOf(String modeName) {
        for (int i = 0; i < modes.size(); i++) {
            if (modes.get(i).equalsIgnoreCase(modeName)) {
                return i;
            }
        }
        return 0;
    }

    public List<String> getModes() {
        return Collections.unmodifiableList(modes);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String candidate = element.getAsString();
            for (String mode : modes) {
                if (mode.equalsIgnoreCase(candidate)) {
                    setValue(mode);
                    return;
                }
            }
        }
    }
}
