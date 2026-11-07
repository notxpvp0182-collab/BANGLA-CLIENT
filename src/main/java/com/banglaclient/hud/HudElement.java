package com.banglaclient.hud;

import net.minecraft.client.gui.DrawContext;

/**
 * Contract for all free-floating, non-snapping HUD elements in BANGLACLIENT.
 */
public interface HudElement {

    /**
     * Renders the HUD element at its exact floating-point position during gameplay or HUD Editor preview.
     */
    void renderHud(DrawContext context, int screenWidth, int screenHeight, float tickDelta, boolean isEditing);

    HudPosition getHudPosition();

    HudStyle getHudStyle();

    float getUnscaledWidth();

    float getUnscaledHeight();

    default float getScaledWidth() {
        return getUnscaledWidth() * getHudStyle().getScale();
    }

    default float getScaledHeight() {
        return getUnscaledHeight() * getHudStyle().getScale();
    }

    default boolean isMouseOver(double mouseX, double mouseY, int screenWidth, int screenHeight) {
        float x = getHudPosition().getRenderX(screenWidth);
        float y = getHudPosition().getRenderY(screenHeight);
        float w = getScaledWidth();
        float h = getScaledHeight();
        return mouseX >= x && mouseX <= (x + w) && mouseY >= y && mouseY <= (y + h);
    }
}
