package com.banglaclient.module.impl.client;

import com.banglaclient.config.ConfigImportExport;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 275: Config Import/Export
 * Transfers BANGLACLIENT configurations between installations via system clipboard share codes.
 */
public final class ConfigImportExportModule extends AbstractModule {

    public ConfigImportExportModule() {
        super(
                "config_import_export",
                "Config Import/Export",
                "Exports and imports configurations via clipboard share codes.",
                ModuleCategory.CLIENT_SETTINGS,
                true
        );

        registerSetting(new ActionSetting(
                "export_to_clipboard",
                "Export To Clipboard",
                "Copy your complete configuration as a BC1 share string",
                "Copy Share Code",
                () -> {
                    if (mc.keyboard != null) {
                        mc.keyboard.setClipboard(ConfigImportExport.exportToShareCode());
                    }
                }
        ));

        registerSetting(new ActionSetting(
                "import_from_clipboard",
                "Import From Clipboard",
                "Load a BC1 share string currently in your clipboard",
                "Import Share Code",
                () -> {
                    if (mc.keyboard != null) {
                        ConfigImportExport.importFromShareCode(mc.keyboard.getClipboard());
                    }
                }
        ));
    }
}
