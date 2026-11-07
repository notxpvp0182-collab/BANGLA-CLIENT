package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.Render2DEvent;
import com.banglaclient.hud.HudRenderer;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.camera.PotionCameraEffectOff;
import com.banglaclient.module.impl.overlay.ScoreboardCustomizer;
import com.banglaclient.module.impl.visual.CustomCrosshair;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks InGameHud for:
 * - Rendering all enabled free-positioned BANGLACLIENT HudModules
 * - Rendering CustomCrosshair when enabled
 * - Applying ScoreboardCustomizer transformations
 * - Suppressing nausea overlay when PotionCameraEffectOff is active
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void banglaclient$onRenderHudTail(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        float tickDelta = tickCounter.getTickProgress(false);
        HudRenderer.getInstance().renderInGameHud(context, tickDelta);
        ClientCore.getInstance().getEventBus().post(
                Render2DEvent.get(context, context.getScaledWindowWidth(), context.getScaledWindowHeight(), tickDelta)
        );
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
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

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"))
    private void banglaclient$preRenderScoreboard(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        ModuleRegistry.getInstance().getByClass(ScoreboardCustomizer.class).ifPresent(mod -> {
            if (mod.isEnabled()) {
                context.getMatrices().pushMatrix();
                context.getMatrices().translate(0.0f, mod.getVerticalOffset());
                float s = mod.getScoreboardScale();
                context.getMatrices().scale(s, s);
            }
        });
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("RETURN"))
    private void banglaclient$postRenderScoreboard(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        ModuleRegistry.getInstance().getByClass(ScoreboardCustomizer.class).ifPresent(mod -> {
            if (mod.isEnabled()) {
                context.getMatrices().popMatrix();
            }
        });
    }

    @Inject(method = "renderNauseaOverlay", at = @At("HEAD"), cancellable = true)
    private void banglaclient$cancelNauseaOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        ModuleRegistry.getInstance().getByClass(PotionCameraEffectOff.class).ifPresent(mod -> {
            if (mod.shouldCancelNauseaOverlay()) {
                ci.cancel();
            }
        });
    }
}
