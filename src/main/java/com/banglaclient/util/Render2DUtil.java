package com.banglaclient.util;

import net.minecraft.client.gui.DrawContext;

/**
 * High-performance 2D drawing primitives compatible with Minecraft 1.21.11 DrawContext.
 * Renders crisp rounded panels, outlines, corner brackets, gradients, and vector lines
 * with zero per-frame object allocations.
 */
public final class Render2DUtil {

    private Render2DUtil() {
    }

    public static void drawRect(DrawContext context, float x, float y, float width, float height, int color) {
        int x1 = Math.round(x);
        int y1 = Math.round(y);
        int x2 = Math.round(x + width);
        int y2 = Math.round(y + height);
        if (x2 > x1 && y2 > y1) {
            context.fill(x1, y1, x2, y2, color);
        }
    }

    public static void drawHorizontalGradient(DrawContext context, float x, float y, float width, float height, int startColor, int endColor) {
        int steps = Math.max(1, Math.min(24, Math.round(width / 4.0f)));
        float stepW = width / steps;
        for (int i = 0; i < steps; i++) {
            float t = (float) i / (float) Math.max(1, steps - 1);
            int c = interpolateArgb(startColor, endColor, t);
            drawRect(context, x + i * stepW, y, stepW + 0.6f, height, c);
        }
    }

    /**
     * Draws a clean chamfered/rounded rectangle matching the modern dark card style in Screenshot 1.
     */
    public static void drawRoundedRect(DrawContext context, float x, float y, float width, float height, float radius, int color) {
        int r = Math.max(0, Math.min(Math.round(radius), Math.round(Math.min(width, height) / 2.0f)));
        if (r <= 1) {
            drawRect(context, x, y, width, height, color);
            return;
        }
        int x1 = Math.round(x);
        int y1 = Math.round(y);
        int x2 = Math.round(x + width);
        int y2 = Math.round(y + height);

        // Center body
        context.fill(x1 + r, y1, x2 - r, y2, color);
        // Left & right vertical strips
        context.fill(x1, y1 + r, x1 + r, y2 - r, color);
        context.fill(x2 - r, y1 + r, x2, y2 - r, color);

        // Smooth stepped corners
        for (int i = 1; i < r; i++) {
            int inset = r - (int) Math.round(Math.sqrt(r * r - i * i));
            // Top-left & Top-right
            context.fill(x1 + inset, y1 + r - i, x1 + r, y1 + r - i + 1, color);
            context.fill(x2 - r, y1 + r - i, x2 - inset, y1 + r - i + 1, color);
            // Bottom-left & Bottom-right
            context.fill(x1 + inset, y2 - r + i - 1, x1 + r, y2 - r + i, color);
            context.fill(x2 - r, y2 - r + i - 1, x2 - inset, y2 - r + i, color);
        }
    }

    public static void drawRoundedOutline(DrawContext context, float x, float y, float width, float height, float radius, float thickness, int color) {
        int t = Math.max(1, Math.round(thickness));
        int x1 = Math.round(x);
        int y1 = Math.round(y);
        int x2 = Math.round(x + width);
        int y2 = Math.round(y + height);
        int cut = radius >= 2.0f ? 1 : 0;

        // Top & Bottom borders
        context.fill(x1 + cut, y1, x2 - cut, y1 + t, color);
        context.fill(x1 + cut, y2 - t, x2 - cut, y2, color);
        // Left & Right borders
        context.fill(x1, y1 + cut, x1 + t, y2 - cut, color);
        context.fill(x2 - t, y1 + cut, x2, y2 - cut, color);
    }

    /**
     * Draws the signature red top-left corner bracket seen on module cards in Screenshot 1.
     */
    public static void drawCornerAccentBracket(DrawContext context, float x, float y, float armLength, float thickness, int color) {
        int x1 = Math.round(x);
        int y1 = Math.round(y);
        int len = Math.round(armLength);
        int t = Math.max(1, Math.round(thickness));
        context.fill(x1, y1, x1 + len, y1 + t, color);
        context.fill(x1, y1, x1 + t, y1 + len, color);
    }

    public static void drawCircleOutline(DrawContext context, int centerX, int centerY, int radius, int color) {
        int x = radius;
        int y = 0;
        int err = 0;
        while (x >= y) {
            plotPixel(context, centerX + x, centerY + y, color);
            plotPixel(context, centerX + y, centerY + x, color);
            plotPixel(context, centerX - y, centerY + x, color);
            plotPixel(context, centerX - x, centerY + y, color);
            plotPixel(context, centerX - x, centerY - y, color);
            plotPixel(context, centerX - y, centerY - x, color);
            plotPixel(context, centerX + y, centerY - x, color);
            plotPixel(context, centerX + x, centerY - y, color);
            y += 1;
            if (err <= 0) {
                err += 2 * y + 1;
            }
            if (err > 0) {
                x -= 1;
                err -= 2 * x + 1;
            }
        }
    }

    public static void drawFilledCircle(DrawContext context, int centerX, int centerY, int radius, int color) {
        for (int dy = -radius; dy <= radius; dy++) {
            int dx = (int) Math.round(Math.sqrt(radius * radius - dy * dy));
            context.fill(centerX - dx, centerY + dy, centerX + dx + 1, centerY + dy + 1, color);
        }
    }

    public static void drawLine(DrawContext context, int x0, int y0, int x1, int y1, int color) {
        int dx = Math.abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;

        while (true) {
            plotPixel(context, x0, y0, color);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    public static void plotPixel(DrawContext context, int x, int y, int color) {
        context.fill(x, y, x + 1, y + 1, color);
    }

    private static int interpolateArgb(int c1, int c2, float t) {
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = Math.round(a1 + (a2 - a1) * t);
        int r = Math.round(r1 + (r2 - r1) * t);
        int g = Math.round(g1 + (g2 - g1) * t);
        int b = Math.round(b1 + (b2 - b1) * t);
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
