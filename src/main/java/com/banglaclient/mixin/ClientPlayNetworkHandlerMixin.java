package com.banglaclient.mixin;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.profile.ServerProfileMapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onWorldTimeUpdate", at = @At("HEAD"), require = 0)
    private void banglaclient$onWorldTimeUpdate(CallbackInfo ci) {
        PerformanceTracker.getInstance().onWorldTimeUpdatePacket();
        PerformanceTracker.getInstance().recordInboundPacket();
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"), require = 0)
    private void banglaclient$onServerGameJoin(CallbackInfo ci) {
        PerformanceTracker.getInstance().recordInboundPacket();
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.getCurrentServerEntry() != null) {
            ServerProfileMapper.getInstance().onServerJoined(mc.getCurrentServerEntry().address);
        }
    }
}
