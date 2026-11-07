package com.banglaclient.module.impl.pvp;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 106: CPS Graph
 * Displays rolling CPS history over time on a free-floating HUD waveform.
 */
public final class CpsGraph extends HudModule {

    private final ColorSetting lineColor = registerSetting(new ColorSetting(
            "cps_graph_color", "Waveform Color", "Color of the CPS waveform line", ColorData.RED_ACCENT, true
    ));

    private final int[] cpsSamples = new int[120];
    private int writeIndex = 0;

    public CpsGraph() {
        super(
                "cps_graph",
                "CPS Graph",
                "Displays real-time CPS history.",
                ModuleCategory.PVP_COMBAT,
                false,
                14.0f,
                486.0f,
                116.0f,
                42.0f
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() == ClientTickEvent.Phase.END) {
            cpsSamples[writeIndex] = PerformanceTracker.getInstance().getLeftCps();
            writeIndex = (writeIndex + 1) % cpsSamples.length;
        }
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 116.0f;
        this.unscaledHeight = 42.0f;

        if (mc.textRenderer != null) {
            context.drawText(mc.textRenderer, "CPS: " + PerformanceTracker.getInstance().getLeftCps(), 5, 4,
                    hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        }

        float gx = 4.0f;
        float gy = 14.0f;
        float gw = unscaledWidth - 8.0f;
        float gh = unscaledHeight - gy - 4.0f;
        float stepX = gw / (float) (cpsSamples.length - 1);
        int col = lineColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb();

        int prevX = Math.round(gx);
        int prevY = Math.round(gy + gh);
        for (int i = 0; i < cpsSamples.length; i++) {
            int idx = (writeIndex + i) % cpsSamples.length;
            float norm = Math.min(1.0f, cpsSamples[idx] / 20.0f);
            int px = Math.round(gx + i * stepX);
            int py = Math.round(gy + gh - norm * gh);
            if (i > 0) {
                Render2DUtil.drawLine(context, prevX, prevY, px, py, col);
            }
            prevX = px;
            prevY = py;
        }
    }
}
