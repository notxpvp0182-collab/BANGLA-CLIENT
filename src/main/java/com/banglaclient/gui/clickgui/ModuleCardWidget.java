package com.banglaclient.gui.clickgui;

import com.banglaclient.animation.SmoothFloat;
import com.banglaclient.core.ClientCore;
import com.banglaclient.module.Module;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.ProceduralIconRenderer;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

/**
 * Renders an individual Module Card modeled after Screenshot 1:
 * - Dark rounded card panel with subtle border
 * - Signature red top-left corner accent bracket
 * - Procedural dual-tone white/red custom icon on the left
 * - Bold white module title
 * - Optional wrapped gray description (controlled by global "Show Module Descriptions" setting)
 * - Full-width bottom status button ("ENABLED" in glowing red-orange gradient, "DISABLED" in dark recessed gray)
 * - Left-click toggles module; Right-click opens module settings modal.
 */
public final class ModuleCardWidget {

    private final Module module;
    private final SmoothFloat hoverAnim = new SmoothFloat(0.0f, 16.0f);
    private final SmoothFloat toggleAnim;

    public ModuleCardWidget(Module module) {
        this.module = module;
        this.toggleAnim = new SmoothFloat(module.isEnabled() ? 1.0f : 0.0f, 14.0f);
    }

    public void render(DrawContext context, float x, float y, float width, float height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ThemePalette palette = ThemeManager.getInstance().getPalette();

        boolean hovered = isInside(mouseX, mouseY, x, y, width, height);
        hoverAnim.setTarget(hovered ? 1.0f : 0.0f);
        toggleAnim.setTarget(module.isEnabled() ? 1.0f : 0.0f);

        float h = hoverAnim.updateAndGet();
        float t = toggleAnim.updateAndGet();

        int cardBg = palette.getCard().getArgb();
        int borderCol = hovered ? palette.getAccent().withAlpha(180).getArgb() : palette.getBorder().getArgb();

        // Card container
        Render2DUtil.drawRoundedRect(context, x, y, width, height, 6.0f, cardBg);
        Render2DUtil.drawRoundedOutline(context, x, y, width, height, 6.0f, 1.0f, borderCol);

        // Top-left signature red corner bracket (Screenshot 1 visual detail)
        Render2DUtil.drawCornerAccentBracket(context, x + 2.0f, y + 2.0f, 12.0f, 1.5f, palette.getAccent().getArgb());

        // Custom Procedural Module Icon
        int iconSize = 26;
        int iconX = Math.round(x + 10.0f);
        int iconY = Math.round(y + 12.0f);
        ProceduralIconRenderer.drawModuleIcon(context, module, iconX, iconY, iconSize);

        // Module Name & Optional Description
        if (mc.textRenderer != null) {
            int textStartX = iconX + iconSize + 8;
            int maxTextW = Math.max(40, Math.round(width - (textStartX - x) - 8.0f));

            String trimmedName = mc.textRenderer.trimToWidth(module.getName(), maxTextW);
            context.drawText(mc.textRenderer, trimmedName, textStartX, iconY + 1, palette.getPrimaryText().getArgb(), true);

            if (ClientCore.getInstance().getClientState().isShowModuleDescriptions()) {
                List<OrderedText> lines = mc.textRenderer.wrapLines(Text.literal(module.getDescription()), maxTextW);
                int maxLines = Math.min(2, lines.size());
                for (int i = 0; i < maxLines; i++) {
                    context.drawText(mc.textRenderer, lines.get(i), textStartX, iconY + 12 + (i * 9), palette.getSecondaryText().getArgb(), false);
                }
            }
        }

        // Bottom Full-Width Status Pill Button ("ENABLED" / "DISABLED")
        float btnX = x + 8.0f;
        float btnW = width - 16.0f;
        float btnH = 20.0f;
        float btnY = y + height - btnH - 7.0f;

        if (t > 0.05f) {
            int startGrad = palette.getEnabled().withAlphaFactor(t).getArgb();
            int endGrad = palette.getAccent().withAlphaFactor(t).getArgb();
            Render2DUtil.drawRoundedRect(context, btnX, btnY, btnW, btnH, 4.0f, palette.getDisabled().getArgb());
            Render2DUtil.drawHorizontalGradient(context, btnX + 1.0f, btnY + 1.0f, btnW - 2.0f, btnH - 2.0f, startGrad, endGrad);
        } else {
            Render2DUtil.drawRoundedRect(context, btnX, btnY, btnW, btnH, 4.0f, palette.getDisabled().getArgb());
            Render2DUtil.drawRoundedOutline(context, btnX, btnY, btnW, btnH, 4.0f, 1.0f, palette.getBorder().getArgb());
        }

        if (mc.textRenderer != null) {
            String statusText = module.isEnabled() ? "ENABLED" : "DISABLED";
            int sw = mc.textRenderer.getWidth(statusText);
            int statusColor = module.isEnabled() ? palette.getPrimaryText().getArgb() : palette.getSecondaryText().getArgb();
            context.drawText(
                    mc.textRenderer,
                    statusText,
                    Math.round(btnX + (btnW - sw) / 2.0f),
                    Math.round(btnY + (btnH - mc.textRenderer.fontHeight) / 2.0f) + 1,
                    statusColor,
                    module.isEnabled()
            );
        }
    }

    public Module getModule() {
        return module;
    }

    public boolean isInside(double mx, double my, float x, float y, float w, float h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
