package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

/**
 * Module 129: Biome Display
 * Displays formatted current biome name on a free-floating HUD element.
 */
public final class BiomeDisplay extends HudModule {

    public BiomeDisplay() {
        super(
                "biome_display",
                "Biome Display",
                "Displays the current biome at your position.",
                ModuleCategory.HUD_INFORMATION,
                false,
                14.0f,
                652.0f,
                114.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        String biomeName = "Plains";
        if (mc.world != null && mc.player != null) {
            Optional<RegistryKey<Biome>> keyOpt = mc.world.getBiome(mc.player.getBlockPos()).getKey();
            if (keyOpt.isPresent()) {
                String path = keyOpt.get().getValue().getPath();
                biomeName = formatBiomePath(path);
            }
        }
        drawSimpleLabelValue(context, "Biome", biomeName);
    }

    private String formatBiomePath(String rawPath) {
        if (rawPath == null || rawPath.isEmpty()) return "Unknown";
        String[] parts = rawPath.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i];
            if (!p.isEmpty()) {
                sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
                if (i < parts.length - 1) sb.append(" ");
            }
        }
        return sb.toString();
    }
}
