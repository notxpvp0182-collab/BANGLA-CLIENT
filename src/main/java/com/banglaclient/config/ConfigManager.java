package com.banglaclient.config;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.core.ClientCore;
import com.banglaclient.core.ClientState;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.theme.ThemeManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public final class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Path mainConfigFile;
    private long lastAutoSaveTimeMillis = System.currentTimeMillis();

    private ConfigManager() {
        this.mainConfigFile = ClientCore.getInstance().getConfigDirectory().resolve("config.json");
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public synchronized void saveConfig() {
        try {
            JsonObject root = serializeCurrentStateToJson();
            String jsonText = GSON.toJson(root);
            ConfigRecoveryHandler.writeAtomically(mainConfigFile, jsonText);
            lastAutoSaveTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            BanglaLogger.error("Failed to save BANGLACLIENT configuration", e);
        }
    }

    public synchronized void loadConfig() {
        Optional<JsonObject> loaded = ConfigRecoveryHandler.readWithRecovery(mainConfigFile);
        if (loaded.isEmpty()) {
            BanglaLogger.info("No existing configuration found; writing initial default configuration.");
            saveConfig();
            return;
        }
        applyJsonState(loaded.get());
        BanglaLogger.info("Loaded BANGLACLIENT configuration successfully.");
    }

    public JsonObject serializeCurrentStateToJson() {
        JsonObject root = new JsonObject();
        root.addProperty("version", ClientCore.MOD_VERSION);

        ClientState state = ClientCore.getInstance().getClientState();
        JsonObject clientObj = new JsonObject();
        clientObj.addProperty("showModuleDescriptions", state.isShowModuleDescriptions());
        clientObj.addProperty("animationsEnabled", state.isAnimationsEnabled());
        clientObj.addProperty("animationSpeed", state.getAnimationSpeed());
        clientObj.addProperty("guiScale", state.getGuiScale());
        clientObj.addProperty("safeMode", state.isSafeMode());
        clientObj.addProperty("compatibilityMode", state.isCompatibilityMode());
        clientObj.addProperty("vanillaMode", state.isVanillaMode());
        clientObj.addProperty("activeProfile", state.getActiveProfileName());
        root.add("clientState", clientObj);

        root.add("themes", ThemeManager.getInstance().saveToJson());

        JsonObject modulesObj = new JsonObject();
        for (Module module : ModuleRegistry.getInstance().getAllModules()) {
            modulesObj.add(module.getId(), module.saveToJson());
        }
        root.add("modules", modulesObj);

        return root;
    }

    public void applyJsonState(JsonObject root) {
        if (root == null) {
            return;
        }
        try {
            if (root.has("clientState") && root.get("clientState").isJsonObject()) {
                JsonObject c = root.getAsJsonObject("clientState");
                ClientState state = ClientCore.getInstance().getClientState();
                if (c.has("showModuleDescriptions")) state.setShowModuleDescriptions(c.get("showModuleDescriptions").getAsBoolean());
                if (c.has("animationsEnabled")) state.setAnimationsEnabled(c.get("animationsEnabled").getAsBoolean());
                if (c.has("animationSpeed")) state.setAnimationSpeed(c.get("animationSpeed").getAsFloat());
                if (c.has("guiScale")) state.setGuiScale(c.get("guiScale").getAsFloat());
                if (c.has("safeMode")) state.setSafeMode(c.get("safeMode").getAsBoolean());
                if (c.has("compatibilityMode")) state.setCompatibilityMode(c.get("compatibilityMode").getAsBoolean());
                if (c.has("vanillaMode")) state.setVanillaMode(c.get("vanillaMode").getAsBoolean());
                if (c.has("activeProfile")) state.setActiveProfileName(c.get("activeProfile").getAsString());
            }

            if (root.has("themes") && root.get("themes").isJsonObject()) {
                ThemeManager.getInstance().loadFromJson(root.getAsJsonObject("themes"));
            }

            if (root.has("modules") && root.get("modules").isJsonObject()) {
                JsonObject modulesObj = root.getAsJsonObject("modules");
                for (Module module : ModuleRegistry.getInstance().getAllModules()) {
                    if (modulesObj.has(module.getId()) && modulesObj.get(module.getId()).isJsonObject()) {
                        module.loadFromJson(modulesObj.getAsJsonObject(module.getId()));
                    }
                }
            }
        } catch (Exception e) {
            BanglaLogger.error("Error applying configuration state", e);
        }
    }

    public void tickAutoSave() {
        long now = System.currentTimeMillis();
        if (now - lastAutoSaveTimeMillis >= 60_000L) {
            saveConfig();
        }
    }

    public Path getMainConfigFile() {
        return mainConfigFile;
    }
}
