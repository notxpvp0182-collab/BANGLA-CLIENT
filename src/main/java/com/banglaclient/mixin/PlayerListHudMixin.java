package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.overlay.BcNametagIcon;
import com.banglaclient.module.impl.overlay.CustomTablist;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks PlayerListHud for CustomTablist scaling and BcNametagIcon tablist badge prefix.
 */
@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void banglaclient$preRenderTablist(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        ModuleRegistry.getInstance().getByClass(CustomTablist.class).ifPresent(tab -> {
            if (tab.isEnabled() && tab.getTablistScale() != 1.0f) {
                float s = tab.getTablistScale();
                context.getMatrices().pushMatrix();
                context.getMatrices().translate(scaledWindowWidth / 2.0f, 10.0f);
                context.getMatrices().scale(s, s);
                context.getMatrices().translate(-scaledWindowWidth / 2.0f, -10.0f);
            }
        });
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void banglaclient$postRenderTablist(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        ModuleRegistry.getInstance().getByClass(CustomTablist.class).ifPresent(tab -> {
            if (tab.isEnabled() && tab.getTablistScale() != 1.0f) {
                context.getMatrices().popMatrix();
            }
        });
    }

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void banglaclient$appendBcTablistBadge(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (ClientCore.getInstance().getClientState().isVanillaMode() || entry == null) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && entry.getProfile().id().equals(mc.player.getUuid())) {
            ModuleRegistry.getInstance().getByClass(BcNametagIcon.class).ifPresent(badge -> {
                if (badge.isShowInTablist()) {
                    cir.setReturnValue(Text.literal(badge.getFormattedPrefix()).append(cir.getReturnValue()));
                }
            });
        }
    }
}
