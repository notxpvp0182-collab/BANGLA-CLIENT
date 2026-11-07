package com.banglaclient.gui.clickgui;

import com.banglaclient.animation.SmoothFloat;
import com.banglaclient.config.ConfigManager;
import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.widget.SmoothScrollBar;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Primary BANGLACLIENT Click GUI / Module Manager Screen modeled after Screenshot 1.
 *
 * Features:
 * - Centered dark modal window with red/black/white theme
 * - Top bar with replaceable logo, search field, settings access, HUD editor launcher, and close [X]
 * - Left sidebar with Dashboard, Profiles, 19 Module Categories, Settings, Theme, and About
 * - Responsive 4-column module card grid with procedural custom icons
 * - Smooth mouse wheel AND draggable scrollbar thumb scrolling
 * - Left-click card to toggle module, Right-click card to open ModuleSettingsModal
 * - Bottom status bar: "STATUS: CONNECTED | BANGLACLIENT | VERSION: 1.0.0"
 */
public final class BanglaClickGuiScreen extends Screen {

    private final TopBarPanel topBar;
    private final SidebarPanel sidebar;
    private final DashboardView dashboardView = new DashboardView();
    private final ProfileView profileView = new ProfileView();
    private final ThemeEditorView themeEditorView = new ThemeEditorView();
    private final GlobalSettingsView globalSettingsView = new GlobalSettingsView();
    private final AboutView aboutView = new AboutView();

    private final SmoothScrollBar gridScrollBar = new SmoothScrollBar();
    private final SmoothFloat openAnimation = new SmoothFloat(0.0f, 14.0f);
    private final Map<Module, ModuleCardWidget> cardWidgets = new HashMap<>();

    private ModuleSettingsModal activeModal = null;

    public BanglaClickGuiScreen() {
        super(Text.literal(ClientCore.MOD_NAME));
        this.sidebar = new SidebarPanel(tab -> gridScrollBar.reset());
        this.topBar = new TopBarPanel(
                () -> sidebar.setActiveTab(SidebarPanel.NavTab.SETTINGS),
                this::close
        );
        this.openAnimation.setTarget(1.0f);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Subtle world darkener
        context.fill(0, 0, this.width, this.height, 0x66040406);

        ThemePalette palette = ThemeManager.getInstance().getPalette();
        float anim = openAnimation.updateAndGet();

        float winW = Math.min(760.0f, this.width - 28.0f);
        float winH = Math.min(430.0f, this.height - 28.0f);
        float winX = (this.width - winW) / 2.0f;
        float winY = (this.height - winH) / 2.0f;

        float scale = 0.94f + (0.06f * anim);
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(scale, scale);
        context.getMatrices().translate(-centerX, -centerY);

        // Main outer window container
        Render2DUtil.drawRoundedRect(context, winX, winY, winW, winH, 8.0f, palette.getBackground().getArgb());
        Render2DUtil.drawRoundedOutline(context, winX, winY, winW, winH, 8.0f, 1.5f, palette.getBorder().getArgb());

        float topBarH = 44.0f;
        float footerH = 22.0f;
        float sideW = 142.0f;
        float bodyY = winY + topBarH;
        float bodyH = winH - topBarH - footerH;
        float contentX = winX + sideW;
        float contentW = winW - sideW;

        topBar.render(context, winX, winY, winW, topBarH, mouseX, mouseY);
        sidebar.render(context, winX, bodyY, sideW, bodyH, mouseX, mouseY);

        // If user typed in search bar, show filtered modules across all categories
        String searchQuery = topBar.getSearchField().getText();
        if (searchQuery != null && !searchQuery.isBlank()) {
            renderModuleGrid(context, ModuleRegistry.getInstance().search(searchQuery), contentX, bodyY, contentW, bodyH, mouseX, mouseY);
        } else {
            switch (sidebar.getActiveTab()) {
                case DASHBOARD -> dashboardView.render(context, contentX, bodyY, contentW, bodyH, mouseX, mouseY);
                case PROFILES -> profileView.render(context, contentX, bodyY, contentW, bodyH, mouseX, mouseY);
                case CATEGORY -> {
                    List<Module> catModules = ModuleRegistry.getInstance().getByCategory(sidebar.getActiveCategory());
                    renderModuleGrid(context, catModules, contentX, bodyY, contentW, bodyH, mouseX, mouseY);
                }
                case SETTINGS -> globalSettingsView.render(context, contentX, bodyY, contentW, bodyH, mouseX, mouseY);
                case THEME -> themeEditorView.render(context, contentX, bodyY, contentW, bodyH, mouseX, mouseY);
                case ABOUT -> aboutView.render(context, contentX, bodyY, contentW, bodyH);
            }
        }

        // Bottom Status Bar (Screenshot 1 bottom bar)
        float footerY = winY + winH - footerH;
        Render2DUtil.drawRect(context, winX, footerY, winW, 1.0f, palette.getBorder().getArgb());
        context.drawText(this.textRenderer, "STATUS: ", Math.round(winX + 14.0f), Math.round(footerY + 7.0f), palette.getSecondaryText().getArgb(), false);
        context.drawText(this.textRenderer, "CONNECTED", Math.round(winX + 58.0f), Math.round(footerY + 7.0f), palette.getAccent().getArgb(), false);

        String centerBrand = "B A N G L A C L I E N T";
        int cbw = this.textRenderer.getWidth(centerBrand);
        context.drawText(this.textRenderer, centerBrand, Math.round(winX + (winW - cbw) / 2.0f), Math.round(footerY + 7.0f), palette.getSecondaryText().getArgb(), false);

        String verLabel = "VERSION:  " + ClientCore.MOD_VERSION;
        int vw = this.textRenderer.getWidth(verLabel);
        context.drawText(this.textRenderer, verLabel, Math.round(winX + winW - vw - 14.0f), Math.round(footerY + 7.0f), palette.getAccent().getArgb(), false);

        context.getMatrices().popMatrix();

        // Modal settings overlay on top if active
        if (activeModal != null) {
            activeModal.render(context, this.width, this.height, mouseX, mouseY);
        }
    }

    private void renderModuleGrid(DrawContext context, List<Module> modules, float x, float y, float width, float height, int mouseX, int mouseY) {
        float pad = 10.0f;
        float availW = width - (pad * 2.0f) - 8.0f;
        int cols = availW >= 540.0f ? 4 : (availW >= 390.0f ? 3 : 2);
        float gap = 8.0f;
        float cardW = (availW - gap * (cols - 1)) / cols;
        float cardH = 74.0f;

        int rows = (modules.size() + cols - 1) / cols;
        float totalContentH = pad * 2.0f + rows * cardH + Math.max(0, rows - 1) * gap;

        gridScrollBar.updateDimensions(height, totalContentH);
        float scrollOffset = gridScrollBar.getScrollOffset();

        context.enableScissor(Math.round(x), Math.round(y), Math.round(x + width), Math.round(y + height));

        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            int col = i % cols;
            int row = i / cols;
            float cx = x + pad + col * (cardW + gap);
            float cy = y + pad + row * (cardH + gap) - scrollOffset;

            if (cy + cardH >= y && cy <= y + height) {
                ModuleCardWidget widget = cardWidgets.computeIfAbsent(m, ModuleCardWidget::new);
                widget.render(context, cx, cy, cardW, cardH, mouseX, mouseY);
            }
        }

        context.disableScissor();
        gridScrollBar.render(context, x + width - 7.0f, y + 6.0f, 4.0f, height - 12.0f, mouseX, mouseY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (activeModal != null) {
            return activeModal.mouseClicked(mouseX, mouseY, button, this.width, this.height);
        }

        float winW = Math.min(760.0f, this.width - 28.0f);
        float winH = Math.min(430.0f, this.height - 28.0f);
        float winX = (this.width - winW) / 2.0f;
        float winY = (this.height - winH) / 2.0f;

        float topBarH = 44.0f;
        float footerH = 22.0f;
        float sideW = 142.0f;
        float bodyY = winY + topBarH;
        float bodyH = winH - topBarH - footerH;
        float contentX = winX + sideW;
        float contentW = winW - sideW;

        if (topBar.mouseClicked(mouseX, mouseY, button, winX, winY, winW, topBarH)) {
            return true;
        }
        if (sidebar.mouseClicked(mouseX, mouseY, button, winX, bodyY, sideW, bodyH)) {
            topBar.getSearchField().setText("");
            return true;
        }

        String searchQuery = topBar.getSearchField().getText();
        if (searchQuery != null && !searchQuery.isBlank()) {
            return handleModuleGridClick(ModuleRegistry.getInstance().search(searchQuery), mouseX, mouseY, button, contentX, bodyY, contentW, bodyH);
        }

        return switch (sidebar.getActiveTab()) {
            case DASHBOARD -> dashboardView.mouseClicked(mouseX, mouseY, button, contentX, bodyY, contentW, bodyH);
            case PROFILES -> profileView.mouseClicked(mouseX, mouseY, button, contentX, bodyY, contentW, bodyH);
            case CATEGORY -> handleModuleGridClick(ModuleRegistry.getInstance().getByCategory(sidebar.getActiveCategory()), mouseX, mouseY, button, contentX, bodyY, contentW, bodyH);
            case SETTINGS -> globalSettingsView.mouseClicked(mouseX, mouseY, button, contentX, bodyY, contentW, bodyH);
            case THEME -> themeEditorView.mouseClicked(mouseX, mouseY, button, contentX, bodyY, contentW, bodyH);
            case ABOUT -> false;
        };
    }

    private boolean handleModuleGridClick(List<Module> modules, double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        if (gridScrollBar.mouseClicked(mouseX, mouseY, button, x + width - 9.0f, y + 6.0f, 7.0f, height - 12.0f)) {
            return true;
        }
        if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) {
            return false;
        }

        float pad = 10.0f;
        float availW = width - (pad * 2.0f) - 8.0f;
        int cols = availW >= 540.0f ? 4 : (availW >= 390.0f ? 3 : 2);
        float gap = 8.0f;
        float cardW = (availW - gap * (cols - 1)) / cols;
        float cardH = 74.0f;
        float scrollOffset = gridScrollBar.getScrollOffset();

        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            int col = i % cols;
            int row = i / cols;
            float cx = x + pad + col * (cardW + gap);
            float cy = y + pad + row * (cardH + gap) - scrollOffset;

            if (mouseX >= cx && mouseX <= cx + cardW && mouseY >= cy && mouseY <= cy + cardH) {
                if (button == 0) {
                    m.toggle();
                    ConfigManager.getInstance().saveConfig();
                } else if (button == 1) {
                    activeModal = new ModuleSettingsModal(m, () -> activeModal = null);
                }
                return true;
            }
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (activeModal != null) {
            return activeModal.mouseDragged(mouseX, mouseY, button, this.width, this.height);
        }
        float winH = Math.min(430.0f, this.height - 28.0f);
        float bodyH = winH - 44.0f - 22.0f;
        if (sidebar.mouseDragged(mouseX, mouseY, button, bodyH)) {
            return true;
        }
        if (gridScrollBar.mouseDragged(mouseX, mouseY, button, bodyH - 12.0f)) {
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (activeModal != null) {
            activeModal.mouseReleased();
        }
        sidebar.mouseReleased();
        gridScrollBar.mouseReleased();
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (activeModal != null) {
            return activeModal.mouseScrolled(verticalAmount);
        }
        float winW = Math.min(760.0f, this.width - 28.0f);
        float winH = Math.min(430.0f, this.height - 28.0f);
        float winX = (this.width - winW) / 2.0f;
        float winY = (this.height - winH) / 2.0f;
        float bodyY = winY + 44.0f;
        float bodyH = winH - 66.0f;

        if (sidebar.mouseScrolled(mouseX, mouseY, verticalAmount, winX, bodyY, 142.0f, bodyH)) {
            return true;
        }
        gridScrollBar.onMouseScroll(verticalAmount);
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (activeModal != null) {
            return activeModal.keyPressed(keyCode);
        }
        if (topBar.getSearchField().keyPressed(keyCode, scanCode, modifiers)) {
            gridScrollBar.reset();
            return true;
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (activeModal == null && topBar.getSearchField().charTyped(chr, modifiers)) {
            gridScrollBar.reset();
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        ConfigManager.getInstance().saveConfig();
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click != null && this.mouseClicked(click.x(), click.y(), click.button())) {
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (click != null && this.mouseDragged(click.x(), click.y(), click.button(), deltaX, deltaY)) {
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (click != null && this.mouseReleased(click.x(), click.y(), click.button())) {
            return true;
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input != null && this.keyPressed(input.key(), input.scancode(), input.modifiers())) {
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (input != null && this.charTyped((char) input.codepoint(), input.modifiers())) {
            return true;
        }
        return super.charTyped(input);
    }
}
