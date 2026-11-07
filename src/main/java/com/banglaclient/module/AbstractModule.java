package com.banglaclient.module;

import com.banglaclient.core.ClientCore;
import com.banglaclient.keybind.Keybind;
import com.banglaclient.setting.KeybindSetting;
import com.banglaclient.setting.Setting;
import com.banglaclient.setting.SettingGroup;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base implementation for all BANGLACLIENT modules.
 * Manages lifecycle hooks, EventBus registration, settings, optional keybinds,
 * and JSON config serialization.
 */
public abstract class AbstractModule implements Module {

    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private final String id;
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private final boolean defaultEnabled;
    private boolean enabled;

    private final List<Setting<?>> settings = new ArrayList<>();
    private final List<SettingGroup> settingGroups = new ArrayList<>();
    protected final SettingGroup generalGroup = new SettingGroup("general", "General");
    private KeybindSetting keybindSetting = null;

    protected AbstractModule(String id, String name, String description, ModuleCategory category, boolean defaultEnabled) {
        this.id = Objects.requireNonNull(id, "Module ID cannot be null");
        this.name = Objects.requireNonNull(name, "Module name cannot be null");
        this.description = description != null ? description : "";
        this.category = Objects.requireNonNull(category, "Module category cannot be null");
        this.defaultEnabled = defaultEnabled;
        this.enabled = false;
        this.settingGroups.add(generalGroup);
    }

    protected AbstractModule(String id, String name, String description, ModuleCategory category) {
        this(id, name, description, category, false);
    }

    /**
     * Enables keybind support for this module. Only called by modules that logically require a keybind.
     */
    protected KeybindSetting registerKeybind(Keybind defaultKeybind) {
        if (this.keybindSetting == null) {
            this.keybindSetting = new KeybindSetting(
                    "keybind",
                    "Keybind",
                    "Shortcut key to toggle or activate " + this.name,
                    defaultKeybind
            );
            registerSetting(generalGroup, this.keybindSetting);
        }
        return this.keybindSetting;
    }

    protected <S extends Setting<?>> S registerSetting(S setting) {
        return registerSetting(generalGroup, setting);
    }

    protected <S extends Setting<?>> S registerSetting(SettingGroup group, S setting) {
        if (setting != null && !settings.contains(setting)) {
            settings.add(setting);
            if (group != null) {
                if (!settingGroups.contains(group)) {
                    settingGroups.add(group);
                }
                group.add(setting);
            }
        }
        return setting;
    }

    protected SettingGroup createGroup(String groupId, String groupTitle) {
        SettingGroup group = new SettingGroup(groupId, groupTitle);
        settingGroups.add(group);
        return group;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ModuleCategory getCategory() {
        return category;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    @Override
    public boolean isEnabled() {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return false;
        }
        return enabled;
    }

    public boolean isRawEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean newEnabled) {
        if (this.enabled == newEnabled) {
            return;
        }
        this.enabled = newEnabled;
        if (this.enabled) {
            onEnable();
        } else {
            ClientCore.getInstance().getEventBus().unsubscribeAll(this);
            onDisable();
        }
    }

    @Override
    public void toggle() {
        setEnabled(!this.enabled);
    }

    @Override
    public boolean isHudModule() {
        return false;
    }

    @Override
    public boolean hasKeybind() {
        return keybindSetting != null;
    }

    @Override
    public Optional<KeybindSetting> getKeybindSetting() {
        return Optional.ofNullable(keybindSetting);
    }

    @Override
    public List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    @Override
    public List<SettingGroup> getSettingGroups() {
        return Collections.unmodifiableList(settingGroups);
    }

    @Override
    public void onEnable() {
        // Subclasses override to subscribe to events or apply states
    }

    @Override
    public void onDisable() {
        // Subclasses override to clean up states
    }

    @Override
    public void resetToDefaults() {
        setEnabled(defaultEnabled);
        for (Setting<?> setting : settings) {
            setting.resetToDefault();
        }
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject root = new JsonObject();
        root.addProperty("enabled", this.enabled);
        JsonObject settingsObj = new JsonObject();
        for (Setting<?> setting : settings) {
            settingsObj.add(setting.getId(), setting.toJson());
        }
        root.add("settings", settingsObj);
        return root;
    }

    @Override
    public void loadFromJson(JsonObject json) {
        if (json == null) {
            return;
        }
        if (json.has("enabled") && json.get("enabled").isJsonPrimitive()) {
            setEnabled(json.get("enabled").getAsBoolean());
        }
        if (json.has("settings") && json.get("settings").isJsonObject()) {
            JsonObject settingsObj = json.getAsJsonObject("settings");
            for (Setting<?> setting : settings) {
                JsonElement element = settingsObj.get(setting.getId());
                if (element != null && !element.isJsonNull()) {
                    setting.fromJson(element);
                }
            }
        }
    }
}
