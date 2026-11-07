package com.banglaclient.module.impl.network;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 242: Voice Chat
 * Detects compatible proximity voice chat mods (Simple Voice Chat `voicechat` or Plasmo Voice `plasmovoice`)
 * and displays microphone/mod readiness status on the HUD.
 */
public final class VoiceChatIntegration extends HudModule {

    private final boolean voiceModInstalled;
    private final String detectedVoiceModName;

    public VoiceChatIntegration() {
        super(
                "voice_chat_integration",
                "Voice Chat",
                "Provides voice communication status with compatible voice mods.",
                ModuleCategory.NETWORK_SERVER,
                false,
                460.0f,
                86.0f,
                138.0f,
                20.0f
        );
        boolean svc = FabricLoader.getInstance().isModLoaded("voicechat");
        boolean plasmo = FabricLoader.getInstance().isModLoaded("plasmovoice");
        this.voiceModInstalled = svc || plasmo;
        this.detectedVoiceModName = svc ? "SimpleVoiceChat" : (plasmo ? "PlasmoVoice" : "Not Installed");
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        String status = voiceModInstalled ? (detectedVoiceModName + " [Ready]") : "Voice Mod Absent";
        drawSimpleLabelValue(context, "Voice", status);
    }

    public boolean isVoiceModInstalled() {
        return voiceModInstalled;
    }
}
