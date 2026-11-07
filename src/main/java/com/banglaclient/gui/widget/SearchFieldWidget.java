package com.banglaclient.gui.widget;

import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

/**
 * Custom dark/red search input box for the Click GUI TopBar ("Search modules...").
 */
public final class SearchFieldWidget {

    private String text = "";
    private final String placeholder;
    private boolean focused = false;

    public SearchFieldWidget(String placeholder) {
        this.placeholder = placeholder != null ? placeholder : "Search modules...";
    }

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();

        int bg = 0xFF121217;
        int border = focused ? palette.getAccent().getArgb() : palette.getBorder().getArgb();

        Render2DUtil.drawRoundedRect(context, x, y, width, height, 4.0f, bg);
        Render2DUtil.drawRoundedOutline(context, x, y, width, height, 4.0f, 1.0f, border);

        if (mc.textRenderer != null) {
            boolean empty = text.isEmpty();
            String display = empty ? placeholder : text;
            if (focused && (System.currentTimeMillis() / 500L) % 2L == 0L) {
                display = text + "|";
            }
            int color = empty && !focused ? palette.getSecondaryText().getArgb() : palette.getPrimaryText().getArgb();
            int textY = Math.round(y + (height - mc.textRenderer.fontHeight) / 2.0f) + 1;
            context.drawText(mc.textRenderer, display, Math.round(x + 10.0f), textY, color, false);
        }

        // Magnifying glass icon on the right side (matching Screenshot 1)
        int iconCx = Math.round(x + width - 14.0f);
        int iconCy = Math.round(y + height / 2.0f) - 1;
        Render2DUtil.drawCircleOutline(context, iconCx, iconCy, 4, palette.getSecondaryText().getArgb());
        Render2DUtil.drawLine(context, iconCx + 3, iconCy + 3, iconCx + 6, iconCy + 6, palette.getSecondaryText().getArgb());
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float x, float y, float width, float height) {
        boolean inside = mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
        if (inside && button == 1) {
            // Right-click clears search text quickly
            this.text = "";
            this.focused = true;
            return true;
        }
        this.focused = inside;
        return inside;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) {
            return false;
        }
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (!text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
            }
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) {
            focused = false;
            return true;
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!focused) {
            return false;
        }
        if (chr >= 32 && chr != 127 && text.length() < 48) {
            text += chr;
            return true;
        }
        return false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
