package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Bounded floating-point slider setting for continuous values such as scale,
 * opacity, FOV, speed, and viewmodel offsets.
 */
public final class FloatSetting extends Setting<Float> {

    private final float min;
    private final float max;
    private final float step;
    private final String suffix;

    public FloatSetting(String id, String name, String description, float defaultValue, float min, float max, float step, String suffix) {
        super(id, name, description, Math.max(min, Math.min(max, defaultValue)));
        this.min = min;
        this.max = max;
        this.step = step > 0.0f ? step : 0.01f;
        this.suffix = suffix != null ? suffix : "";
    }

    public FloatSetting(String id, String name, String description, float defaultValue, float min, float max) {
        this(id, name, description, defaultValue, min, max, 0.01f, "");
    }

    @Override
    public void setValue(Float newValue) {
        if (newValue == null || Float.isNaN(newValue) || Float.isInfinite(newValue)) {
            return;
        }
        float clamped = Math.max(min, Math.min(max, newValue));
        if (step > 0.0f) {
            clamped = min + Math.round((clamped - min) / step) * step;
            clamped = Math.max(min, Math.min(max, clamped));
        }
        super.setValue(clamped);
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getStep() {
        return step;
    }

    public String getSuffix() {
        return suffix;
    }

    public float getNormalizedProgress() {
        if (max <= min) {
            return 0.0f;
        }
        return (getValue() - min) / (max - min);
    }

    public void setFromNormalizedProgress(float progress) {
        float clampedProgress = Math.max(0.0f, Math.min(1.0f, progress));
        setValue(min + clampedProgress * (max - min));
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            setValue(element.getAsFloat());
        }
    }
}
