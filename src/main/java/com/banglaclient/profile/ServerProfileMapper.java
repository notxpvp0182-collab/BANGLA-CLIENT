package com.banglaclient.profile;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.core.ClientCore;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Automatically switches local client profiles based on server address
 * (e.g., play.banglaSMP.net -> PvP or Survival profile).
 * Strictly local; never transmits client configuration data to any server.
 */
public final class ServerProfileMapper {

    private static final ServerProfileMapper INSTANCE = new ServerProfileMapper();

    private final Map<String, String> serverToProfileId = new LinkedHashMap<>();
    private boolean enabled = true;

    private ServerProfileMapper() {
        // Default mapping for Bangla SMP
        serverToProfileId.put("play.banglasmp.net", "pvp");
    }

    public static ServerProfileMapper getInstance() {
        return INSTANCE;
    }

    public void bindServerProfile(String serverAddress, String profileId) {
        if (serverAddress == null || serverAddress.isBlank() || profileId == null || profileId.isBlank()) {
            return;
        }
        serverToProfileId.put(normalizeAddress(serverAddress), profileId.toLowerCase(Locale.ROOT));
    }

    public void unbindServer(String serverAddress) {
        if (serverAddress != null) {
            serverToProfileId.remove(normalizeAddress(serverAddress));
        }
    }

    public void onServerJoined(String serverAddress) {
        ClientCore.getInstance().getClientState().setCurrentServerAddress(serverAddress);
        if (!enabled || serverAddress == null || serverAddress.isBlank()) {
            return;
        }
        String normalized = normalizeAddress(serverAddress);
        String targetProfileId = serverToProfileId.get(normalized);
        if (targetProfileId != null) {
            BanglaLogger.info("Auto-applying server-specific profile '{}' for {}", targetProfileId, serverAddress);
            ProfileManager.getInstance().applyProfile(targetProfileId);
        }
    }

    private String normalizeAddress(String addr) {
        String s = addr.trim().toLowerCase(Locale.ROOT);
        int colon = s.indexOf(':');
        return colon > 0 ? s.substring(0, colon) : s;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> getMappings() {
        return Collections.unmodifiableMap(serverToProfileId);
    }
}
