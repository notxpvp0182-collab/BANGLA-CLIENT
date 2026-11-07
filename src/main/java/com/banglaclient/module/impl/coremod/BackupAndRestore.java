package com.banglaclient.module.impl.coremod;

import com.banglaclient.config.ConfigBackupService;
import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 315: Backup & Restore
 * Backs up and restores the complete BANGLACLIENT configuration state.
 */
public final class BackupAndRestore extends AbstractModule {

    public BackupAndRestore() {
        super(
                "backup_and_restore",
                "Backup & Restore",
                "Backs up and restores all BANGLACLIENT configurations.",
                ModuleCategory.BANGLACLIENT_CORE,
                true
        );

        registerSetting(new ActionSetting(
                "core_backup_now",
                "Create Full Backup",
                "Create a timestamped snapshot of all modules, HUDs, and profiles",
                "Create Backup",
                () -> ConfigBackupService.getInstance().createTimestampedBackup(ConfigManager.getInstance().getMainConfigFile())
        ));

        registerSetting(new ActionSetting(
                "core_restore_now",
                "Restore Latest Backup",
                "Restore the most recent backup snapshot",
                "Restore Backup",
                () -> {
                    if (ConfigBackupService.getInstance().restoreLatestBackup(ConfigManager.getInstance().getMainConfigFile())) {
                        ConfigManager.getInstance().loadConfig();
                    }
                }
        ));
    }
}
