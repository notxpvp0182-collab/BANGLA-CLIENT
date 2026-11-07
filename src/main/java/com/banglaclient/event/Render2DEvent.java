package com.banglaclient.event;

import net.minecraft.client.gui.DrawContext;

/**
 * Fired when rendering 2D HUD overlays on screen.
 * Reuses a single instance per frame to prevent per-frame heap allocations.
 */
public final class Render2DEvent extends Event {

    private static final Render2DEvent INSTANCE = new Render2DEvent();

    private DrawContext drawContext;
    private int scaledWidth;
    private int scaledHeight;
    private float tickDelta;

    private Render2DEvent() {
    }

    public static Render2DEvent get(DrawContext drawContext, int scaledWidth, int scaledHeight, float tickDelta) {
        INSTANCE.setCancelled(false);
        INSTANCE.drawContext = drawContext;
        INSTANCE.scaledWidth = scaledWidth;
        INSTANCE.scaledHeight = scaledHeight;
        INSTANCE.tickDelta = tickDelta;
        return INSTANCE;
    }

    public DrawContext getDrawContext() {
        return drawContext;
    }

    public int getScaledWidth() {
        return scaledWidth;
    }

    public int getScaledHeight() {
        return scaledHeight;
    }

    public float getTickDelta() {
        return tickDelta;
    }
}
