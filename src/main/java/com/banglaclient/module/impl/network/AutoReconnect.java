package com.banglaclient.module.impl.network;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.network.ServerInfo;

/**
 * Module 237: Auto Reconnect
 * Automatically reconnects or provides a countdown button on the Disconnected screen after connection loss.
 */
public final class AutoReconnect extends AbstractModule {

    private final BooleanSetting cancelOnManualKick = registerSetting(new BooleanSetting(
            "cancel_on_manual_kick", "Pause On Ban/Kick", "Do not auto-reconnect if disconnect message indicates a ban or manual kick", true
    ));

    private ServerInfo lastConnectedServer = null;

    public AutoReconnect() {
        super(
                "auto_reconnect",
                "Auto Reconnect",
                "Reconnects after connection loss according to configurable rules.",
                ModuleCategory.NETWORK_SERVER,
                false
        );
    }

    public void recordServer(ServerInfo serverInfo) {
        if (serverInfo != null) {
            this.lastConnectedServer = serverInfo;
        }
    }

    public ServerInfo getLastConnectedServer() {
        return lastConnectedServer;
    }

    public boolean isCancelOnManualKick() {
        return cancelOnManualKick.isEnabled();
    }
}
