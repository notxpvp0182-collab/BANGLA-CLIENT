package com.banglaclient.gui.widget;

import com.banglaclient.animation.SmoothFloat;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Reusable smooth-interpolated scrollbar supporting BOTH:
 * 1. Mouse wheel scrolling
 * 2. Left-click scrollbar track & draggable thumb interaction
 */
public final class SmoothScrollBar {

    private final SmoothFloat smoothOffset = new SmoothFloat(0.0f, 16.0f);
    private float contentHeight = 0.0f;
    private float viewportHeight = 0.0f;
    private boolean dragging = false;
    private float dragStartMouseY = 0.0f;
    private float dragStartOffset = 0.0f;

    public void updateDimensions(float viewportHeight, float contentHeight) {
        this.viewportHeight = Math.max(1.0f, viewportHeight);
        this.contentHeight = Math.max(0.0f, contentHeight);
        clampTarget();
    }

    public void onMouseScroll(double verticalAmount) {
        float maxScroll = getMaxScroll();
        if (maxScroll <= 0.0f) {
            smoothOffset.setTarget(0.0f);
            return;
        }
        float next = smoothOffset.getTarget() - (float) (verticalAmount * 34.0);
        smoothOffset.setTarget(Math.max(0.0f, Math.min(maxScroll, next)));
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, float trackX, float trackY, float trackWidth, float trackHeight) {
        if (button != 0 || getMaxScroll() <= 0.0f) {
            return false;
        }
        if (mouseX >= trackX && mouseX <= (trackX + trackWidth) && mouseY >= trackY && mouseY <= (trackY + trackHeight)) {
            float thumbHeight = getThumbHeight(trackHeight);
            float thumbY = getThumbY(trackY, trackHeight, thumbHeight);

            if (mouseY >= thumbY && mouseY <= (thumbY + thumbHeight)) {
                dragging = true;
                dragStartMouseY = (float) mouseY;
                dragStartOffset = smoothOffset.getTarget();
            } else {
                // Click on track jumps proportionally
                float clickRatio = (float) ((mouseY - trackY - thumbHeight / 2.0f) / Math.max(1.0f, trackHeight - thumbHeight));
                smoothOffset.setTarget(Math.max(0.0f, Math.min(getMaxScroll(), clickRatio * getMaxScroll())));
                dragging = true;
                dragStartMouseY = (float) mouseY;
                dragStartOffset = smoothOffset.getTarget();
            }
            return true;
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, float trackHeight) {
        if (!dragging || button != 0) {
            return false;
        }
        float maxScroll = getMaxScroll();
        if (maxScroll <= 0.0f) {
            return false;
        }
        float thumbHeight = getThumbHeight(trackHeight);
        float scrollableTrack = Math.max(1.0f, trackHeight - thumbHeight);
        float deltaY = (float) mouseY - dragStartMouseY;
        float scrollDelta = (deltaY / scrollableTrack) * maxScroll;

        float next = Math.max(0.0f, Math.min(maxScroll, dragStartOffset + scrollDelta));
        smoothOffset.setImmediate(next);
        return true;
    }

    public void mouseReleased() {
        dragging = false;
    }

    public void render(DrawContext context, float trackX, float trackY, float trackWidth, float trackHeight, double mouseX, double mouseY) {
        if (getMaxScroll() <= 0.0f) {
            return;
        }
        ThemePalette palette = ThemeManager.getInstance().getPalette();
        Render2DUtil.drawRoundedRect(context, trackX, trackY, trackWidth, trackHeight, trackWidth / 2.0f, 0x66101014);

        float thumbHeight = getThumbHeight(trackHeight);
        float thumbY = getThumbY(trackY, trackHeight, thumbHeight);
        boolean hovered = dragging || (mouseX >= trackX && mouseX <= trackX + trackWidth && mouseY >= thumbY && mouseY <= thumbY + thumbHeight);

        int thumbColor = hovered ? palette.getAccent().getArgb() : palette.getScrollbar().getArgb();
        Render2DUtil.drawRoundedRect(context, trackX, thumbY, trackWidth, thumbHeight, trackWidth / 2.0f, thumbColor);
    }

    public float getScrollOffset() {
        clampTarget();
        return smoothOffset.updateAndGet();
    }

    public void reset() {
        smoothOffset.setImmediate(0.0f);
        dragging = false;
    }

    public float getMaxScroll() {
        return Math.max(0.0f, contentHeight - viewportHeight);
    }

    private void clampTarget() {
        float max = getMaxScroll();
        if (smoothOffset.getTarget() > max) {
            smoothOffset.setTarget(max);
        }
        if (smoothOffset.getTarget() < 0.0f) {
            smoothOffset.setTarget(0.0f);
        }
    }

    private float getThumbHeight(float trackHeight) {
        if (contentHeight <= 0.0f) {
            return trackHeight;
        }
        return Math.max(24.0f, Math.min(trackHeight, (viewportHeight / contentHeight) * trackHeight));
    }

    private float getThumbY(float trackY, float trackHeight, float thumbHeight) {
        float maxScroll = getMaxScroll();
        if (maxScroll <= 0.0f) {
            return trackY;
        }
        float ratio = smoothOffset.getCurrent() / maxScroll;
        return trackY + ratio * (trackHeight - thumbHeight);
    }
}
