package com.banglaclient.mixin;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.profile.ServerProfileMapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks ClientPlayNetworkHandler for legitimate observational telemetry:
 * - WorldTimeUpdateS2CPacket interval tracking for ServerTpsDisplay
 * - GameJoinS2CPacket trigger for ServerProfileMapper (e.g., play.banglaSMP.net)
 */
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onWorldTimeUpdate", at = @At("HEAD"))
    private void banglaclient$onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        PerformanceTracker.getInstance().onWorldTimeUpdatePacket();
        PerformanceTracker.getInstance().recordInboundPacket();
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void banglaclient$onServerGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        PerformanceTracker.getInstance().recordInboundPacket();
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.getCurrentServerEntry() != null) {
            ServerProfileMapper.getInstance().onServerJoined(mc.getCurrentServerEntry().address);
        }
    }
}
