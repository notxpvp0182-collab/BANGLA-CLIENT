package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.loading.BanglaLoadingOverlay;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.quality.CustomLoadingScreenModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Hooks SplashOverlay to render the custom animated BANGLACLIENT Loading Screen
 * with smooth progress interpolation and a fade/scale transition at 100%.
 */
@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private ResourceReload reload;

    @Shadow
    @Final
    private Consumer<Optional<Throwable>> exceptionHandler;

    private boolean banglaclient$notifiedCompletion = false;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void banglaclient$renderCustomLoadingScreen(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        boolean useCustomLoading = ModuleRegistry.getInstance()
                .getByClass(CustomLoadingScreenModule.class)
                .map(CustomLoadingScreenModule::isEnabled)
                .orElse(true);

        if (!useCustomLoading) {
            return;
        }

        float rawProgress = this.reload.getProgress();
        boolean done = this.reload.isComplete();

        if (done && !banglaclient$notifiedCompletion) {
            banglaclient$notifiedCompletion = true;
            try {
                this.reload.throwException();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable t) {
                this.exceptionHandler.accept(Optional.of(t));
            }
        }

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        // Render underlying screen during the final fade-out so Main Menu smoothly appears
        if (done && this.client.currentScreen != null) {
            this.client.currentScreen.render(context, 0, 0, delta);
        }

        boolean keepOverlayActive = BanglaLoadingOverlay.getInstance().render(context, width, height, rawProgress, done);
        if (!keepOverlayActive && done) {
            this.client.setOverlay(null);
            BanglaLoadingOverlay.getInstance().reset();
            banglaclient$notifiedCompletion = false;
        }

        ci.cancel();
    }
}
