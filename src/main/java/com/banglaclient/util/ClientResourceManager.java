package com.banglaclient.util;

import com.banglaclient.core.ClientCore;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages replaceable BANGLACLIENT branding resources:
 * 1. Logo: assets/banglaclient/textures/gui/logo.png
 * 2. Main Menu Background: assets/banglaclient/textures/gui/main_menu_background_demo.png
 * 3. Loading Background: assets/banglaclient/textures/gui/loading_background.png
 *
 * HOW TO REPLACE WITHOUT CHANGING JAVA CODE:
 * - If you want to change the BANGLACLIENT Logo, replace:
 *   src/main/resources/assets/banglaclient/textures/gui/logo.png
 * - If you want to change the Main Menu background, replace:
 *   src/main/resources/assets/banglaclient/textures/gui/main_menu_background_demo.png
 *
 * If a PNG file is not yet placed in the resources folder, this class automatically
 * renders a clean procedural BANGLACLIENT emblem or dark atmospheric background so
 * the client never shows missing purple-black checkerboard textures.
 */
public final class ClientResourceManager {

    public static final Identifier LOGO_TEXTURE = Identifier.of(ClientCore.MOD_ID, "textures/gui/logo.png");
    public static final Identifier MAIN_MENU_BG_TEXTURE = Identifier.of(ClientCore.MOD_ID, "textures/gui/main_menu_background_demo.png");
    public static final Identifier LOADING_BG_TEXTURE = Identifier.of(ClientCore.MOD_ID, "textures/gui/loading_background.png");

    private static final Map<Identifier, Boolean> RESOURCE_EXISTENCE_CACHE = new HashMap<>();

    private ClientResourceManager() {
    }

    public static void invalidateCache() {
        RESOURCE_EXISTENCE_CACHE.clear();
    }

    public static boolean hasResource(Identifier id) {
        if (id == null) {
            return false;
        }
        return RESOURCE_EXISTENCE_CACHE.computeIfAbsent(id, key -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc == null || mc.getResourceManager() == null) {
                return false;
            }
            return mc.getResourceManager().getResource(key).isPresent();
        });
    }

    /**
     * Renders the BANGLACLIENT logo from assets/banglaclient/textures/gui/logo.png if present,
     * or renders a sharp procedural Black/White/Red 'B' crest emblem if the PNG has not been placed yet.
     */
    public static void drawBrandLogo(DrawContext context, int x, int y, int size) {
        if (hasResource(LOGO_TEXTURE)) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, LOGO_TEXTURE, x, y, 0.0f, 0.0f, size, size, size, size);
            return;
        }

        ThemePalette palette = ThemeManager.getInstance().getPalette();
        int red = palette.getAccent().getArgb();
        int darkRed = palette.getHover().getArgb();
        int white = palette.getPrimaryText().getArgb();

        // Outer stylized shield/emblem frame
        Render2DUtil.drawRoundedRect(context, x, y, size, size, Math.max(3.0f, size * 0.18f), 0xFF14141A);
        Render2DUtil.drawRoundedOutline(context, x, y, size, size, Math.max(3.0f, size * 0.18f), 1.5f, red);

        // Procedural stylized "B" + lightning slash
        int pad = Math.max(3, size / 5);
        int stemW = Math.max(2, size / 7);
        // Red diagonal accent wing
        Render2DUtil.drawRect(context, x + pad, y + pad, stemW + 1, size - pad * 2.0f, red);
        // White top & bottom loops of 'B'
        int loopW = Math.max(4, size - (pad * 2) - stemW);
        int halfH = (size - pad * 2) / 2;
        Render2DUtil.drawRoundedOutline(context, x + pad + stemW, y + pad, loopW, halfH, 2.0f, stemW * 0.7f, white);
        Render2DUtil.drawRoundedOutline(context, x + pad + stemW, y + pad + halfH - 1, loopW, halfH + 1, 2.0f, stemW * 0.7f, darkRed);
    }

    /**
     * Renders the Main Menu background from assets/banglaclient/textures/gui/main_menu_background_demo.png
     * if present, or a dark atmospheric vignette background if absent.
     */
    public static void drawMainMenuBackground(DrawContext context, int screenWidth, int screenHeight) {
        if (hasResource(MAIN_MENU_BG_TEXTURE)) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    MAIN_MENU_BG_TEXTURE,
                    0, 0, 0.0f, 0.0f,
                    screenWidth, screenHeight,
                    screenWidth, screenHeight
            );
            // Subtle dark overlay for crisp button readability
            context.fill(0, 0, screenWidth, screenHeight, 0x5508080A);
            return;
        }

        // Procedural dark atmospheric landscape fallback
        context.fillGradient(0, 0, screenWidth, screenHeight, 0xFF0A0A0E, 0xFF15151C);
        // Subtle horizon glow
        context.fillGradient(0, screenHeight / 2, screenWidth, screenHeight, 0x00000000, 0x28E52521);
    }
}
