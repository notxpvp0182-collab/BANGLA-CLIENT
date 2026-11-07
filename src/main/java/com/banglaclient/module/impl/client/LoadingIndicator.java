package com.banglaclient.module.impl.client;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 280: Loading Indicator
 * Red action progress indicator with optional chunk/resource status (Screenshot 1 bottom-right module card).
 */
public final class LoadingIndicator extends HudModule {

    private final BooleanSetting onlyShowWhenBusy = registerSetting(new BooleanSetting(
            "only_show_when_busy", "Auto-Hide When Idle", "Only display the indicator during chunk loading or resource activity", false
    ));

    public LoadingIndicator() {
        super(
                "loading_indicator",
                "Loading Indicator",
                "Red action progress indicator with optional status text.",
                ModuleCategory.CLIENT_SETTINGS,
                true,
                460.0f,
                314.0f,
                104.0f,
                22.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        boolean busy = mc.getOverlay() != null || (mc.world != null && mc.world.getChunkManager().getLoadedChunkCount() == 0);
        if (onlyShowWhenBusy.isEnabled() && !busy && !isEditing) {
            this.unscaledWidth = 0.0f;
            this.unscaledHeight = 0.0f;
            return;
        }

        this.unscaledWidth = 104.0f;
        this.unscaledHeight = 22.0f;

        int red = ThemeManager.getInstance().getPalette().getAccent().getArgb();
        int white = hudStyle.getTextColor().getArgb();

        // Animated indicator ring
        Render2DUtil.drawCircleOutline(context, 12, 11, 5, white);
        int phase = (int) ((System.currentTimeMillis() / 120L) % 4L);
        int dx = (phase == 0 || phase == 1) ? 2 : -4;
        int dy = (phase == 1 || phase == 2) ? 2 : -4;
        context.fill(12 + dx, 11 + dy, 12 + dx + 3, 11 + dy + 3, red);

        String label = busy ? "Loading..." : "Ready";
        context.drawText(mc.textRenderer, label, 24, 7, white, hudStyle.isTextShadow());
    }
}
