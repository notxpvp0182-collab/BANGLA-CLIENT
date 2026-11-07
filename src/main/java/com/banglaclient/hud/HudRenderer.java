package com.banglaclient.hud;

import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.hudeditor.HudEditorScreen;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/**
 * Renders all active HUD elements during gameplay.
 * Skips rendering when F1 (hudHidden) is active, Vanilla Mode is active,
 * or when HudEditorScreen is open (since HudEditorScreen renders them interactively).
 */
public final class HudRenderer {

    private static final HudRenderer INSTANCE = new HudRenderer();

    private HudRenderer() {
    }

    public static HudRenderer getInstance() {
        return INSTANCE;
    }

    public void renderInGameHud(DrawContext context, float tickDelta) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.options.hudHidden || mc.currentScreen instanceof HudEditorScreen) {
            return;
        }

        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        List<HudModule> enabledHuds = ModuleRegistry.getInstance().getEnabledHudModules();
        for (int i = 0, size = enabledHuds.size(); i < size; i++) {
            HudModule hud = enabledHuds.get(i);
            hud.renderHud(context, screenWidth, screenHeight, tickDelta, false);
        }
    }
}
