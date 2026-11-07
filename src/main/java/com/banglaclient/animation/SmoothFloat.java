package com.banglaclient.animation;

import com.banglaclient.core.ClientCore;

/**
 * Frame-rate independent exponential smoothing utility for scrollbars,
 * hover highlights, progress bars, and GUI opening/closing transitions.
 */
public final class SmoothFloat {

    private float current;
    private float target;
    private final float speed;
    private long lastUpdateNano;

    public SmoothFloat(float initialValue, float speed) {
        this.current = initialValue;
        this.target = initialValue;
        this.speed = Math.max(0.5f, speed);
        this.lastUpdateNano = System.nanoTime();
    }

    public SmoothFloat(float initialValue) {
        this(initialValue, 14.0f);
    }

    public float updateAndGet() {
        long now = System.nanoTime();
        float deltaSeconds = (now - lastUpdateNano) / 1_000_000_000.0f;
        lastUpdateNano = now;

        if (!ClientCore.getInstance().getClientState().isAnimationsEnabled()) {
            this.current = this.target;
            return this.current;
        }

        deltaSeconds = Math.min(0.1f, Math.max(0.0001f, deltaSeconds));
        float globalSpeedMult = ClientCore.getInstance().getClientState().getAnimationSpeed();
        float factor = 1.0f - (float) Math.exp(-speed * globalSpeedMult * deltaSeconds);
        this.current += (this.target - this.current) * factor;

        if (Math.abs(this.target - this.current) < 0.001f) {
            this.current = this.target;
        }
        return this.current;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public void setImmediate(float value) {
        this.current = value;
        this.target = value;
        this.lastUpdateNano = System.nanoTime();
    }

    public float getCurrent() {
        return current;
    }

    public float getTarget() {
        return target;
    }
}
