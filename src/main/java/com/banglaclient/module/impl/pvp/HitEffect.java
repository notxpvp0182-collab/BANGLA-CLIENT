package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;

/**
 * Module 113: Hit Effect
 * Displays configurable client-side visual particle feedback when a hit is registered.
 */
public final class HitEffect extends AbstractModule {

    private final ModeSetting effectStyle = registerSetting(new ModeSetting(
            "hit_effect_style", "Effect Type", "Visual particle style spawned on target hit",
            "Crit Sparks", "Crit Sparks", "Enchanted Hit", "Heart Burst", "Crimson Flame"
    ));
    private final IntegerSetting particleCount = registerSetting(new IntegerSetting(
            "hit_particle_count", "Particle Count", "Number of visual particles spawned per hit", 5, 1, 15, 1, ""
    ));
    private final BooleanSetting playHitSound = registerSetting(new BooleanSetting(
            "play_hit_sound", "Crisp Hit Audio", "Ensure critical hit sound feedback is audible", false
    ));

    public HitEffect() {
        super(
                "hit_effect",
                "Hit Effect",
                "Displays configurable visual feedback when a hit is registered.",
                ModuleCategory.PVP_COMBAT,
                false
        );
    }

    public void triggerHitEffect(Entity target) {
        if (!isEnabled() || target == null || mc.particleManager == null || mc.world == null) {
            return;
        }
        int count = particleCount.getValue();
        double cx = target.getX();
        double cy = target.getBodyY(0.6);
        double cz = target.getZ();

        for (int i = 0; i < count; i++) {
            double vx = (mc.world.random.nextDouble() - 0.5) * 0.35;
            double vy = (mc.world.random.nextDouble() - 0.2) * 0.35;
            double vz = (mc.world.random.nextDouble() - 0.5) * 0.35;

            switch (effectStyle.getValue()) {
                case "Enchanted Hit" -> mc.particleManager.addParticle(ParticleTypes.ENCHANTED_HIT, cx, cy, cz, vx, vy, vz);
                case "Heart Burst" -> mc.particleManager.addParticle(ParticleTypes.HEART, cx, cy, cz, vx, vy, vz);
                case "Crimson Flame" -> mc.particleManager.addParticle(ParticleTypes.FLAME, cx, cy, cz, vx * 0.2, vy * 0.2, vz * 0.2);
                default -> mc.particleManager.addParticle(ParticleTypes.CRIT, cx, cy, cz, vx, vy, vz);
            }
        }
    }

    public boolean isPlayHitSound() {
        return isEnabled() && playHitSound.isEnabled();
    }
}
