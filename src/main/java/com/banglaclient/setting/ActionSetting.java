package com.banglaclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

/**
 * Triggerable button action setting inside a module's settings panel
 * (e.g., "Run Memory Cleanup Now", "Reset HUD Position", "Clear Chat").
 */
public final class ActionSetting extends Setting<Runnable> {

    private final String buttonLabel;

    public ActionSetting(String id, String name, String description, String buttonLabel, Runnable action) {
        super(id, name, description, action != null ? action : () -> {});
        this.buttonLabel = buttonLabel != null ? buttonLabel : name;
    }

    public void execute() {
        Runnable action = getValue();
        if (action != null) {
            action.run();
        }
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    @Override
    public JsonElement toJson() {
        return JsonNull.INSTANCE;
    }

    @Override
    public void fromJson(JsonElement element) {
        // Action buttons are stateless commands and do not require deserialization
    }
}
