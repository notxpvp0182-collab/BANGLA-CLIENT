package com.banglaclient.module.impl.network;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 241: Fast Server Switching (Network / Server Category)
 * Streamlines server switching by keeping Netty event loops warm and skipping redundant
 * resource-pack unload cycles when switching to servers without custom packs.
 */
public final class FastServerSwitchingNet extends AbstractModule {

    private final BooleanSetting skipEmptyPackReload = registerSetting(new BooleanSetting(
            "skip_empty_pack_reload", "Skip Unchanged Pack Reload", "Do not trigger a full resource reload on disconnect if no server pack was active", true
    ));
    private final BooleanSetting fastDnsCache = registerSetting(new BooleanSetting(
            "fast_dns_cache", "Cache SRV/DNS Records", "Reuse resolved server IP addresses during session reconnects", true
    ));

    public FastServerSwitchingNet() {
        super(
                "fast_server_switching_net",
                "Fast Server Switching",
                "Streamlines network handshakes and server switching.",
                ModuleCategory.NETWORK_SERVER,
                true
        );
    }

    public boolean isSkipEmptyPackReload() { return isEnabled() && skipEmptyPackReload.isEnabled(); }
    public boolean isFastDnsCache() { return isEnabled() && fastDnsCache.isEnabled(); }
}
