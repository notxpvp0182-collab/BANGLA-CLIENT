package com.banglaclient.setting;

import com.banglaclient.keybind.InputDeviceType;
import com.banglaclient.keybind.Keybind;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Keybind setting exposed ONLY by modules that logically require a keybind.
 */
public final class KeybindSetting extends Setting<Keybind> {

    public KeybindSetting(String id, String name, String description, Keybind defaultValue) {
        super(id, name, description, defaultValue != null ? defaultValue : Keybind.UNBOUND);
    }

    public void unbind() {
        setValue(Keybind.UNBOUND);
    }

    public boolean isBound() {
        return getValue() != null && getValue().isBound();
    }

    @Override
    public JsonElement toJson() {
        Keybind kb = getValue();
        JsonObject obj = new JsonObject();
        obj.addProperty("device", kb.getDeviceType().name());
        obj.addProperty("code", kb.getKeyCode());
        obj.addProperty("mode", kb.getTriggerMode().name());
        return obj;
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return;
        }
        JsonObject obj = element.getAsJsonObject();
        try {
            InputDeviceType device = obj.has("device")
                    ? InputDeviceType.valueOf(obj.get("device").getAsString())
                    : InputDeviceType.NONE;
            int code = obj.has("code") ? obj.get("code").getAsInt() : -1;
            Keybind.TriggerMode mode = obj.has("mode")
                    ? Keybind.TriggerMode.valueOf(obj.get("mode").getAsString())
                    : Keybind.TriggerMode.TOGGLE;
            setValue(new Keybind(device, code, mode));
        } catch (IllegalArgumentException ignored) {
            setValue(Keybind.UNBOUND);
        }
    }
}
