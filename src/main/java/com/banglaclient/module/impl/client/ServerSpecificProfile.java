package com.banglaclient.module.impl.client;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ServerProfileMapper;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.ModeSetting;
import com.banglaclient.setting.StringSetting;

/**
 * Module 261: Server-Specific Profile
 * Automatically applies a local configuration profile based on the server address joined
 * (e.g., play.banglaSMP.net -> PvP).
 */
public final class ServerSpecificProfile extends AbstractModule {

    private final StringSetting serverAddress = registerSetting(new StringSetting(
            "server_addr_rule", "Server Address", "Server domain or IP to bind", ClientCore.OFFICIAL_SERVER, 128
    ));
    private final ModeSetting targetProfile = registerSetting(new ModeSetting(
            "server_target_profile", "Assigned Profile", "Profile applied automatically when joining the server",
            "pvp", "default", "pvp", "survival", "low_end", "balanced", "quality", "extreme_fps"
    ));

    public ServerSpecificProfile() {
        super(
                "server_specific_profile",
                "Server-Specific Profile",
                "Automatically applies a profile based on server address.",
                ModuleCategory.CLIENT_SETTINGS,
                true
        );

        registerSetting(new ActionSetting(
                "bind_server_rule",
                "Save Server Rule",
                "Bind the specified server address to the chosen profile",
                "Save Rule",
                () -> ServerProfileMapper.getInstance().bindServerProfile(serverAddress.getValue(), targetProfile.getValue())
        ));
    }

    @Override
    public void onEnable() {
        ServerProfileMapper.getInstance().setEnabled(true);
    }

    @Override
    public void onDisable() {
        ServerProfileMapper.getInstance().setEnabled(false);
    }
}
