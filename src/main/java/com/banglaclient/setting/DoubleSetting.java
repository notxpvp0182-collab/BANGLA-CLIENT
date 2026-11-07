package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * High-precision double setting for continuous normalized coordinates and fine adjustments.
 */
public final class DoubleSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;
    private final String suffix;

    public DoubleSetting(String id, String name, String description, double defaultValue, double min, double max, double step, String suffix) {
        super(id, name, description, Math.max(min, Math.min(max, defaultValue)));
        this.min = min;
        this.max = max;
        this.step = step;
        this.suffix = suffix != null ? suffix : "";
    }

    public DoubleSetting(String id, String name, String description, double defaultValue, double min, double max) {
        this(id, name, description, defaultValue, min, max, 0.0, "");
    }

    @Override
    public void setValue(Double newValue) {
        if (newValue == null || Double.isNaN(newValue) || Double.isInfinite(newValue)) {
            return;
        }
        double clamped = Math.max(min, Math.min(max, newValue));
        if (step > 0.0) {
            clamped = min + Math.round((clamped - min) / step) * step;
            clamped = Math.max(min, Math.min(max, clamped));
        }
        super.setValue(clamped);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            setValue(element.getAsDouble());
        }
    }
}
