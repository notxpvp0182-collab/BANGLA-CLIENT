package com.banglaclient.profile;

import com.banglaclient.core.BanglaLogger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Optional local singleplayer world-to-profile mapper.
 */
public final class WorldProfileMapper {

    private static final WorldProfileMapper INSTANCE = new WorldProfileMapper();

    private final Map<String, String> worldToProfileId = new LinkedHashMap<>();
    private boolean enabled = false;

    private WorldProfileMapper() {
    }

    public static WorldProfileMapper getInstance() {
        return INSTANCE;
    }

    public void bindWorldProfile(String worldFolderName, String profileId) {
        if (worldFolderName != null && !worldFolderName.isBlank() && profileId != null) {
            worldToProfileId.put(worldFolderName.trim().toLowerCase(Locale.ROOT), profileId.toLowerCase(Locale.ROOT));
        }
    }

    public void onSingleplayerWorldLoaded(String worldFolderName) {
        if (!enabled || worldFolderName == null || worldFolderName.isBlank()) {
            return;
        }
        String profileId = worldToProfileId.get(worldFolderName.trim().toLowerCase(Locale.ROOT));
        if (profileId != null) {
            BanglaLogger.info("Auto-applying world-specific profile '{}' for local world '{}'", profileId, worldFolderName);
            ProfileManager.getInstance().applyProfile(profileId);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> getMappings() {
        return Collections.unmodifiableMap(worldToProfileId);
    }
}
