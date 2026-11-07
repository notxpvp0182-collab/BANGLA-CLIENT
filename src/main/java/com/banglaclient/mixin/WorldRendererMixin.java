package com.banglaclient.mixin;

import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.optimizer.CloudRenderingOptimizer;
import com.banglaclient.module.impl.optimizer.SkyRenderingOptimizer;
import com.banglaclient.module.impl.worldvisual.WeatherControl;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks WorldRenderer for:
 * - WeatherControl (Cancelling precipitation pass when set to Always Clear)
 * - CloudRenderingOptimizer (Skipping cloud pass when deep underground)
 * - SkyRenderingOptimizer (Skipping sky dome pass in sealed caves)
 */
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void banglaclient$onRenderWeather(CallbackInfo ci) {
        var weatherCtrl = ModuleRegistry.getInstance().getByClass(WeatherControl.class);
        if (weatherCtrl.isPresent() && weatherCtrl.get().shouldCancelWeatherRender()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void banglaclient$onRenderClouds(CallbackInfo ci) {
        var cloudOpt = ModuleRegistry.getInstance().getByClass(CloudRenderingOptimizer.class);
        if (cloudOpt.isPresent() && cloudOpt.get().shouldSkipCloudPass()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void banglaclient$onRenderSky(CallbackInfo ci) {
        var skyOpt = ModuleRegistry.getInstance().getByClass(SkyRenderingOptimizer.class);
        if (skyOpt.isPresent() && skyOpt.get().shouldSkipSkyDome()) {
            ci.cancel();
        }
    }
}
