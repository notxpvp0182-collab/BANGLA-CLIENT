package com.banglaclient.module.impl.network;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 238: Reconnect Delay
 * Controls the countdown delay in seconds before Auto Reconnect initiates a new connection.
 */
public final class ReconnectDelay extends AbstractModule {

    private final IntegerSetting delaySeconds = registerSetting(new IntegerSetting(
            "reconnect_delay_sec", "Reconnect Delay", "Seconds to wait before reconnecting to the server", 5, 2, 60, 1, "s"
    ));

    public ReconnectDelay() {
        super(
                "reconnect_delay",
                "Reconnect Delay",
                "Controls the countdown delay for Auto Reconnect.",
                ModuleCategory.NETWORK_SERVER,
                true
        );
    }

    public int getDelaySeconds() {
        return delaySeconds.getValue();
    }
}
