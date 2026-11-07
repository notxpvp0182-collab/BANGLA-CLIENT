package com.banglaclient.integration;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.core.ClientCore;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.gui.screen.Screen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Dynamically detects compatible installed mods and extracts their menu/config screens
 * safely via Fabric's entrypoint container API without hard dependencies.
 *
 * When a mod is installed, its button appears automatically in the left-side navigation
 * area of the BANGLACLIENT Main Menu. When removed, it disappears automatically.
 */
public final class ExternalModDetector {

    private static final ExternalModDetector INSTANCE = new ExternalModDetector();

    private static final Set<String> IGNORED_INTERNAL_MODS = Set.of(
            "minecraft",
            "java",
            "fabricloader",
            "fabric-api",
            "mixinextras",
            ClientCore.MOD_ID
    );

    private final List<ExternalModEntry> detectedEntries = new ArrayList<>();
    private boolean scanned = false;

    private ExternalModDetector() {
    }

    public static ExternalModDetector getInstance() {
        return INSTANCE;
    }

    public synchronized void scanInstalledMods() {
        if (scanned) {
            return;
        }
        scanned = true;
        detectedEntries.clear();

        Map<String, Function<Screen, Screen>> factoriesByModId = new LinkedHashMap<>();

        // Discover ModMenu API entrypoints safely if any installed mod provides them
        try {
            List<EntrypointContainer<Object>> containers = FabricLoader.getInstance()
                    .getEntrypointContainers("modmenu", Object.class);

            for (EntrypointContainer<Object> container : containers) {
                String modId = container.getProvider().getMetadata().getId();
                if (IGNORED_INTERNAL_MODS.contains(modId) || modId.startsWith("fabric-")) {
                    continue;
                }
                try {
                    Object apiInstance = container.getEntrypoint();
                    Method getFactoryMethod = apiInstance.getClass().getMethod("getModConfigScreenFactory");
                    Object factoryObj = getFactoryMethod.invoke(apiInstance);
                    if (factoryObj != null) {
                        Method createMethod = factoryObj.getClass().getMethod("create", Screen.class);
                        factoriesByModId.put(modId, parent -> {
                            try {
                                Object res = createMethod.invoke(factoryObj, parent);
                                return (res instanceof Screen s) ? s : null;
                            } catch (Throwable ignored) {
                                return null;
                            }
                        });
                    }
                } catch (Throwable ignored) {
                    // Mod uses an incompatible or non-standard entrypoint signature; skip safely
                }
            }
        } catch (Throwable e) {
            BanglaLogger.debug("ModMenu entrypoint inspection skipped: {}", e.getMessage());
        }

        // Build ordered ExternalModEntry list for installed user-facing mods
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            String modId = mod.getMetadata().getId();
            if (IGNORED_INTERNAL_MODS.contains(modId) || modId.startsWith("fabric-")) {
                continue;
            }
            Function<Screen, Screen> factory = factoriesByModId.get(modId);
            if (factory != null) {
                String name = mod.getMetadata().getName();
                String ver = mod.getMetadata().getVersion().getFriendlyString();
                detectedEntries.add(new ExternalModEntry(modId, name, ver, factory));
            }
        }

        BanglaLogger.info("Detected {} external mod menu integration(s).", detectedEntries.size());
    }

    public List<ExternalModEntry> getDetectedEntries() {
        if (!scanned) {
            scanInstalledMods();
        }
        return Collections.unmodifiableList(detectedEntries);
    }
}
