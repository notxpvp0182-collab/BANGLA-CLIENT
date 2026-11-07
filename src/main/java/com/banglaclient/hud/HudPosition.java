package com.banglaclient.hud;

import com.google.gson.JsonObject;

/**
 * Continuous, free-floating HUD position storage.
 *
 * CRITICAL REQUIREMENT COMPLIANCE:
 * - NO corner snapping
 * - NO predefined 9-grid slots
 * - NO snap-to-edge or magnetic alignment
 * - Preserves exact floating-point coordinates (e.g., X = 347.35f, Y = 128.72f)
 * - Stores both exact pixel coordinates and resolution-normalized ratios so the user's
 *   exact continuous placement is preserved across window resizes.
 */
public final class HudPosition {

    private float exactX;
    private float exactY;
    private double normalizedX;
    private double normalizedY;
    private int referenceScreenWidth = 1920;
    private int referenceScreenHeight = 1080;

    public HudPosition(float initialX, float initialY) {
        setExactPosition(initialX, initialY, 1920, 1080);
    }

    /**
     * Sets the exact floating-point screen coordinates without any snapping.
     */
    public void setExactPosition(float x, float y, int screenWidth, int screenHeight) {
        this.exactX = x;
        this.exactY = y;
        this.referenceScreenWidth = Math.max(1, screenWidth);
        this.referenceScreenHeight = Math.max(1, screenHeight);
        this.normalizedX = (double) x / (double) this.referenceScreenWidth;
        this.normalizedY = (double) y / (double) this.referenceScreenHeight;
    }

    /**
     * Moves the HUD element continuously by exact floating-point mouse delta.
     */
    public void translate(float deltaX, float deltaY, int screenWidth, int screenHeight) {
        float currentX = getRenderX(screenWidth);
        float currentY = getRenderY(screenHeight);
        setExactPosition(currentX + deltaX, currentY + deltaY, screenWidth, screenHeight);
    }

    /**
     * Returns the exact continuous floating-point X coordinate for the current screen width.
     * If the screen width matches the reference width where the user placed it, returns exactX directly.
     */
    public float getRenderX(int currentScreenWidth) {
        if (currentScreenWidth <= 0 || currentScreenWidth == referenceScreenWidth) {
            return exactX;
        }
        return (float) (normalizedX * currentScreenWidth);
    }

    /**
     * Returns the exact continuous floating-point Y coordinate for the current screen height.
     * If the screen height matches the reference height where the user placed it, returns exactY directly.
     */
    public float getRenderY(int currentScreenHeight) {
        if (currentScreenHeight <= 0 || currentScreenHeight == referenceScreenHeight) {
            return exactY;
        }
        return (float) (normalizedY * currentScreenHeight);
    }

    public float getExactX() {
        return exactX;
    }

    public float getExactY() {
        return exactY;
    }

    public double getNormalizedX() {
        return normalizedX;
    }

    public double getNormalizedY() {
        return normalizedY;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("exactX", exactX);
        obj.addProperty("exactY", exactY);
        obj.addProperty("normalizedX", normalizedX);
        obj.addProperty("normalizedY", normalizedY);
        obj.addProperty("refWidth", referenceScreenWidth);
        obj.addProperty("refHeight", referenceScreenHeight);
        return obj;
    }

    public void fromJson(JsonObject obj) {
        if (obj == null) {
            return;
        }
        if (obj.has("exactX") && obj.has("exactY")) {
            this.exactX = obj.get("exactX").getAsFloat();
            this.exactY = obj.get("exactY").getAsFloat();
        }
        if (obj.has("normalizedX") && obj.has("normalizedY")) {
            this.normalizedX = obj.get("normalizedX").getAsDouble();
            this.normalizedY = obj.get("normalizedY").getAsDouble();
        }
        if (obj.has("refWidth") && obj.has("refHeight")) {
            this.referenceScreenWidth = Math.max(1, obj.get("refWidth").getAsInt());
            this.referenceScreenHeight = Math.max(1, obj.get("refHeight").getAsInt());
        }
    }
}
