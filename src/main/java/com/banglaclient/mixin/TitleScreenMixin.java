package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.gui.mainmenu.BanglaMainMenuScreen;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.quality.CustomMainMenuModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fallback safeguard on TitleScreen#init so that even if TitleScreen is instantiated directly,
 * it redirects cleanly to BanglaMainMenuScreen when CustomMainMenuModule is active.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void banglaclient$redirectToCustomMainMenu(CallbackInfo ci) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        boolean enabled = ModuleRegistry.getInstance()
                .getByClass(CustomMainMenuModule.class)
                .map(CustomMainMenuModule::isEnabled)
                .orElse(true);

        if (enabled) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc != null && !(mc.currentScreen instanceof BanglaMainMenuScreen)) {
                mc.setScreen(new BanglaMainMenuScreen());
                ci.cancel();
            }
        }
    }
}
