package com.banglaclient.module.impl.network;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 233: Network Optimizer
 * Optimizes client-side Netty channel configuration (TCP_NODELAY verification and buffer reuse)
 * without altering gameplay packets.
 */
public final class NetworkOptimizer extends AbstractModule {

    private final BooleanSetting verifyTcpNoDelay = registerSetting(new BooleanSetting(
            "verify_tcp_nodelay", "Verify TCP_NODELAY", "Ensure Nagle's algorithm is disabled on client sockets", true
    ));
    private final BooleanSetting fastServerListPing = registerSetting(new BooleanSetting(
            "fast_server_list_ping", "Parallel Server List", "Avoid blocking UI renders during multiplayer server list pings", true
    ));

    public NetworkOptimizer() {
        super(
                "network_optimizer",
                "Network Optimizer",
                "Optimizes client-side network socket and buffer processing.",
                ModuleCategory.NETWORK_SERVER,
                true
        );
    }

    public boolean isVerifyTcpNoDelay() { return isEnabled() && verifyTcpNoDelay.isEnabled(); }
    public boolean isFastServerListPing() { return isEnabled() && fastServerListPing.isEnabled(); }
}
