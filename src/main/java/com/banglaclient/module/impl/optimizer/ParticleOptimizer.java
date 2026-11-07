package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 008: Particle Optimizer
 * Controls particle workload, maximum active particle cap, and rendering distance cost.
 */
public final class ParticleOptimizer extends AbstractModule {

    private final IntegerSetting maxParticleDistance = registerSetting(new IntegerSetting(
            "max_particle_dist", "Max Particle Distance", "Ignore spawning particles further than this distance", 32, 8, 64, 4, "m"
    ));
    private final BooleanSetting reduceExplosionParticles = registerSetting(new BooleanSetting(
            "reduce_explosion_particles", "Reduce Explosion Burst", "Throttle dense explosion emitter bursts to prevent lag spikes", true
    ));
    private final BooleanSetting cullBehindCamera = registerSetting(new BooleanSetting(
            "cull_behind_camera", "Frustum Particle Check", "Skip spawning non-critical ambient particles behind the camera", true
    ));

    public ParticleOptimizer() {
        super(
                "particle_optimizer",
                "Particle Optimizer",
                "Controls particle workload, distance, and explosion spikes.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullParticleSpawn(double x, double y, double z) {
        if (!isEnabled() || mc.player == null) {
            return false;
        }
        double dx = x - mc.player.getX();
        double dy = y - mc.player.getY();
        double dz = z - mc.player.getZ();
        double max = maxParticleDistance.getValue();
        return (dx * dx + dy * dy + dz * dz) > (max * max);
    }

    public boolean isReduceExplosionParticles() {
        return isEnabled() && reduceExplosionParticles.isEnabled();
    }

    public boolean isCullBehindCamera() {
        return isEnabled() && cullBehindCamera.isEnabled();
    }
}
