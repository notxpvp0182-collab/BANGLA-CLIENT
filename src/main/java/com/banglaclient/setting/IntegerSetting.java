package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Bounded integer slider/input setting with step increments and optional unit suffix.
 */
public final class IntegerSetting extends Setting<Integer> {

    private final int min;
    private final int max;
    private final int step;
    private final String suffix;

    public IntegerSetting(String id, String name, String description, int defaultValue, int min, int max, int step, String suffix) {
        super(id, name, description, Math.max(min, Math.min(max, defaultValue)));
        this.min = min;
        this.max = max;
        this.step = Math.max(1, step);
        this.suffix = suffix != null ? suffix : "";
    }

    public IntegerSetting(String id, String name, String description, int defaultValue, int min, int max) {
        this(id, name, description, defaultValue, min, max, 1, "");
    }

    @Override
    public void setValue(Integer newValue) {
        if (newValue == null) {
            return;
        }
        int clamped = Math.max(min, Math.min(max, newValue));
        if (step > 1) {
            clamped = min + Math.round((clamped - min) / (float) step) * step;
            clamped = Math.max(min, Math.min(max, clamped));
        }
        super.setValue(clamped);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getStep() {
        return step;
    }

    public String getSuffix() {
        return suffix;
    }

    public float getNormalizedProgress() {
        if (max <= min) {
            return 0.0f;
        }
        return (float) (getValue() - min) / (float) (max - min);
    }

    public void setFromNormalizedProgress(float progress) {
        float clampedProgress = Math.max(0.0f, Math.min(1.0f, progress));
        int raw = Math.round(min + clampedProgress * (max - min));
        setValue(raw);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            setValue(element.getAsInt());
        }
    }
}
