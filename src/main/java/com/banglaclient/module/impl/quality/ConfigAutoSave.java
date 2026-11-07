package com.banglaclient.module.impl.quality;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 302: Config Auto-Save
 * Automatically saves BANGLACLIENT configuration at configurable intervals and whenever GUI screens close.
 */
public final class ConfigAutoSave extends AbstractModule {

    private final IntegerSetting intervalSeconds = registerSetting(new IntegerSetting(
            "autosave_interval_sec", "Auto-Save Interval", "Interval in seconds between automatic background saves", 60, 15, 300, 15, "s"
    ));

    private long lastSaveMillis = System.currentTimeMillis();

    public ConfigAutoSave() {
        super(
                "config_auto_save",
                "Config Auto-Save",
                "Automatically saves client configuration periodically.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastSaveMillis >= (intervalSeconds.getValue() * 1000L)) {
            lastSaveMillis = now;
            ConfigManager.getInstance().saveConfig();
        }
    }
}
