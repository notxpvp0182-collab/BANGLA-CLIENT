package com.banglaclient.config;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.core.ClientCore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class ConfigBackupService {

    private static final ConfigBackupService INSTANCE = new ConfigBackupService();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final int MAX_BACKUPS_RETAINED = 15;

    private ConfigBackupService() {
    }

    public static ConfigBackupService getInstance() {
        return INSTANCE;
    }

    public Path getBackupDirectory() {
        return ClientCore.getInstance().getConfigDirectory().resolve("backups");
    }

    public boolean createTimestampedBackup(Path sourceConfigFile) {
        if (sourceConfigFile == null || !Files.exists(sourceConfigFile)) {
            return false;
        }
        try {
            Path backupDir = getBackupDirectory();
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }
            String timestamp = LocalDateTime.now().format(FORMATTER);
            Path snapshot = backupDir.resolve("banglaclient_backup_" + timestamp + ".json");
            Files.copy(sourceConfigFile, snapshot, StandardCopyOption.REPLACE_EXISTING);
            pruneOldBackups(backupDir);
            BanglaLogger.info("Created configuration backup: {}", snapshot.getFileName());
            return true;
        } catch (IOException e) {
            BanglaLogger.error("Failed to create timestamped config backup", e);
            return false;
        }
    }

    public boolean restoreLatestBackup(Path targetConfigFile) {
        List<Path> backups = listBackupsNewestFirst();
        if (backups.isEmpty()) {
            BanglaLogger.warn("No configuration backups available to restore.");
            return false;
        }
        try {
            Path newest = backups.get(0);
            Files.copy(newest, targetConfigFile, StandardCopyOption.REPLACE_EXISTING);
            BanglaLogger.info("Restored configuration from backup: {}", newest.getFileName());
            return true;
        } catch (IOException e) {
            BanglaLogger.error("Failed to restore configuration backup", e);
            return false;
        }
    }

    public List<Path> listBackupsNewestFirst() {
        Path backupDir = getBackupDirectory();
        if (!Files.exists(backupDir)) {
            return List.of();
        }
        List<Path> result = new ArrayList<>();
        try (Stream<Path> stream = Files.list(backupDir)) {
            stream.filter(p -> p.getFileName().toString().endsWith(".json"))
                    .sorted(Comparator.comparingLong((Path p) -> p.toFile().lastModified()).reversed())
                    .forEach(result::add);
        } catch (IOException ignored) {
        }
        return result;
    }

    private void pruneOldBackups(Path backupDir) {
        List<Path> backups = listBackupsNewestFirst();
        for (int i = MAX_BACKUPS_RETAINED; i < backups.size(); i++) {
            try {
                Files.deleteIfExists(backups.get(i));
            } catch (IOException ignored) {
            }
        }
    }
}
