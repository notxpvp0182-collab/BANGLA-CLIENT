package com.banglaclient.module.impl.client;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 270: Hardware Detection
 * Detects and displays CPU cores/model, OpenGL GPU renderer, and allocated JVM RAM.
 */
public final class HardwareDetection extends HudModule {

    public HardwareDetection() {
        super(
                "hardware_detection",
                "Hardware Detection",
                "Detects available CPU, GPU, and RAM hardware telemetry.",
                ModuleCategory.CLIENT_SETTINGS,
                false,
                460.0f,
                268.0f,
                164.0f,
                40.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 164.0f;
        this.unscaledHeight = 40.0f;

        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();

        int col = hudStyle.getTextColor().getArgb();
        boolean sh = hudStyle.isTextShadow();

        String cpu = mc.textRenderer.trimToWidth("CPU: " + hw.getCpuModel(), 152);
        String gpu = mc.textRenderer.trimToWidth("GPU: " + hw.getGpuRenderer(), 152);
        String ram = "RAM: " + hw.getMaxMemoryMb() + " MB (" + hw.getAvailableProcessors() + " Cores)";

        context.drawText(mc.textRenderer, cpu, 6, 5, col, sh);
        context.drawText(mc.textRenderer, gpu, 6, 16, col, sh);
        context.drawText(mc.textRenderer, ram, 6, 27, col, sh);
    }
}
