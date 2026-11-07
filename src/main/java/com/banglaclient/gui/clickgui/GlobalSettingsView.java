package com.banglaclient.gui.clickgui;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.core.ClientCore;
import com.banglaclient.core.ClientState;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Global Client Settings Panel (Section 30):
 * - Show Module Descriptions (ON/OFF)
 * - GUI Animations (ON/OFF)
 * - Animation Speed multiplier
 * - Safe Mode (ON/OFF)
 * - Compatibility Mode (ON/OFF)
 * - Vanilla Mode (ON/OFF)
 */
public final class GlobalSettingsView {

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();
        ClientState state = ClientCore.getInstance().getClientState();
        if (mc.textRenderer == null) return;

        float pad = 14.0f;
        float cx = x + pad;
        float cy = y + pad;
        float contentW = width - pad * 2.0f;

        context.drawText(mc.textRenderer, "GLOBAL CLIENT SETTINGS", Math.round(cx), Math.round(cy), palette.getPrimaryText().getArgb(), true);
        cy += 18.0f;

        drawToggleRow(context, cx, cy, contentW, "Show Module Descriptions",
                "Display module descriptions on Click GUI cards", state.isShowModuleDescriptions(), palette);
        cy += 38.0f;

        drawToggleRow(context, cx, cy, contentW, "GUI Animations",
                "Enable smooth fade, scroll, and hover transitions", state.isAnimationsEnabled(), palette);
        cy += 38.0f;

        drawToggleRow(context, cx, cy, contentW, "Safe Mode",
                "Disable experimental low-level optimizations for maximum stability", state.isSafeMode(), palette);
        cy += 38.0f;

        drawToggleRow(context, cx, cy, contentW, "Compatibility Mode",
                "Use conservative rendering hooks when running alongside third-party shader mods", state.isCompatibilityMode(), palette);
        cy += 38.0f;

        drawToggleRow(context, cx, cy, contentW, "Vanilla Mode",
                "Temporarily suspend all BANGLACLIENT modules and HUDs for a pure vanilla session", state.isVanillaMode(), palette);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        if (button != 0) return false;
        ClientState state = ClientCore.getInstance().getClientState();
        float pad = 14.0f;
        float cx = x + pad;
        float cy = y + pad + 18.0f;
        float contentW = width - pad * 2.0f;

        if (mouseX >= cx && mouseX <= cx + contentW) {
            if (mouseY >= cy && mouseY <= cy + 32.0f) {
                state.setShowModuleDescriptions(!state.isShowModuleDescriptions());
                ConfigManager.getInstance().saveConfig();
                return true;
            }
            if (mouseY >= cy + 38.0f && mouseY <= cy + 70.0f) {
                state.setAnimationsEnabled(!state.isAnimationsEnabled());
                ConfigManager.getInstance().saveConfig();
                return true;
            }
            if (mouseY >= cy + 76.0f && mouseY <= cy + 108.0f) {
                state.setSafeMode(!state.isSafeMode());
                ConfigManager.getInstance().saveConfig();
                return true;
            }
            if (mouseY >= cy + 114.0f && mouseY <= cy + 146.0f) {
                state.setCompatibilityMode(!state.isCompatibilityMode());
                ConfigManager.getInstance().saveConfig();
                return true;
            }
            if (mouseY >= cy + 152.0f && mouseY <= cy + 184.0f) {
                state.setVanillaMode(!state.isVanillaMode());
                ConfigManager.getInstance().saveConfig();
                return true;
            }
        }
        return false;
    }

    private void drawToggleRow(DrawContext context, float x, float y, float w, String title, String subtitle, boolean active, ThemePalette palette) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Render2DUtil.drawRoundedRect(context, x, y, w, 32.0f, 5.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, x, y, w, 32.0f, 5.0f, 1.0f, palette.getBorder().getArgb());

        context.drawText(mc.textRenderer, title, Math.round(x + 12.0f), Math.round(y + 6.0f), palette.getPrimaryText().getArgb(), true);
        context.drawText(mc.textRenderer, subtitle, Math.round(x + 12.0f), Math.round(y + 18.0f), palette.getSecondaryText().getArgb(), false);

        float btnW = 64.0f;
        float btnX = x + w - btnW - 10.0f;
        Render2DUtil.drawRoundedRect(context, btnX, y + 6.0f, btnW, 20.0f, 4.0f, active ? palette.getAccent().getArgb() : palette.getDisabled().getArgb());
        String status = active ? "ON" : "OFF";
        context.drawText(mc.textRenderer, status, Math.round(btnX + (btnW - mc.textRenderer.getWidth(status)) / 2.0f), Math.round(y + 12.0f), palette.getPrimaryText().getArgb(), false);
    }
}
