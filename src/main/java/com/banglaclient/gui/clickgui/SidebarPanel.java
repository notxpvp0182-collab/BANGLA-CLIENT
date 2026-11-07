package com.banglaclient.gui.clickgui;

import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.widget.SmoothScrollBar;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

/**
 * Left Navigation Sidebar modeled after Screenshot 1 & Section 11:
 * Top:
 *   - DASHBOARD
 *   - PROFILES
 * Scrollable Middle:
 *   - Categories 01 through 19
 * Bottom:
 *   - SETTINGS
 *   - THEME
 *   - ABOUT
 *   - Status indicator (CONNECTED / VERSION)
 */
public final class SidebarPanel {

    public enum NavTab {
        DASHBOARD,
        PROFILES,
        CATEGORY,
        SETTINGS,
        THEME,
        ABOUT
    }

    private NavTab activeTab = NavTab.CATEGORY;
    private ModuleCategory activeCategory = ModuleCategory.OPTIMIZER;
    private final SmoothScrollBar categoryScrollBar = new SmoothScrollBar();
    private final Consumer<NavTab> onTabChanged;

    public SidebarPanel(Consumer<NavTab> onTabChanged) {
        this.onTabChanged = onTabChanged;
    }

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();

        // Sidebar right border
        Render2DUtil.drawRect(context, x + width - 1.0f, y, 1.0f, height, palette.getBorder().getArgb());

        float padX = x + 8.0f;
        float itemW = width - 16.0f;
        float itemH = 22.0f;

        // Top Static Tabs: DASHBOARD & PROFILES
        renderNavButton(context, "DASHBOARD", padX, y + 8.0f, itemW, itemH, activeTab == NavTab.DASHBOARD, mouseX, mouseY, palette);
        renderNavButton(context, "PROFILES", padX, y + 34.0f, itemW, itemH, activeTab == NavTab.PROFILES, mouseX, mouseY, palette);

        Render2DUtil.drawRect(context, padX, y + 60.0f, itemW, 1.0f, palette.getBorder().getArgb());

        // Scrollable 19 Categories Area
        float catAreaTop = y + 65.0f;
        float bottomReserved = 108.0f;
        float catAreaHeight = Math.max(40.0f, height - 65.0f - bottomReserved);

        ModuleCategory[] categories = ModuleCategory.values();
        float totalCatHeight = categories.length * 21.0f;
        categoryScrollBar.updateDimensions(catAreaHeight, totalCatHeight);
        float scrollOffset = categoryScrollBar.getScrollOffset();

        context.enableScissor(Math.round(x), Math.round(catAreaTop), Math.round(x + width - 1), Math.round(catAreaTop + catAreaHeight));
        float cy = catAreaTop - scrollOffset;
        for (ModuleCategory category : categories) {
            if (cy + 20.0f >= catAreaTop && cy <= catAreaTop + catAreaHeight) {
                boolean selected = (activeTab == NavTab.CATEGORY && activeCategory == category);
                renderCategoryButton(context, category.getDisplayName(), padX, cy, itemW - 5.0f, 19.0f, selected, mouseX, mouseY, palette);
            }
            cy += 21.0f;
        }
        context.disableScissor();

        categoryScrollBar.render(context, x + width - 5.0f, catAreaTop, 3.0f, catAreaHeight, mouseX, mouseY);

        // Bottom Static Tabs: SETTINGS, THEME, ABOUT + Status Card
        float bottomTop = y + height - bottomReserved + 4.0f;
        Render2DUtil.drawRect(context, padX, bottomTop - 4.0f, itemW, 1.0f, palette.getBorder().getArgb());

        renderCategoryButton(context, "SETTINGS", padX, bottomTop, itemW, 18.0f, activeTab == NavTab.SETTINGS, mouseX, mouseY, palette);
        renderCategoryButton(context, "THEME", padX, bottomTop + 20.0f, itemW, 18.0f, activeTab == NavTab.THEME, mouseX, mouseY, palette);
        renderCategoryButton(context, "ABOUT", padX, bottomTop + 40.0f, itemW, 18.0f, activeTab == NavTab.ABOUT, mouseX, mouseY, palette);

        // Bottom-left CONNECTED / VERSION badge (Screenshot 1 reference)
        if (mc.textRenderer != null) {
            float statusY = bottomTop + 64.0f;
            Render2DUtil.drawRect(context, padX + 2.0f, statusY + 2.0f, 5.0f, 5.0f, palette.getAccent().getArgb());
            context.drawText(mc.textRenderer, "CONNECTED", Math.round(padX + 11.0f), Math.round(statusY), palette.getAccent().getArgb(), false);
            context.drawText(mc.textRenderer, "VERSION " + ClientCore.MOD_VERSION, Math.round(padX + 2.0f), Math.round(statusY + 13.0f), palette.getSecondaryText().getArgb(), false);
        }
    }

    private void renderNavButton(DrawContext context, String label, float x, float y, float w, float h, boolean active, int mx, int my, ThemePalette palette) {
        MinecraftClient mc = MinecraftClient.getInstance();
        boolean hovered = mx >= x && mx <= x + w && my >= y && my <= y + h;
        if (active) {
            Render2DUtil.drawHorizontalGradient(context, x, y, w, h, palette.getAccent().withAlpha(200).getArgb(), 0x22E52521);
            Render2DUtil.drawRoundedOutline(context, x, y, w, h, 4.0f, 1.0f, palette.getAccent().getArgb());
        } else if (hovered) {
            Render2DUtil.drawRoundedRect(context, x, y, w, h, 4.0f, 0xFF1A1A22);
        }
        if (mc.textRenderer != null) {
            int color = active ? palette.getPrimaryText().getArgb() : (hovered ? palette.getPrimaryText().getArgb() : palette.getSecondaryText().getArgb());
            context.drawText(mc.textRenderer, label, Math.round(x + 10.0f), Math.round(y + (h - mc.textRenderer.fontHeight) / 2.0f) + 1, color, false);
        }
    }

    private void renderCategoryButton(DrawContext context, String label, float x, float y, float w, float h, boolean active, int mx, int my, ThemePalette palette) {
        MinecraftClient mc = MinecraftClient.getInstance();
        boolean hovered = mx >= x && mx <= x + w && my >= y && my <= y + h;
        if (active) {
            Render2DUtil.drawRoundedRect(context, x, y, w, h, 3.0f, palette.getAccent().withAlpha(55).getArgb());
            Render2DUtil.drawRect(context, x, y + 2.0f, 2.0f, h - 4.0f, palette.getAccent().getArgb());
        } else if (hovered) {
            Render2DUtil.drawRoundedRect(context, x, y, w, h, 3.0f, 0xFF181820);
        }
        if (mc.textRenderer != null) {
            String trimmed = mc.textRenderer.trimToWidth(label, Math.round(w - 14.0f));
            int color = active ? palette.getPrimaryText().getArgb() : (hovered ? palette.getPrimaryText().getArgb() : palette.getSecondaryText().getArgb());
            context.drawText(mc.textRenderer, trimmed, Math.round(x + 8.0f), Math.round(y + (h - mc.textRenderer.fontHeight) / 2.0f) + 1, color, false);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        if (button != 0) {
            return false;
        }
        float padX = x + 8.0f;
        float itemW = width - 16.0f;

        if (isInside(mouseX, mouseY, padX, y + 8.0f, itemW, 22.0f)) {
            setActiveTab(NavTab.DASHBOARD);
            return true;
        }
        if (isInside(mouseX, mouseY, padX, y + 34.0f, itemW, 22.0f)) {
            setActiveTab(NavTab.PROFILES);
            return true;
        }

        float catAreaTop = y + 65.0f;
        float bottomReserved = 108.0f;
        float catAreaHeight = Math.max(40.0f, height - 65.0f - bottomReserved);

        if (categoryScrollBar.mouseClicked(mouseX, mouseY, button, x + width - 6.0f, catAreaTop, 5.0f, catAreaHeight)) {
            return true;
        }

        if (isInside(mouseX, mouseY, x, catAreaTop, width - 6.0f, catAreaHeight)) {
            float scrollOffset = categoryScrollBar.getScrollOffset();
            float cy = catAreaTop - scrollOffset;
            for (ModuleCategory category : ModuleCategory.values()) {
                if (mouseY >= cy && mouseY <= cy + 19.0f) {
                    this.activeCategory = category;
                    setActiveTab(NavTab.CATEGORY);
                    return true;
                }
                cy += 21.0f;
            }
        }

        float bottomTop = y + height - bottomReserved + 4.0f;
        if (isInside(mouseX, mouseY, padX, bottomTop, itemW, 18.0f)) {
            setActiveTab(NavTab.SETTINGS);
            return true;
        }
        if (isInside(mouseX, mouseY, padX, bottomTop + 20.0f, itemW, 18.0f)) {
            setActiveTab(NavTab.THEME);
            return true;
        }
        if (isInside(mouseX, mouseY, padX, bottomTop + 40.0f, itemW, 18.0f)) {
            setActiveTab(NavTab.ABOUT);
            return true;
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, float height) {
        float catAreaHeight = Math.max(40.0f, height - 65.0f - 108.0f);
        return categoryScrollBar.mouseDragged(mouseX, mouseY, button, catAreaHeight);
    }

    public void mouseReleased() {
        categoryScrollBar.mouseReleased();
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount, float x, float y, float width, float height) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            categoryScrollBar.onMouseScroll(verticalAmount);
            return true;
        }
        return false;
    }

    public NavTab getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(NavTab tab) {
        this.activeTab = tab;
        if (onTabChanged != null) {
            onTabChanged.accept(tab);
        }
    }

    public ModuleCategory getActiveCategory() {
        return activeCategory;
    }

    private boolean isInside(double mx, double my, float x, float y, float w, float h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
