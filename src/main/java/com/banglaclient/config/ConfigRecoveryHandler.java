package com.banglaclient.config;

import com.banglaclient.core.BanglaLogger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public final class ConfigRecoveryHandler {

    private ConfigRecoveryHandler() {
    }

    public static void writeAtomically(Path targetFile, String jsonContent) throws IOException {
        Path parentDir = targetFile.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        JsonParser.parseString(jsonContent).getAsJsonObject();

        Path tempFile = targetFile.resolveSibling(targetFile.getFileName().toString() + ".tmp");
        Path backupFile = targetFile.resolveSibling(targetFile.getFileName().toString() + ".bak");

        Files.writeString(tempFile, jsonContent, StandardCharsets.UTF_8);

        if (Files.exists(targetFile)) {
            try {
                String existing = Files.readString(targetFile, StandardCharsets.UTF_8);
                JsonParser.parseString(existing).getAsJsonObject();
                Files.copy(targetFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ignored) {
                BanglaLogger.warn("Existing config file was malformed; preserving previous .bak file.");
            }
        }

        try {
            Files.move(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException atomicUnsupported) {
            Files.move(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static Optional<JsonObject> readWithRecovery(Path targetFile) {
        Path backupFile = targetFile.resolveSibling(targetFile.getFileName().toString() + ".bak");

        if (Files.exists(targetFile)) {
            try {
                String raw = Files.readString(targetFile, StandardCharsets.UTF_8);
                JsonObject parsed = JsonParser.parseString(raw).getAsJsonObject();
                return Optional.of(parsed);
            } catch (Exception primaryError) {
                BanglaLogger.error("Primary configuration file {} is corrupted! Attempting automatic recovery...", targetFile.getFileName());
            }
        }

        if (Files.exists(backupFile)) {
            try {
                String backupRaw = Files.readString(backupFile, StandardCharsets.UTF_8);
                JsonObject recovered = JsonParser.parseString(backupRaw).getAsJsonObject();
                Files.copy(backupFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                BanglaLogger.info("Successfully recovered configuration from {}", backupFile.getFileName());
                return Optional.of(recovered);
            } catch (Exception backupError) {
                BanglaLogger.error("Backup configuration file {} was also unreadable.", backupFile.getFileName());
            }
        }

        return Optional.empty();
    }
}
