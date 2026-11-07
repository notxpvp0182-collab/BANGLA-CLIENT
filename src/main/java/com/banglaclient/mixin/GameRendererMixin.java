package com.banglaclient.mixin;

import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.camera.CustomFov;
import com.banglaclient.module.impl.camera.DamageFovControl;
import com.banglaclient.module.impl.camera.DeathCameraEffectOff;
import com.banglaclient.module.impl.camera.NoCameraShake;
import com.banglaclient.module.impl.camera.NoViewBobbing;
import com.banglaclient.module.impl.pvp.HurtCameraControl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$controlHurtCameraTilt(CallbackInfo ci) {
        var noShake = ModuleRegistry.getInstance().getByClass(NoCameraShake.class);
        if (noShake.isPresent() && noShake.get().shouldCancelShake()) {
            ci.cancel();
            return;
        }

        if (this.client.player != null && this.client.player.isDead()) {
            var noDeathTilt = ModuleRegistry.getInstance().getByClass(DeathCameraEffectOff.class);
            if (noDeathTilt.isPresent() && noDeathTilt.get().shouldCancelDeathRoll()) {
                ci.cancel();
                return;
            }
        }

        var hurtCtrl = ModuleRegistry.getInstance().getByClass(HurtCameraControl.class);
        if (hurtCtrl.isPresent() && hurtCtrl.get().isEnabled()) {
            if (hurtCtrl.get().getHurtShakeMultiplier() <= 0.01f) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$controlWorldViewBobbing(CallbackInfo ci) {
        var noBob = ModuleRegistry.getInstance().getByClass(NoViewBobbing.class);
        if (noBob.isPresent() && noBob.get().shouldCancelWorldBobbing()) {
            ci.cancel();
        }
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true, require = 0)
    private void banglaclient$applyCustomFov(CallbackInfoReturnable<Float> cir) {
        var customFov = ModuleRegistry.getInstance().getByClass(CustomFov.class);
        if (customFov.isPresent() && customFov.get().isEnabled()) {
            if (customFov.get().isStaticFovLock()) {
                cir.setReturnValue(customFov.get().getBaseFov());
                return;
            }
        }

        var dmgFov = ModuleRegistry.getInstance().getByClass(DamageFovControl.class);
        if (dmgFov.isPresent() && dmgFov.get().shouldLockFovOnHurt() && customFov.isPresent() && customFov.get().isEnabled()) {
            cir.setReturnValue(customFov.get().getBaseFov());
        }
    }
}
