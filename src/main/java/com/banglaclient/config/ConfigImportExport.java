package com.banglaclient.config;

import com.banglaclient.core.BanglaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public final class ConfigImportExport {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private ConfigImportExport() {
    }

    public static boolean exportToFile(Path exportPath) {
        try {
            JsonObject state = ConfigManager.getInstance().serializeCurrentStateToJson();
            String json = GSON.toJson(state);
            Files.writeString(exportPath, json, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            BanglaLogger.error("Failed to export configuration to file", e);
            return false;
        }
    }

    public static boolean importFromFile(Path importPath) {
        if (importPath == null || !Files.exists(importPath)) {
            return false;
        }
        try {
            String raw = Files.readString(importPath, StandardCharsets.UTF_8);
            JsonObject parsed = JsonParser.parseString(raw).getAsJsonObject();
            ConfigManager.getInstance().applyJsonState(parsed);
            ConfigManager.getInstance().saveConfig();
            return true;
        } catch (Exception e) {
            BanglaLogger.error("Failed to import configuration file", e);
            return false;
        }
    }

    public static String exportToShareCode() {
        JsonObject state = ConfigManager.getInstance().serializeCurrentStateToJson();
        String compactJson = state.toString();
        return "BC1:" + Base64.getUrlEncoder().withoutPadding().encodeToString(compactJson.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean importFromShareCode(String shareCode) {
        if (shareCode == null || !shareCode.startsWith("BC1:")) {
            return false;
        }
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(shareCode.substring(4).trim());
            String json = new String(decoded, StandardCharsets.UTF_8);
            JsonObject parsed = JsonParser.parseString(json).getAsJsonObject();
            ConfigManager.getInstance().applyJsonState(parsed);
            ConfigManager.getInstance().saveConfig();
            return true;
        } catch (Exception e) {
            BanglaLogger.error("Invalid BANGLACLIENT share code", e);
            return false;
        }
    }
}
