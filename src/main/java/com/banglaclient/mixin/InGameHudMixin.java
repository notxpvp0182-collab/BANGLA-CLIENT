package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.Render2DEvent;
import com.banglaclient.hud.HudRenderer;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.camera.PotionCameraEffectOff;
import com.banglaclient.module.impl.visual.CustomCrosshair;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"), require = 0)
    private void banglaclient$onRenderHudTail(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        float tickDelta = tickCounter.getTickProgress(false);
        HudRenderer.getInstance().renderInGameHud(context, tickDelta);
        ClientCore.getInstance().getEventBus().post(
                Render2DEvent.get(context, context.getScaledWindowWidth(), context.getScaledWindowHeight(), tickDelta)
        );
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        ModuleRegistry.getInstance().getByClass(CustomCrosshair.class).ifPresent(crosshair -> {
            if (crosshair.isEnabled()) {
                crosshair.renderCrosshair(context, context.getScaledWindowWidth(), context.getScaledWindowHeight());
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderNauseaOverlay", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$cancelNauseaOverlay(CallbackInfo ci) {
        ModuleRegistry.getInstance().getByClass(PotionCameraEffectOff.class).ifPresent(mod -> {
            if (mod.shouldCancelNauseaOverlay()) {
                ci.cancel();
            }
        });
    }
}
