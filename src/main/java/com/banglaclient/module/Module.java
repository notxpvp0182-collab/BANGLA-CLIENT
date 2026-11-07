package com.banglaclient.module;

import com.banglaclient.setting.KeybindSetting;
import com.banglaclient.setting.Setting;
import com.banglaclient.setting.SettingGroup;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Optional;

/**
 * Core contract implemented by all 315 independent BANGLACLIENT modules.
 */
public interface Module {

    String getId();

    String getName();

    String getDescription();

    ModuleCategory getCategory();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void toggle();

    boolean isHudModule();

    boolean hasKeybind();

    Optional<KeybindSetting> getKeybindSetting();

    List<Setting<?>> getSettings();

    List<SettingGroup> getSettingGroups();

    void onEnable();

    void onDisable();

    void resetToDefaults();

    JsonObject saveToJson();

    void loadFromJson(JsonObject json);
}
