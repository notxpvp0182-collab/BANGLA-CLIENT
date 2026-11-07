package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.WorldProfileMapper;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.ModeSetting;
import com.banglaclient.setting.StringSetting;

/**
 * Module 262: World-Specific Profile
 * Applies a configuration profile automatically when loading a specific local singleplayer world.
 */
public final class WorldSpecificProfile extends AbstractModule {

    private final StringSetting worldName = registerSetting(new StringSetting(
            "world_folder_rule", "World Name", "Local singleplayer world name", "New World", 128
    ));
    private final ModeSetting assignedProfile = registerSetting(new ModeSetting(
            "world_assigned_profile", "Assigned Profile", "Profile applied when opening this world",
            "survival", "default", "pvp", "survival", "low_end", "balanced", "quality", "extreme_fps"
    ));

    public WorldSpecificProfile() {
        super(
                "world_specific_profile",
                "World-Specific Profile",
                "Applies configuration to selected local singleplayer worlds.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );

        registerSetting(new ActionSetting(
                "save_world_rule",
                "Bind World Rule",
                "Save the world-to-profile mapping",
                "Save World Rule",
                () -> WorldProfileMapper.getInstance().bindWorldProfile(worldName.getValue(), assignedProfile.getValue())
        ));
    }

    @Override
    public void onEnable() {
        WorldProfileMapper.getInstance().setEnabled(true);
    }

    @Override
    public void onDisable() {
        WorldProfileMapper.getInstance().setEnabled(false);
    }
}
