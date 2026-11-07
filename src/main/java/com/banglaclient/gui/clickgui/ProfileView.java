package com.banglaclient.gui.clickgui;

import com.banglaclient.config.ConfigBackupService;
import com.banglaclient.config.ConfigManager;
import com.banglaclient.profile.Profile;
import com.banglaclient.profile.ProfileManager;
import com.banglaclient.profile.ServerProfileMapper;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/**
 * Profile & Backup Management View inside the Click GUI:
 * - Built-in Profiles (Default, PvP, Survival, Low-End, Balanced, Quality, Extreme FPS)
 * - Create Custom Profile
 * - Server-Specific Profile Auto-Switching (play.banglaSMP.net)
 * - Timestamped Config Backup & Restore
 */
public final class ProfileView {

    private int customCounter = 1;

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();
        if (mc.textRenderer == null) return;

        float pad = 14.0f;
        float cx = x + pad;
        float cy = y + pad;
        float contentW = width - pad * 2.0f;

        context.drawText(mc.textRenderer, "CONFIGURATION PROFILES & PRESETS", Math.round(cx), Math.round(cy), palette.getPrimaryText().getArgb(), true);
        cy += 18.0f;

        List<Profile> profiles = ProfileManager.getInstance().getAllProfiles();
        Profile active = ProfileManager.getInstance().getActiveProfile();

        int cols = 3;
        float gap = 10.0f;
        float cardW = (contentW - gap * (cols - 1)) / cols;
        float cardH = 48.0f;

        for (int i = 0; i < profiles.size(); i++) {
            Profile p = profiles.get(i);
            int col = i % cols;
            int row = i / cols;
            float px = cx + col * (cardW + gap);
            float py = cy + row * (cardH + gap);

            boolean isCurrent = (p == active);
            boolean hover = mouseX >= px && mouseX <= px + cardW && mouseY >= py && mouseY <= py + cardH;

            Render2DUtil.drawRoundedRect(context, px, py, cardW, cardH, 5.0f, palette.getCard().getArgb());
            Render2DUtil.drawRoundedOutline(context, px, py, cardW, cardH, 5.0f, isCurrent ? 1.5f : 1.0f,
                    isCurrent || hover ? palette.getAccent().getArgb() : palette.getBorder().getArgb());

            context.drawText(mc.textRenderer, p.getName(), Math.round(px + 10.0f), Math.round(py + 10.0f), palette.getPrimaryText().getArgb(), true);
            String badge = isCurrent ? "ACTIVE PROFILE" : (p.isBuiltIn() ? "BUILT-IN PRESET" : "CUSTOM PROFILE");
            context.drawText(mc.textRenderer, badge, Math.round(px + 10.0f), Math.round(py + 26.0f),
                    isCurrent ? palette.getAccent().getArgb() : palette.getSecondaryText().getArgb(), false);
        }

        int totalRows = (profiles.size() + cols - 1) / cols;
        cy += totalRows * (cardH + gap) + 12.0f;

        // Server-Specific Profile Panel
        Render2DUtil.drawRoundedRect(context, cx, cy, contentW, 44.0f, 5.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, cx, cy, contentW, 44.0f, 5.0f, 1.0f, palette.getBorder().getArgb());
        boolean srvAuto = ServerProfileMapper.getInstance().isEnabled();
        context.drawText(mc.textRenderer, "Server-Specific Auto Profile (play.banglaSMP.net -> PvP)", Math.round(cx + 12.0f), Math.round(cy + 10.0f), palette.getPrimaryText().getArgb(), false);
        context.drawText(mc.textRenderer, "Automatically applies local profile rules when joining configured servers.", Math.round(cx + 12.0f), Math.round(cy + 24.0f), palette.getSecondaryText().getArgb(), false);

        float toggleW = 64.0f;
        float toggleX = cx + contentW - toggleW - 12.0f;
        Render2DUtil.drawRoundedRect(context, toggleX, cy + 12.0f, toggleW, 20.0f, 4.0f, srvAuto ? palette.getAccent().getArgb() : palette.getDisabled().getArgb());
        String tStr = srvAuto ? "ENABLED" : "DISABLED";
        context.drawText(mc.textRenderer, tStr, Math.round(toggleX + (toggleW - mc.textRenderer.getWidth(tStr)) / 2.0f), Math.round(cy + 18.0f), palette.getPrimaryText().getArgb(), false);

        cy += 58.0f;

        // Bottom Action Buttons: Create Custom Profile, Create Backup, Restore Latest Backup
        float btnW = (contentW - gap * 2.0f) / 3.0f;
        float btnH = 26.0f;
        drawActionButton(context, "+ CREATE CUSTOM PROFILE", cx, cy, btnW, btnH, palette);
        drawActionButton(context, "CREATE CONFIG BACKUP", cx + btnW + gap, cy, btnW, btnH, palette);
        drawActionButton(context, "RESTORE LATEST BACKUP", cx + (btnW + gap) * 2.0f, cy, btnW, btnH, palette);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        if (button != 0) return false;
        float pad = 14.0f;
        float cx = x + pad;
        float cy = y + pad + 18.0f;
        float contentW = width - pad * 2.0f;

        List<Profile> profiles = ProfileManager.getInstance().getAllProfiles();
        int cols = 3;
        float gap = 10.0f;
        float cardW = (contentW - gap * (cols - 1)) / cols;
        float cardH = 48.0f;

        for (int i = 0; i < profiles.size(); i++) {
            Profile p = profiles.get(i);
            int col = i % cols;
            int row = i / cols;
            float px = cx + col * (cardW + gap);
            float py = cy + row * (cardH + gap);
            if (mouseX >= px && mouseX <= px + cardW && mouseY >= py && mouseY <= py + cardH) {
                ProfileManager.getInstance().applyProfile(p.getId());
                return true;
            }
        }

        int totalRows = (profiles.size() + cols - 1) / cols;
        float srvY = cy + totalRows * (cardH + gap) + 12.0f;
        if (mouseX >= cx && mouseX <= cx + contentW && mouseY >= srvY && mouseY <= srvY + 44.0f) {
            ServerProfileMapper.getInstance().setEnabled(!ServerProfileMapper.getInstance().isEnabled());
            return true;
        }

        float btnY = srvY + 58.0f;
        float btnW = (contentW - gap * 2.0f) / 3.0f;
        if (mouseY >= btnY && mouseY <= btnY + 26.0f) {
            if (mouseX >= cx && mouseX <= cx + btnW) {
                ProfileManager.getInstance().createCustomProfile("Custom " + (customCounter++));
                return true;
            }
            if (mouseX >= cx + btnW + gap && mouseX <= cx + btnW * 2.0f + gap) {
                ConfigBackupService.getInstance().createTimestampedBackup(ConfigManager.getInstance().getMainConfigFile());
                return true;
            }
            if (mouseX >= cx + (btnW + gap) * 2.0f && mouseX <= cx + contentW) {
                if (ConfigBackupService.getInstance().restoreLatestBackup(ConfigManager.getInstance().getMainConfigFile())) {
                    ConfigManager.getInstance().loadConfig();
                }
                return true;
            }
        }
        return false;
    }

    private void drawActionButton(DrawContext context, String label, float x, float y, float w, float h, ThemePalette palette) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Render2DUtil.drawRoundedRect(context, x, y, w, h, 4.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, x, y, w, h, 4.0f, 1.0f, palette.getAccent().getArgb());
        int lw = mc.textRenderer.getWidth(label);
        context.drawText(mc.textRenderer, label, Math.round(x + (w - lw) / 2.0f), Math.round(y + 9.0f), palette.getPrimaryText().getArgb(), false);
    }
}
