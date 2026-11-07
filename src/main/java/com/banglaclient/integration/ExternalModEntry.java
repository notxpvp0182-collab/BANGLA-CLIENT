package com.banglaclient.integration;

import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

/**
 * Represents a dynamically detected external mod that exposes a configuration or menu screen
 * (e.g., Replay Mod, Account Switcher, Sodium, Iris, Voice Chat, ModMenu).
 */
public final class ExternalModEntry {

    private final String modId;
    private final String displayName;
    private final String version;
    private final Function<Screen, Screen> screenFactory;

    public ExternalModEntry(String modId, String displayName, String version, Function<Screen, Screen> screenFactory) {
        this.modId = modId;
        this.displayName = displayName;
        this.version = version;
        this.screenFactory = screenFactory;
    }

    public String getModId() {
        return modId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getVersion() {
        return version;
    }

    public Screen createScreen(Screen parentScreen) {
        if (screenFactory == null) {
            return null;
        }
        try {
            return screenFactory.apply(parentScreen);
        } catch (Throwable t) {
            return null;
        }
    }
}
