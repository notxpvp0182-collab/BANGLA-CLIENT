package com.banglaclient.core;

/**
 * Holds global runtime flags for BANGLACLIENT such as Safe Mode, Vanilla Mode,
 * and module description visibility.
 */
public final class ClientState {

    private boolean showModuleDescriptions = true;
    private boolean animationsEnabled = true;
    private float animationSpeed = 1.0f;
    private float guiScale = 1.0f;
    private boolean safeMode = false;
    private boolean compatibilityMode = false;
    private boolean vanillaMode = false;
    private boolean initialized = false;
    private String activeProfileName = "Default";
    private String currentServerAddress = "";

    public boolean isShowModuleDescriptions() {
        return showModuleDescriptions;
    }

    public void setShowModuleDescriptions(boolean showModuleDescriptions) {
        this.showModuleDescriptions = showModuleDescriptions;
    }

    public boolean isAnimationsEnabled() {
        return animationsEnabled;
    }

    public void setAnimationsEnabled(boolean animationsEnabled) {
        this.animationsEnabled = animationsEnabled;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = Math.max(0.1f, Math.min(5.0f, animationSpeed));
    }

    public float getGuiScale() {
        return guiScale;
    }

    public void setGuiScale(float guiScale) {
        this.guiScale = Math.max(0.5f, Math.min(2.0f, guiScale));
    }

    public boolean isSafeMode() {
        return safeMode;
    }

    public void setSafeMode(boolean safeMode) {
        this.safeMode = safeMode;
    }

    public boolean isCompatibilityMode() {
        return compatibilityMode;
    }

    public void setCompatibilityMode(boolean compatibilityMode) {
        this.compatibilityMode = compatibilityMode;
    }

    public boolean isVanillaMode() {
        return vanillaMode;
    }

    public void setVanillaMode(boolean vanillaMode) {
        this.vanillaMode = vanillaMode;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getActiveProfileName() {
        return activeProfileName;
    }

    public void setActiveProfileName(String activeProfileName) {
        this.activeProfileName = activeProfileName != null ? activeProfileName : "Default";
    }

    public String getCurrentServerAddress() {
        return currentServerAddress;
    }

    public void setCurrentServerAddress(String currentServerAddress) {
        this.currentServerAddress = currentServerAddress != null ? currentServerAddress : "";
    }
}
