package com.banglaclient.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized logging facility for BANGLACLIENT.
 * Prevents frame-spamming by supporting rate-limited warnings and structured prefixes.
 */
public final class BanglaLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger("BANGLACLIENT");
    private static long lastRateLimitedLogTime = 0L;
    private static final long RATE_LIMIT_INTERVAL_MS = 5000L;

    private BanglaLogger() {
    }

    public static void info(String message, Object... args) {
        LOGGER.info("[BANGLACLIENT] " + message, args);
    }

    public static void warn(String message, Object... args) {
        LOGGER.warn("[BANGLACLIENT] " + message, args);
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.error("[BANGLACLIENT] " + message, throwable);
    }

    public static void error(String message, Object... args) {
        LOGGER.error("[BANGLACLIENT] " + message, args);
    }

    public static void debug(String message, Object... args) {
        LOGGER.debug("[BANGLACLIENT-DEBUG] " + message, args);
    }

    /**
     * Logs a warning at most once every 5 seconds to prevent render-loop log flooding.
     */
    public static void warnRateLimited(String message, Object... args) {
        long now = System.currentTimeMillis();
        if (now - lastRateLimitedLogTime >= RATE_LIMIT_INTERVAL_MS) {
            lastRateLimitedLogTime = now;
            LOGGER.warn("[BANGLACLIENT-RATELIMITED] " + message, args);
        }
    }
}
