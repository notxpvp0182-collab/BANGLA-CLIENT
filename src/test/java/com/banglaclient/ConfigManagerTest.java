package com.banglaclient;

import com.banglaclient.config.ConfigRecoveryHandler;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies atomic config writing and automatic crash recovery from .bak when primary config is corrupted.
 */
public class ConfigManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void verifyAtomicWriteAndCorruptedFileRecovery() throws Exception {
        Path configFile = tempDir.resolve("config.json");

        // 1. Write valid initial JSON
        ConfigRecoveryHandler.writeAtomically(configFile, "{\"version\":\"1.0.0\",\"activeProfile\":\"Default\"}");
        assertTrue(Files.exists(configFile));

        // 2. Write second valid update (promotes first to config.json.bak)
        ConfigRecoveryHandler.writeAtomically(configFile, "{\"version\":\"1.0.0\",\"activeProfile\":\"PvP\"}");

        // 3. Simulate file corruption in config.json
        Files.writeString(configFile, "{corrupted_json_syntax_!@#", StandardCharsets.UTF_8);

        // 4. Verify ConfigRecoveryHandler automatically recovers from .bak without crashing
        Optional<JsonObject> recovered = ConfigRecoveryHandler.readWithRecovery(configFile);
        assertTrue(recovered.isPresent(), "Must recover valid configuration from .bak file");
        assertEquals("1.0.0", recovered.get().get("version").getAsString());
    }
}
