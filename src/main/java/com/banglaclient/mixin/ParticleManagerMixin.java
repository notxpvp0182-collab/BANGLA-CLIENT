package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.optimizer.ParticleOptimizer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks ParticleManager#addParticle to enforce distance culling from ParticleOptimizer.
 */
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Inject(
            method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void banglaclient$cullDistantParticles(
            ParticleEffect parameters,
            double x,
            double y,
            double z,
            double velocityX,
            double velocityY,
            double velocityZ,
            CallbackInfoReturnable<Particle> cir
    ) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        var partOpt = ModuleRegistry.getInstance().getByClass(ParticleOptimizer.class);
        if (partOpt.isPresent() && partOpt.get().shouldCullParticleSpawn(x, y, z)) {
            cir.setReturnValue(null);
        }
    }
}
