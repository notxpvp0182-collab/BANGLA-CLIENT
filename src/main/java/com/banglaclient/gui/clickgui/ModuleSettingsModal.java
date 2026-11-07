package com.banglaclient.gui.clickgui;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.gui.widget.ColorPickerWidget;
import com.banglaclient. gui.widget.SmoothScrollBar;
import com.banglaclient.keybind.InputDeviceType;
import com.banglaclient.keybind.KeyConflictDetector;
import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.Module;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.DoubleSetting;
import com.banglaclient.setting.EnumSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.setting.KeybindSetting;
import com.banglaclient.setting.ModeSetting;
import com.banglaclient.setting.Setting;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.ProceduralIconRenderer;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Right-click Module Settings Modal Dialog.
 * Supports Boolean toggles, Sliders (Int/Float/Double), Mode/Enum selectors,
 * Action buttons, Interactive RGB/HSB Color Pickers, and Keybind capture with
 * live conflict warnings.
 */
public final class ModuleSettingsModal {

    private final Module module;
    private final Runnable onClose;
    private final SmoothScrollBar scrollBar = new SmoothScrollBar();
    private final Map<ColorSetting, ColorPickerWidget> colorPickers = new HashMap<>();

    private KeybindSetting listeningKeybind = null;
    private Setting<?> draggingSliderSetting = null;

    public ModuleSettingsModal(Module module, Runnable onClose) {
        this.module = module;
        this.onClose = onClose;
        for (Setting<?> s : module.getSettings()) {
            if (s instanceof ColorSetting cs) {
                colorPickers.put(cs, new ColorPickerWidget(cs));
            }
        }
    }

    public void render(DrawContext context, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();

        // Dim background overlay
        context.fill(0, 0, screenWidth, screenHeight, 0xAA050508);

        float modalW = Math.min(440.0f, screenWidth - 40.0f);
        float modalH = Math.min(310.0f, screenHeight - 40.0f);
        float mx = (screenWidth - modalW) / 2.0f;
        float my = (screenHeight - modalH) / 2.0f;

        Render2DUtil.drawRoundedRect(context, mx, my, modalW, modalH, 7.0f, palette.getPanel().getArgb());
        Render2DUtil.drawRoundedOutline(context, mx, my, modalW, modalH, 7.0f, 1.5f, palette.getAccent().getArgb());

        // Header
        ProceduralIconRenderer.drawModuleIcon(context, module, Math.round(mx + 14.0f), Math.round(my + 10.0f), 24);
        if (mc.textRenderer != null) {
            context.drawText(mc.textRenderer, module.getName() + " — Settings", Math.round(mx + 46.0f), Math.round(my + 13.0f), palette.getPrimaryText().getArgb(), true);
            String desc = mc.textRenderer.trimToWidth(module.getDescription(), Math.round(modalW - 95.0f));
            context.drawText(mc.textRenderer, desc, Math.round(mx + 46.0f), Math.round(my + 24.0f), palette.getSecondaryText().getArgb(), false);
        }

        // Close [X] button
        float closeX = mx + modalW - 30.0f;
        float closeY = my + 10.0f;
        Render2DUtil.drawRoundedRect(context, closeX, closeY, 20.0f, 20.0f, 4.0f, 0xFF1E1E26);
        if (mc.textRenderer != null) {
            context.drawText(mc.textRenderer, "X", Math.round(closeX + 7.0f), Math.round(closeY + 6.0f), palette.getPrimaryText().getArgb(), false);
        }

        Render2DUtil.drawRect(context, mx + 10.0f, my + 42.0f, modalW - 20.0f, 1.0f, palette.getBorder().getArgb());

        // Settings Viewport
        float viewTop = my + 48.0f;
        float viewH = modalH - 56.0f;
        List<Setting<?>> settings = module.getSettings();

        float contentH = calculateContentHeight(settings);
        scrollBar.updateDimensions(viewH, contentH);
        float offset = scrollBar.getScrollOffset();

        context.enableScissor(Math.round(mx + 4), Math.round(viewTop), Math.round(mx + modalW - 4), Math.round(viewTop + viewH));
        float cy = viewTop - offset;

        if (settings.isEmpty() && mc.textRenderer != null) {
            String msg = "This module operates automatically with zero configuration overhead.";
            int mw = mc.textRenderer.getWidth(msg);
            context.drawText(mc.textRenderer, msg, Math.round(mx + (modalW - mw) / 2.0f), Math.round(viewTop + viewH / 2.0f - 6.0f), palette.getSecondaryText().getArgb(), false);
        }

        for (Setting<?> setting : settings) {
            if (!setting.isVisible()) continue;
            float rowH = (setting instanceof ColorSetting) ? 104.0f : 28.0f;

            if (cy + rowH >= viewTop && cy <= viewTop + viewH) {
                renderSettingRow(context, setting, mx + 14.0f, cy, modalW - 34.0f, rowH, mouseX, mouseY, palette);
            }
            cy += (rowH + 5.0f);
        }

        context.disableScissor();
        scrollBar.render(context, mx + modalW - 10.0f, viewTop, 4.0f, viewH, mouseX, mouseY);
    }

    private void renderSettingRow(DrawContext context, Setting<?> s, float x, float y, float w, float h, int mouseX, int mouseY, ThemePalette palette) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Render2DUtil.drawRoundedRect(context, x, y, w, h, 4.0f, palette.getCard().getArgb());
        Render2DUtil.drawRoundedOutline(context, x, y, w, h, 4.0f, 1.0f, palette.getBorder().getArgb());

        if (mc.textRenderer == null) return;
        context.drawText(mc.textRenderer, s.getName(), Math.round(x + 10.0f), Math.round(y + 10.0f), palette.getPrimaryText().getArgb(), false);

        if (s instanceof BooleanSetting bs) {
            float pillW = 46.0f;
            float pillX = x + w - pillW - 10.0f;
            float pillY = y + 5.0f;
            int col = bs.isEnabled() ? palette.getAccent().getArgb() : palette.getDisabled().getArgb();
            Render2DUtil.drawRoundedRect(context, pillX, pillY, pillW, 18.0f, 4.0f, col);
            String label = bs.isEnabled() ? "ON" : "OFF";
            context.drawText(mc.textRenderer, label, Math.round(pillX + (pillW - mc.textRenderer.getWidth(label)) / 2.0f), Math.round(pillY + 5.0f), palette.getPrimaryText().getArgb(), false);
        } else if (s instanceof IntegerSetting || s instanceof FloatSetting || s instanceof DoubleSetting) {
            float progress = getSliderProgress(s);
            String valLabel = formatSliderValue(s);
            float sliderW = 135.0f;
            float sliderX = x + w - sliderW - 10.0f;
            float sliderY = y + 15.0f;

            int vw = mc.textRenderer.getWidth(valLabel);
            context.drawText(mc.textRenderer, valLabel, Math.round(sliderX + sliderW - vw), Math.round(y + 5.0f), palette.getAccent().getArgb(), false);

            Render2DUtil.drawRoundedRect(context, sliderX, sliderY, sliderW, 5.0f, 2.0f, 0xFF101014);
            Render2DUtil.drawRoundedRect(context, sliderX, sliderY, Math.max(4.0f, sliderW * progress), 5.0f, 2.0f, palette.getAccent().getArgb());
        } else if (s instanceof ModeSetting ms) {
            String val = "< " + ms.getValue() + " >";
            int vw = mc.textRenderer.getWidth(val);
            context.drawText(mc.textRenderer, val, Math.round(x + w - vw - 10.0f), Math.round(y + 10.0f), palette.getAccent().getArgb(), false);
        } else if (s instanceof EnumSetting<?> es) {
            String val = "< " + es.getValue().name() + " >";
            int vw = mc.textRenderer.getWidth(val);
            context.drawText(mc.textRenderer, val, Math.round(x + w - vw - 10.0f), Math.round(y + 10.0f), palette.getAccent().getArgb(), false);
        } else if (s instanceof KeybindSetting ks) {
            boolean capturing = (listeningKeybind == ks);
            String keyStr = capturing ? "[Press a key...]" : ("Keybind: [" + ks.getValue().getDisplayName() + "]");
            List<Module> conflicts = KeyConflictDetector.getInstance().findConflictsFor(module);
            if (!conflicts.isEmpty() && !capturing) {
                keyStr = "⚠ Conflict (" + conflicts.size() + ") " + keyStr;
            }
            int kw = mc.textRenderer.getWidth(keyStr);
            int col = capturing ? palette.getAccent().getArgb() : (!conflicts.isEmpty() ? palette.getWarning().getArgb() : palette.getPrimaryText().getArgb());
            context.drawText(mc.textRenderer, keyStr, Math.round(x + w - kw - 10.0f), Math.round(y + 10.0f), col, false);
        } else if (s instanceof ActionSetting as) {
            float btnW = Math.max(85.0f, mc.textRenderer.getWidth(as.getButtonLabel()) + 16.0f);
            float btnX = x + w - btnW - 10.0f;
            Render2DUtil.drawRoundedRect(context, btnX, y + 5.0f, btnW, 18.0f, 4.0f, palette.getAccent().getArgb());
            int lw = mc.textRenderer.getWidth(as.getButtonLabel());
            context.drawText(mc.textRenderer, as.getButtonLabel(), Math.round(btnX + (btnW - lw) / 2.0f), Math.round(y + 10.0f), palette.getPrimaryText().getArgb(), false);
        } else if (s instanceof ColorSetting cs) {
            ColorPickerWidget picker = colorPickers.get(cs);
            if (picker != null) {
                picker.render(context, x + w - 180.0f, y + 6.0f, 170.0f, 92.0f);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, int screenWidth, int screenHeight) {
        if (listeningKeybind != null) {
            listeningKeybind.setValue(Keybind.ofMouse(button));
            listeningKeybind = null;
            ConfigManager.getInstance().saveConfig();
            return true;
        }

        float modalW = Math.min(440.0f, screenWidth - 40.0f);
        float modalH = Math.min(310.0f, screenHeight - 40.0f);
        float mx = (screenWidth - modalW) / 2.0f;
        float my = (screenHeight - modalH) / 2.0f;

        // Click outside modal closes it
        if (mouseX < mx || mouseX > mx + modalW || mouseY < my || mouseY > my + modalH) {
            onClose.run();
            return true;
        }

        // Close [X] button
        if (mouseX >= mx + modalW - 30.0f && mouseX <= mx + modalW - 10.0f && mouseY >= my + 10.0f && mouseY <= my + 30.0f) {
            onClose.run();
            return true;
        }

        float viewTop = my + 48.0f;
        float viewH = modalH - 56.0f;
        if (scrollBar.mouseClicked(mouseX, mouseY, button, mx + modalW - 12.0f, viewTop, 6.0f, viewH)) {
            return true;
        }

        float cy = viewTop - scrollBar.getScrollOffset();
        float rowX = mx + 14.0f;
        float rowW = modalW - 34.0f;

        for (Setting<?> s : module.getSettings()) {
            if (!s.isVisible()) continue;
            float rowH = (s instanceof ColorSetting) ? 104.0f : 28.0f;

            if (mouseY >= cy && mouseY <= cy + rowH && mouseX >= rowX && mouseX <= rowX + rowW) {
                if (s instanceof BooleanSetting bs && button == 0) {
                    bs.toggle();
                } else if ((s instanceof IntegerSetting || s instanceof FloatSetting || s instanceof DoubleSetting) && button == 0) {
                    draggingSliderSetting = s;
                    updateSliderFromMouse(s, mouseX, rowX + rowW - 145.0f, 135.0f);
                } else if (s instanceof ModeSetting ms) {
                    if (button == 0) ms.cycleNext();
                    else ms.cyclePrevious();
                } else if (s instanceof EnumSetting<?> es) {
                    if (button == 0) es.cycleNext();
                    else es.cyclePrevious();
                } else if (s instanceof KeybindSetting ks) {
                    if (button == 0) {
                        listeningKeybind = ks;
                    } else if (button == 1) {
                        ks.setValue(ks.getValue().withTriggerMode(
                                ks.getValue().getTriggerMode() == Keybind.TriggerMode.TOGGLE ? Keybind.TriggerMode.HOLD : Keybind.TriggerMode.TOGGLE
                        ));
                    }
                } else if (s instanceof ActionSetting as && button == 0) {
                    as.execute();
                } else if (s instanceof ColorSetting cs && button == 0) {
                    ColorPickerWidget picker = colorPickers.get(cs);
                    if (picker != null) {
                        picker.mouseInteraction(mouseX, mouseY, rowX + rowW - 180.0f, cy + 6.0f, 170.0f);
                    }
                }
                ConfigManager.getInstance().saveConfig();
                return true;
            }
            cy += (rowH + 5.0f);
        }
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, int screenWidth, int screenHeight) {
        float modalW = Math.min(440.0f, screenWidth - 40.0f);
        float modalH = Math.min(310.0f, screenHeight - 40.0f);
        float mx = (screenWidth - modalW) / 2.0f;
        float rowX = mx + 14.0f;
        float rowW = modalW - 34.0f;

        if (draggingSliderSetting != null && button == 0) {
            updateSliderFromMouse(draggingSliderSetting, mouseX, rowX + rowW - 145.0f, 135.0f);
            return true;
        }
        return scrollBar.mouseDragged(mouseX, mouseY, button, modalH - 56.0f);
    }

    public void mouseReleased() {
        if (draggingSliderSetting != null) {
            draggingSliderSetting = null;
            ConfigManager.getInstance().saveConfig();
        }
        scrollBar.mouseReleased();
    }

    public boolean mouseScrolled(double verticalAmount) {
        scrollBar.onMouseScroll(verticalAmount);
        return true;
    }

    public boolean keyPressed(int keyCode) {
        if (listeningKeybind != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                listeningKeybind.unbind();
            } else {
                listeningKeybind.setValue(new Keybind(InputDeviceType.KEYBOARD, keyCode, listeningKeybind.getValue().getTriggerMode()));
            }
            listeningKeybind = null;
            ConfigManager.getInstance().saveConfig();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onClose.run();
            return true;
        }
        return false;
    }

    private float calculateContentHeight(List<Setting<?>> settings) {
        float total = 4.0f;
        for (Setting<?> s : settings) {
            if (!s.isVisible()) continue;
            total += ((s instanceof ColorSetting) ? 104.0f : 28.0f) + 5.0f;
        }
        return total;
    }

    private float getSliderProgress(Setting<?> s) {
        if (s instanceof IntegerSetting is) return is.getNormalizedProgress();
        if (s instanceof FloatSetting fs) return fs.getNormalizedProgress();
        if (s instanceof DoubleSetting ds) {
            return (float) ((ds.getValue() - ds.getMin()) / Math.max(0.0001, ds.getMax() - ds.getMin()));
        }
        return 0.0f;
    }

    private String formatSliderValue(Setting<?> s) {
        if (s instanceof IntegerSetting is) return is.getValue() + is.getSuffix();
        if (s instanceof FloatSetting fs) return String.format("%.2f%s", fs.getValue(), fs.getSuffix());
        if (s instanceof DoubleSetting ds) return String.format("%.2f%s", ds.getValue(), ds.getSuffix());
        return "";
    }

    private void updateSliderFromMouse(Setting<?> s, double mouseX, float sliderX, float sliderW) {
        float ratio = (float) Math.max(0.0, Math.min(1.0, (mouseX - sliderX) / sliderW));
        if (s instanceof IntegerSetting is) is.setFromNormalizedProgress(ratio);
        else if (s instanceof FloatSetting fs) fs.setFromNormalizedProgress(ratio);
        else if (s instanceof DoubleSetting ds) {
            ds.setValue(ds.getMin() + ratio * (ds.getMax() - ds.getMin()));
        }
    }
}
