package com.banglaclient.profile;

import com.google.gson.JsonObject;

/**
 * Represents a complete BANGLACLIENT configuration profile (e.g., Default, PvP,
 * Survival, Low-End, Balanced, Quality, Extreme FPS, or custom user profiles).
 */
public final class Profile {

    private final String id;
    private String name;
    private final boolean builtIn;
    private JsonObject stateData;

    public Profile(String id, String name, boolean builtIn, JsonObject stateData) {
        this.id = id;
        this.name = name;
        this.builtIn = builtIn;
        this.stateData = stateData != null ? stateData : new JsonObject();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!builtIn && name != null && !name.isBlank()) {
            this.name = name.trim();
        }
    }

    public boolean isBuiltIn() {
        return builtIn;
    }

    public JsonObject getStateData() {
        return stateData;
    }

    public void setStateData(JsonObject stateData) {
        if (stateData != null) {
            this.stateData = stateData;
        }
    }
}
