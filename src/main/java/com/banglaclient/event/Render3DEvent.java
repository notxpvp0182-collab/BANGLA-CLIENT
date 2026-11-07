package com.banglaclient.event;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Fired during world rendering for 3D visual overlays such as Waypoint beacons,
 * Chunk borders, Attack Range Visualizer, and Projectile Trajectory Previews.
 */
public final class Render3DEvent extends Event {

    private static final Render3DEvent INSTANCE = new Render3DEvent();

    private MatrixStack matrices;
    private Camera camera;
    private float tickDelta;

    private Render3DEvent() {
    }

    public static Render3DEvent get(MatrixStack matrices, Camera camera, float tickDelta) {
        INSTANCE.setCancelled(false);
        INSTANCE.matrices = matrices;
        INSTANCE.camera = camera;
        INSTANCE.tickDelta = tickDelta;
        return INSTANCE;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public Camera getCamera() {
        return camera;
    }

    public float getTickDelta() {
        return tickDelta;
    }
}
