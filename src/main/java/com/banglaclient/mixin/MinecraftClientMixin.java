package com.banglaclient.mixin;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.gui.mainmenu.BanglaMainMenuScreen;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.advanced.AfkOptimization;
import com.banglaclient.module.impl.advanced.BackgroundMode;
import com.banglaclient.module.impl.input.BreakDelayControl;
import com.banglaclient.module.impl.input.FastBlockPlacement;
import com.banglaclient.module.impl.input.FastXpBottle;
import com.banglaclient.module.impl.input.LeftClickDelayControl;
import com.banglaclient.module.impl.performance.MenuFpsLimiter;
import com.banglaclient.module.impl.performance.UnfocusedFpsLimiter;
import com.banglaclient.module.impl.performance.WorldFpsLimiter;
import com.banglaclient.module.impl.quality.CustomMainMenuModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks MinecraftClient for:
 * - Frame telemetry tracking and ClientTickEvent dispatch
 * - Custom Main Menu interception (TitleScreen -> BanglaMainMenuScreen)
 * - Unfocused, AFK, Menu, and World FPS limiters
 * - Legitimate LeftClickDelayControl, FastXpBottle, FastBlockPlacement, and BreakDelayControl
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    public int attackCooldown;

    @Shadow
    private int itemUseCooldown;

    @Inject(method = "render", at = @At("HEAD"))
    private void banglaclient$onRenderFrameStart(boolean tick, CallbackInfo ci) {
        PerformanceTracker.getInstance().onFrameRendered();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void banglaclient$onTickStart(CallbackInfo ci) {
        MinecraftClient self = (MinecraftClient) (Object) this;
        ClientCore.getInstance().getEventBus().post(ClientTickEvent.getStart(self));

        if (!ClientCore.getInstance().getClientState().isVanillaMode()) {
            // LeftClickDelayControl: Clear air-miss cooldown penalty if enabled
            ModuleRegistry.getInstance().getByClass(LeftClickDelayControl.class).ifPresent(mod -> {
                if (mod.shouldClearMissCooldown() && this.attackCooldown > 0) {
                    this.attackCooldown = 0;
                }
            });

            // FastXpBottle & FastBlockPlacement right-click cooldown control
            ModuleRegistry.getInstance().getByClass(FastXpBottle.class).ifPresent(mod -> {
                if (mod.isHoldingXpBottle() && this.itemUseCooldown > mod.getTargetUseCooldown()) {
                    this.itemUseCooldown = mod.getTargetUseCooldown();
                }
            });

            ModuleRegistry.getInstance().getByClass(FastBlockPlacement.class).ifPresent(mod -> {
                if (mod.shouldApplyFastPlace() && this.itemUseCooldown > mod.getTargetPlaceCooldown()) {
                    this.itemUseCooldown = mod.getTargetPlaceCooldown();
                }
            });
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void banglaclient$onTickEnd(CallbackInfo ci) {
        MinecraftClient self = (MinecraftClient) (Object) this;
        PerformanceTracker.getInstance().onClientTick();
        ClientCore.getInstance().getEventBus().post(ClientTickEvent.getEnd(self));
        ConfigManager.getInstance().tickAutoSave();
    }

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    private Screen banglaclient$replaceTitleScreen(Screen incoming) {
        if (incoming instanceof TitleScreen && !ClientCore.getInstance().getClientState().isVanillaMode()) {
            boolean useCustomMenu = ModuleRegistry.getInstance()
                    .getByClass(CustomMainMenuModule.class)
                    .map(CustomMainMenuModule::isEnabled)
                    .orElse(true);
            if (useCustomMenu) {
                return new BanglaMainMenuScreen();
            }
        }
        return incoming;
    }

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    private void banglaclient$overrideFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        MinecraftClient self = (MinecraftClient) (Object) this;

        // 1. Unfocused / Background FPS Limiters
        if (!self.isWindowFocused()) {
            var bgMod = ModuleRegistry.getInstance().getByClass(BackgroundMode.class);
            if (bgMod.isPresent() && bgMod.get().isEnabled()) {
                cir.setReturnValue(bgMod.get().getBackgroundFps());
                return;
            }
            var unfocusMod = ModuleRegistry.getInstance().getByClass(UnfocusedFpsLimiter.class);
            if (unfocusMod.isPresent() && unfocusMod.get().isEnabled()) {
                cir.setReturnValue(unfocusMod.get().getUnfocusedFpsCap());
                return;
            }
        }

        // 2. Menu FPS Limiter when not in a world
        if (self.world == null) {
            var menuMod = ModuleRegistry.getInstance().getByClass(MenuFpsLimiter.class);
            if (menuMod.isPresent() && menuMod.get().isEnabled()) {
                cir.setReturnValue(menuMod.get().getMenuFpsCap());
                return;
            }
        }

        // 3. AFK Optimization Limiter
        var afkMod = ModuleRegistry.getInstance().getByClass(AfkOptimization.class);
        if (afkMod.isPresent() && afkMod.get().isCurrentlyAfk()) {
            cir.setReturnValue(afkMod.get().getAfkFpsLimit());
            return;
        }

        // 4. World FPS Limiter
        if (self.world != null) {
            var worldMod = ModuleRegistry.getInstance().getByClass(WorldFpsLimiter.class);
            if (worldMod.isPresent() && worldMod.get().isEnabled()) {
                int cap = worldMod.get().getGameplayFpsCap();
                if (cap < 260) {
                    cir.setReturnValue(cap);
                }
            }
        }
    }
}
