package com.banglaclient.gui.hudeditor;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.setting.ModeSetting;
import com.banglaclient.setting.Setting;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Dedicated Free-Positioning HUD Editor Screen.
 *
 * STRICT REQUIREMENT COMPLIANCE (Sections 17, 18, 19):
 * - Displays ONLY currently enabled HUD modules.
 * - ZERO snapping: No corner slots, no 9-position grid, no snap-to-edge, no magnetic lines.
 * - Left-click + drag moves any HUD element to ANY continuous floating-point coordinate
 *   on screen (e.g., X = 347.35f, Y = 128.72f).
 * - Bottom-right resize handle allows smooth continuous scaling.
 * - Right-click opens an inline settings popup for that specific HUD element.
 */
public final class HudEditorScreen extends Screen {

    private final Screen parentScreen;
    private HudModule selectedModule = null;
    private HudModule draggingModule = null;
    private HudModule resizingModule = null;
    private HudModule settingsPopupModule = null;
    private float popupX = 0.0f;
    private float popupY = 0.0f;

    private double lastMouseX = 0.0;
    private double lastMouseY = 0.0;

    public HudEditorScreen(Screen parentScreen) {
        super(Text.literal("BANGLACLIENT HUD Editor"));
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Subtle translucent backdrop so user can see exact game viewport while editing
        context.fill(0, 0, this.width, this.height, 0x55060608);

        ThemePalette palette = ThemeManager.getInstance().getPalette();
        List<HudModule> activeHuds = ModuleRegistry.getInstance().getEnabledHudModules();

        // Top instructional banner
        String header = "BANGLACLIENT HUD EDITOR — Free Continuous Placement (Left-Drag: Move | Corner-Drag: Scale | Right-Click: Settings | ESC: Save & Exit)";
        int hw = this.textRenderer.getWidth(header);
        Render2DUtil.drawRoundedRect(context, (this.width - hw) / 2.0f - 10.0f, 8.0f, hw + 20.0f, 20.0f, 4.0f, 0xCC101015);
        context.drawText(this.textRenderer, header, (this.width - hw) / 2, 14, palette.getPrimaryText().getArgb(), false);

        for (HudModule hud : activeHuds) {
            hud.renderHud(context, this.width, this.height, delta, true);

            float hx = hud.getHudPosition().getRenderX(this.width);
            float hy = hud.getHudPosition().getRenderY(this.height);
            float hwScaled = hud.getScaledWidth();
            float hhScaled = hud.getScaledHeight();

            boolean hovered = hud.isMouseOver(mouseX, mouseY, this.width, this.height);
            boolean isSelected = (hud == selectedModule || hud == draggingModule || hud == resizingModule);

            int borderCol = isSelected ? palette.getAccent().getArgb() : (hovered ? 0xCCFFFFFF : 0x669E9EA8);
            Render2DUtil.drawRoundedOutline(context, hx - 1.0f, hy - 1.0f, hwScaled + 2.0f, hhScaled + 2.0f, 2.0f, 1.0f, borderCol);

            // Resize handle at bottom-right corner
            Render2DUtil.drawRect(context, hx + hwScaled - 4.0f, hy + hhScaled - 4.0f, 5.0f, 5.0f, palette.getAccent().getArgb());

            if (isSelected || hovered) {
                String coordLabel = String.format("%s (X: %.2f, Y: %.2f, %.2fx)", hud.getName(), hx, hy, hud.getHudStyle().getScale());
                context.drawText(this.textRenderer, coordLabel, Math.round(hx), Math.max(2, Math.round(hy - 11.0f)), palette.getPrimaryText().getArgb(), true);
            }
        }

        if (settingsPopupModule != null && settingsPopupModule.isEnabled()) {
            renderContextPopup(context, mouseX, mouseY, palette);
        }
    }

    private void renderContextPopup(DrawContext context, int mouseX, int mouseY, ThemePalette palette) {
        List<Setting<?>> settings = settingsPopupModule.getSettings();
        float pw = 195.0f;
        float rowH = 18.0f;
        float ph = 26.0f + (settings.size() * rowH) + 6.0f;

        float px = Math.min(this.width - pw - 8.0f, Math.max(8.0f, popupX));
        float py = Math.min(this.height - ph - 8.0f, Math.max(8.0f, popupY));

        Render2DUtil.drawRoundedRect(context, px, py, pw, ph, 5.0f, 0xF414141A);
        Render2DUtil.drawRoundedOutline(context, px, py, pw, ph, 5.0f, 1.0f, palette.getAccent().getArgb());

        context.drawText(this.textRenderer, settingsPopupModule.getName() + " Settings", Math.round(px + 8.0f), Math.round(py + 7.0f), palette.getAccent().getArgb(), false);

        float cy = py + 24.0f;
        for (Setting<?> s : settings) {
            if (!s.isVisible()) continue;
            boolean rowHover = mouseX >= px + 4 && mouseX <= px + pw - 4 && mouseY >= cy && mouseY <= cy + rowH;
            if (rowHover) {
                Render2DUtil.drawRoundedRect(context, px + 4.0f, cy, pw - 8.0f, rowH - 1.0f, 3.0f, 0x442A2A35);
            }
            context.drawText(this.textRenderer, s.getName(), Math.round(px + 8.0f), Math.round(cy + 4.0f), palette.getPrimaryText().getArgb(), false);

            String valStr = formatSettingValue(s);
            int vw = this.textRenderer.getWidth(valStr);
            context.drawText(this.textRenderer, valStr, Math.round(px + pw - vw - 8.0f), Math.round(cy + 4.0f), palette.getSecondaryText().getArgb(), false);
            cy += rowH;
        }
    }

    private String formatSettingValue(Setting<?> s) {
        if (s instanceof BooleanSetting bs) {
            return bs.isEnabled() ? "ON" : "OFF";
        }
        if (s instanceof FloatSetting fs) {
            return String.format("%.2f%s", fs.getValue(), fs.getSuffix());
        }
        if (s instanceof IntegerSetting is) {
            return is.getValue() + is.getSuffix();
        }
        return String.valueOf(s.getValue());
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        if (settingsPopupModule != null) {
            if (handlePopupClick(mouseX, mouseY, button)) {
                return true;
            }
            settingsPopupModule = null;
        }

        List<HudModule> activeHuds = ModuleRegistry.getInstance().getEnabledHudModules();
        // Iterate top-most first
        for (int i = activeHuds.size() - 1; i >= 0; i--) {
            HudModule hud = activeHuds.get(i);
            float hx = hud.getHudPosition().getRenderX(this.width);
            float hy = hud.getHudPosition().getRenderY(this.height);
            float hw = hud.getScaledWidth();
            float hh = hud.getScaledHeight();

            // Check bottom-right resize handle first
            if (button == 0 && mouseX >= hx + hw - 7.0f && mouseX <= hx + hw + 3.0f
                    && mouseY >= hy + hh - 7.0f && mouseY <= hy + hh + 3.0f) {
                selectedModule = hud;
                resizingModule = hud;
                return true;
            }

            if (hud.isMouseOver(mouseX, mouseY, this.width, this.height)) {
                selectedModule = hud;
                if (button == 0) {
                    draggingModule = hud;
                    return true;
                } else if (button == 1) {
                    settingsPopupModule = hud;
                    popupX = (float) mouseX;
                    popupY = (float) mouseY;
                    return true;
                }
            }
        }

        selectedModule = null;
        return false;
    }

    private boolean handlePopupClick(double mouseX, double mouseY, int button) {
        List<Setting<?>> settings = settingsPopupModule.getSettings();
        float pw = 195.0f;
        float rowH = 18.0f;
        float ph = 26.0f + (settings.size() * rowH) + 6.0f;
        float px = Math.min(this.width - pw - 8.0f, Math.max(8.0f, popupX));
        float py = Math.min(this.height - ph - 8.0f, Math.max(8.0f, popupY));

        if (mouseX < px || mouseX > px + pw || mouseY < py || mouseY > py + ph) {
            return false;
        }

        float cy = py + 24.0f;
        for (Setting<?> s : settings) {
            if (!s.isVisible()) continue;
            if (mouseY >= cy && mouseY <= cy + rowH) {
                if (s instanceof BooleanSetting bs) {
                    bs.toggle();
                } else if (s instanceof FloatSetting fs) {
                    float delta = (button == 0 ? 1 : -1) * fs.getStep() * 2.0f;
                    fs.setValue(fs.getValue() + delta);
                } else if (s instanceof IntegerSetting is) {
                    int delta = (button == 0 ? 1 : -1) * is.getStep();
                    is.setValue(is.getValue() + delta);
                } else if (s instanceof ModeSetting ms) {
                    if (button == 0) ms.cycleNext();
                    else ms.cyclePrevious();
                }
                return true;
            }
            cy += rowH;
        }
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        float exactDx = (float) (mouseX - this.lastMouseX);
        float exactDy = (float) (mouseY - this.lastMouseY);
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        if (draggingModule != null && button == 0) {
            // Strictly continuous, non-snapping floating-point translation
            draggingModule.getHudPosition().translate(exactDx, exactDy, this.width, this.height);
            return true;
        }

        if (resizingModule != null && button == 0) {
            float currentScale = resizingModule.getHudStyle().getScale();
            resizingModule.getHudStyle().setScale(currentScale + (exactDx * 0.01f));
            return true;
        }

        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingModule != null || resizingModule != null) {
            draggingModule = null;
            resizingModule = null;
            ConfigManager.getInstance().saveConfig();
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        ConfigManager.getInstance().saveConfig();
        if (this.client != null) {
            this.client.setScreen(parentScreen);
        }
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
        
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        
        return super.charTyped(input);
    }
}
