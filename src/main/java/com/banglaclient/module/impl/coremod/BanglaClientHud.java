package com.banglaclient.module.impl.coremod;

import com.banglaclient.module. HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.util.ClientResourceManager;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 304: BANGLACLIENT HUD
 * Provides the unified HUD framework and renders the signature BANGLACLIENT watermark badge
 * (with FPS and server ping) at any continuous screen coordinate.
 */
public final class BanglaClientHud extends HudModule {

    private final BooleanSetting showBrandLogo = registerSetting(new BooleanSetting(
            "show_brand_logo", "Show Emblem Icon", "Render the BANGLACLIENT emblem inside the watermark badge", true
    ));
    private final BooleanSetting showKeystrokesOverlay = registerSetting(new BooleanSetting(
            "show_keystrokes", "Keystrokes Sub-Overlay", "Also render WASD + LMB/RMB Keystrokes below the badge", false
    ));

    public BanglaClientHud() {
        super(
                "banglaclient_hud",
                "Keystrokes & Brand HUD",
                "Shows BANGLACLIENT watermark and optional Keystrokes inputs.",
                ModuleCategory.BANGLACLIENT_CORE,
                false,
                14.0f,
                700.0f,
                138.0f,
                22.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;

        int offsetX = 6;
        if (showBrandLogo.isEnabled()) {
            ClientResourceManager.drawBrandLogo(context, 4, 3, 16);
            offsetX = 24;
        }
        String text = "BANGLACLIENT v1.0.0";
        int tw = mc.textRenderer.getWidth(text);
        this.unscaledWidth = offsetX + tw + 8.0f;
        this.unscaledHeight = showKeystrokesOverlay.isEnabled() ? 66.0f : 22.0f;

        context.drawText(mc.textRenderer, "BANGLA", offsetX, 7, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        int bw = mc.textRenderer.getWidth("BANGLA");
        context.drawText(mc.textRenderer, "CLIENT", offsetX + bw, 7, ThemeManager.getInstance().getPalette().getAccent().getArgb(), hudStyle.isTextShadow());

        if (showKeystrokesOverlay.isEnabled() && mc.options != null) {
            drawKeyBox(context, "W", 44, 24, 20, 16, mc.options.forwardKey.isPressed());
            drawKeyBox(context, "A", 22, 42, 20, 16, mc.options.leftKey.isPressed());
            drawKeyBox(context, "S", 44, 42, 20, 16, mc.options.backKey.isPressed());
            drawKeyBox(context, "D", 66, 42, 20, 16, mc.options.rightKey.isPressed());
        }
    }

    private void drawKeyBox(DrawContext context, String key, int x, int y, int w, int h, boolean pressed) {
        int bg = pressed ? ThemeManager.getInstance().getPalette().getAccent().getArgb() : 0x8814141A;
        context.fill(x, y, x + w, y + h, bg);
        int kw = mc.textRenderer.getWidth(key);
        context.drawText(mc.textRenderer, key, x + (w - kw) / 2, y + 4, 0xFFFFFFFF, false);
    }
}
