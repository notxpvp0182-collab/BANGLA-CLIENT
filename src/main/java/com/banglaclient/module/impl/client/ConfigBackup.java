package com.banglaclient.module.impl.client;

import com.banglaclient.config.ConfigBackupService;
import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 274: Config Backup
 * Creates timestamped configuration backups and restores snapshots on demand.
 */
public final class ConfigBackup extends AbstractModule {

    public ConfigBackup() {
        super(
                "config_backup",
                "Config Backup",
                "Backs up and restores BANGLACLIENT configuration snapshots.",
                ModuleCategory.CLIENT_SETTINGS,
                true
        );

        registerSetting(new ActionSetting(
                "create_backup_btn",
                "Create Snapshot",
                "Save a timestamped backup of your current config",
                "Backup Now",
                () -> ConfigBackupService.getInstance().createTimestampedBackup(ConfigManager.getInstance().getMainConfigFile())
        ));

        registerSetting(new ActionSetting(
                "restore_backup_btn",
                "Restore Latest Snapshot",
                "Restore your most recent timestamped config backup",
                "Restore Latest",
                () -> {
                    if (ConfigBackupService.getInstance().restoreLatestBackup(ConfigManager.getInstance().getMainConfigFile())) {
                        ConfigManager.getInstance().loadConfig();
                    }
                }
        ));
    }
}
