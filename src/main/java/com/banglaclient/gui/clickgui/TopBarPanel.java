package com.banglaclient.gui.clickgui;

import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.hudeditor.HudEditorScreen;
import com.banglaclient.gui.widget.SearchFieldWidget;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.ClientResourceManager;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Renders the Click GUI Top Header Bar modeled after Screenshot 1:
 * - Left: Replaceable BANGLACLIENT Logo + "BANGLACLIENT" + "MODS & HUD MANAGER"
 * - Right: [HUD EDITOR] button, [SETTINGS] button, "Search modules..." input box, and [X] close button.
 */
public final class TopBarPanel {

    private final SearchFieldWidget searchField = new SearchFieldWidget("Search modules...");
    private final Runnable onSelectSettingsTab;
    private final Runnable onCloseGui;

    public TopBarPanel(Runnable onSelectSettingsTab, Runnable onCloseGui) {
        this.onSelectSettingsTab = onSelectSettingsTab;
        this.onCloseGui = onCloseGui;
    }

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();

        // Bottom separator line
        Render2DUtil.drawRect(context, x, y + height - 1.0f, width, 1.0f, palette.getBorder().getArgb());

        // Left: Brand Logo + Titles
        int logoSize = 28;
        int logoX = Math.round(x + 16.0f);
        int logoY = Math.round(y + (height - logoSize) / 2.0f);
        ClientResourceManager.drawBrandLogo(context, logoX, logoY, logoSize);

        if (mc.textRenderer != null) {
            int titleX = logoX + logoSize + 10;
            context.drawText(mc.textRenderer, "BANGLA", titleX, logoY + 3, palette.getPrimaryText().getArgb(), true);
            int banglaW = mc.textRenderer.getWidth("BANGLA");
            context.drawText(mc.textRenderer, "_CLIENT", titleX + banglaW, logoY + 3, palette.getAccent().getArgb(), true);
            context.drawText(mc.textRenderer, "MODS & HUD MANAGER", titleX, logoY + 15, palette.getSecondaryText().getArgb(), false);
        }

        // Right-side controls
        float closeSize = 24.0f;
        float closeX = x + width - closeSize - 14.0f;
        float ctrlY = y + (height - 26.0f) / 2.0f;

        // Close [X] button
        boolean closeHover = isInside(mouseX, mouseY, closeX, ctrlY + 1.0f, closeSize, closeSize);
        Render2DUtil.drawRoundedRect(context, closeX, ctrlY + 1.0f, closeSize, closeSize, 4.0f,
                closeHover ? palette.getAccent().getArgb() : 0xFF16161C);
        Render2DUtil.drawRoundedOutline(context, closeX, ctrlY + 1.0f, closeSize, closeSize, 4.0f, 1.0f,
                closeHover ? palette.getAccent().getArgb() : palette.getBorder().getArgb());
        if (mc.textRenderer != null) {
            int xw = mc.textRenderer.getWidth("X");
            context.drawText(mc.textRenderer, "X", Math.round(closeX + (closeSize - xw) / 2.0f), Math.round(ctrlY + 9.0f), palette.getPrimaryText().getArgb(), false);
        }

        // Search box
        float searchW = 165.0f;
        float searchX = closeX - searchW - 10.0f;
        searchField.render(context, searchX, ctrlY, searchW, 26.0f, mouseX, mouseY);

        // SETTINGS button (matching Screenshot 1 top bar)
        float settingsW = 84.0f;
        float settingsX = searchX - settingsW - 10.0f;
        boolean settingsHover = isInside(mouseX, mouseY, settingsX, ctrlY, settingsW, 26.0f);
        Render2DUtil.drawRoundedRect(context, settingsX, ctrlY, settingsW, 26.0f, 4.0f, 0xFF14141A);
        Render2DUtil.drawRoundedOutline(context, settingsX, ctrlY, settingsW, 26.0f, 4.0f, 1.0f,
                settingsHover ? palette.getAccent().getArgb() : palette.getBorder().getArgb());
        Render2DUtil.drawCircleOutline(context, Math.round(settingsX + 13.0f), Math.round(ctrlY + 13.0f), 4, palette.getAccent().getArgb());
        if (mc.textRenderer != null) {
            context.drawText(mc.textRenderer, "SETTINGS", Math.round(settingsX + 24.0f), Math.round(ctrlY + 9.0f), palette.getPrimaryText().getArgb(), false);
        }

        // HUD EDITOR quick button
        float hudBtnW = 86.0f;
        float hudBtnX = settingsX - hudBtnW - 8.0f;
        boolean hudHover = isInside(mouseX, mouseY, hudBtnX, ctrlY, hudBtnW, 26.0f);
        Render2DUtil.drawRoundedRect(context, hudBtnX, ctrlY, hudBtnW, 26.0f, 4.0f, 0xFF14141A);
        Render2DUtil.drawRoundedOutline(context, hudBtnX, ctrlY, hudBtnW, 26.0f, 4.0f, 1.0f,
                hudHover ? palette.getAccent().getArgb() : palette.getBorder().getArgb());
        if (mc.textRenderer != null) {
            int hw = mc.textRenderer.getWidth("HUD EDITOR");
            context.drawText(mc.textRenderer, "HUD EDITOR", Math.round(hudBtnX + (hudBtnW - hw) / 2.0f), Math.round(ctrlY + 9.0f), palette.getPrimaryText().getArgb(), false);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        float closeSize = 24.0f;
        float closeX = x + width - closeSize - 14.0f;
        float ctrlY = y + (height - 26.0f) / 2.0f;
        float searchW = 165.0f;
        float searchX = closeX - searchW - 10.0f;
        float settingsW = 84.0f;
        float settingsX = searchX - settingsW - 10.0f;
        float hudBtnW = 86.0f;
        float hudBtnX = settingsX - hudBtnW - 8.0f;

        if (searchField.mouseClicked(mouseX, mouseY, button, searchX, ctrlY, searchW, 26.0f)) {
            return true;
        }
        if (button == 0) {
            if (isInside(mouseX, mouseY, closeX, ctrlY + 1.0f, closeSize, closeSize)) {
                onCloseGui.run();
                return true;
            }
            if (isInside(mouseX, mouseY, settingsX, ctrlY, settingsW, 26.0f)) {
                onSelectSettingsTab.run();
                return true;
            }
            if (isInside(mouseX, mouseY, hudBtnX, ctrlY, hudBtnW, 26.0f)) {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc != null) {
                    mc.setScreen(new HudEditorScreen(mc.currentScreen));
                }
                return true;
            }
        }
        return false;
    }

    public SearchFieldWidget getSearchField() {
        return searchField;
    }

    private boolean isInside(double mx, double my, float x, float y, float w, float h) {
        return mx >= x && mx <= (x + w) && my >= y && my <= (y + h);
    }
}
