package com.banglaclient.module.impl.quality;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.config.ConfigRecoveryHandler;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 303: Config Recovery
 * Restores previous working `.bak` configuration after file corruption or accidental misconfiguration.
 */
public final class ConfigRecovery extends AbstractModule {

    public ConfigRecovery() {
        super(
                "config_recovery",
                "Config Recovery",
                "Restores previous valid configuration after corruption.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );

        registerSetting(new ActionSetting(
                "verify_and_recover_now",
                "Verify & Recover Config",
                "Validate config.json syntax and recover from .bak if needed",
                "Verify Config",
                () -> ConfigRecoveryHandler.readWithRecovery(ConfigManager.getInstance().getMainConfigFile())
                        .ifPresent( json -> ConfigManager.getInstance().applyJsonState(json))
        ));
    }
}
