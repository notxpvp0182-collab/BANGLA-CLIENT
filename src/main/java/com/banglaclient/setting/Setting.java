package com.banglaclient.setting;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

/**
 * Abstract strongly-typed base class for all module and client settings.
 * Supports visibility conditions, change listeners, favorite marking, default reset,
 * and JSON serialization for profiles and config files.
 *
 * @param <T> The underlying value type of this setting.
 */
public abstract class Setting<T> {

    private final String id;
    private final String name;
    private final String description;
    private final T defaultValue;
    protected T value;
    private BooleanSupplier visibilityCondition = () -> true;
    private final List<BiConsumer<T, T>> changeListeners = new ArrayList<>();
    private boolean favorite = false;

    protected Setting(String id, String name, String description, T defaultValue) {
        this.id = Objects.requireNonNull(id, "Setting ID cannot be null");
        this.name = Objects.requireNonNull(name, "Setting name cannot be null");
        this.description = description != null ? description : "";
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        if (newValue == null || !isValid(newValue)) {
            return;
        }
        T oldValue = this.value;
        if (!Objects.equals(oldValue, newValue)) {
            this.value = newValue;
            notifyListeners(oldValue, newValue);
        }
    }

    public void resetToDefault() {
        setValue(defaultValue);
    }

    public boolean isDefault() {
        return Objects.equals(value, defaultValue);
    }

    public boolean isVisible() {
        return visibilityCondition == null || visibilityCondition.getAsBoolean();
    }

    public Setting<T> visibleWhen(BooleanSupplier condition) {
        this.visibilityCondition = condition != null ? condition : () -> true;
        return this;
    }

    public Setting<T> onChange(BiConsumer<T, T> listener) {
        if (listener != null) {
            this.changeListeners.add(listener);
        }
        return this;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    protected void notifyListeners(T oldValue, T newValue) {
        for (int i = 0, size = changeListeners.size(); i < size; i++) {
            changeListeners.get(i).accept(oldValue, newValue);
        }
    }

    protected boolean isValid(T candidate) {
        return candidate != null;
    }

    public abstract JsonElement toJson();

    public abstract void fromJson(JsonElement element);
}
