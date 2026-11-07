package com.banglaclient.core;

import com.banglaclient.event.EventBus;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

/**
 * Central singleton coordinator for BANGLACLIENT.
 * Provides clean access to core subsystems without static spaghetti coupling.
 */
public final class ClientCore {

    public static final String MOD_ID = "banglaclient";
    public static final String MOD_NAME = "BANGLACLIENT";
    public static final String MOD_VERSION = "1.0.0";
    public static final String DEVELOPER = "Ahmed";
    public static final String OFFICIAL_SERVER = "play.banglaSMP.net";

    private static final ClientCore INSTANCE = new ClientCore();

    private final ClientState clientState;
    private final EventBus eventBus;
    private final Path configDirectory;
    private final long sessionStartTimeMillis;

    private ClientCore() {
        this.clientState = new ClientState();
        this.eventBus = new EventBus();
        this.configDirectory = resolveConfigDirectory();
        this.sessionStartTimeMillis = System.currentTimeMillis();
    }

    public static ClientCore getInstance() {
        return INSTANCE;
    }

    /**
     * Resolves run/config/banglaclient. Falls back to a relative ./config/banglaclient path when
     * FabricLoader is not available (for example inside plain JUnit unit tests).
     */
    private static Path resolveConfigDirectory() {
        try {
            FabricLoader loader = FabricLoader.getInstance();
            if (loader != null) {
                Path dir = loader.getConfigDir();
                if (dir != null) {
                    return dir.resolve(MOD_ID);
                }
            }
        } catch (Throwable ignored) {
            // Fall through to the relative fallback below.
        }
        return Path.of("config", MOD_ID);
    }

    public void initialize() {
        if (clientState.isInitialized()) {
            return;
        }
        BanglaLogger.info("Initializing {} v{} by {} for Minecraft 1.21.11...", MOD_NAME, MOD_VERSION, DEVELOPER);

        try {
            if (!configDirectory.toFile().exists()) {
                boolean created = configDirectory.toFile().mkdirs();
                if (created) {
                    BanglaLogger.info("Created configuration directory at: {}", configDirectory.toAbsolutePath());
                }
            }
            clientState.setInitialized(true);
            BanglaLogger.info("{} core initialization complete.", MOD_NAME);
        } catch (Exception e) {
            BanglaLogger.error("Failed during core initialization; enabling Safe Mode fallback.", e);
            clientState.setSafeMode(true);
        }
    }

    public void shutdown() {
        BanglaLogger.info("Shutting down {} gracefully...", MOD_NAME);
    }

    public ClientState getClientState() {
        return clientState;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Path getConfigDirectory() {
        return configDirectory;
    }

    public long getSessionStartTimeMillis() {
        return sessionStartTimeMillis;
    }

    public long getSessionDurationSeconds() {
        return (System.currentTimeMillis() - sessionStartTimeMillis) / 1000L;
    }
}
