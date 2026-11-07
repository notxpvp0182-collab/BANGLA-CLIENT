package com.banglaclient;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.core.BanglaLogger;
import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.clickgui.BanglaClickGuiScreen;
import com.banglaclient.gui.hudeditor.HudEditorScreen;
import com.banglaclient.integration.ExternalModDetector;
import com.banglaclient.keybind.KeybindManager;
import com.banglaclient.module.AllModulesBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Main Fabric client entrypoint for BANGLACLIENT (Minecraft Java Edition 1.21.11).
 * Bootstraps ClientCore, registers all 315 modules, loads crash-safe configuration,
 * binds GUI/HUD Editor screen callbacks, and scans installed external mods.
 */
public class BanglaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BanglaLogger.info("Starting BANGLACLIENT bootstrap sequence...");

        ClientCore core = ClientCore.getInstance();
        core.initialize();

        // Register all 315 independent module classes
        AllModulesBootstrap.registerAllModules();

        // Load saved configuration (with automatic .bak recovery if corrupted)
        ConfigManager.getInstance().loadConfig();

        // Bind Click GUI (Open & Close with same key) and HUD Editor screen launchers
        KeybindManager.getInstance().setScreenCallbacks(
                () -> {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    if (mc == null) {
                        return;
                    }
                    if (mc.currentScreen instanceof BanglaClickGuiScreen) {
                        mc.currentScreen.close();
                    } else {
                        mc.setScreen(new BanglaClickGuiScreen());
                    }
                },
                () -> {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    if (mc != null) {
                        mc.setScreen(new HudEditorScreen(mc.currentScreen));
                    }
                }
        );

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ExternalModDetector.getInstance().scanInstalledMods();
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ConfigManager.getInstance().saveConfig();
            core.shutdown();
        });
    }
}
