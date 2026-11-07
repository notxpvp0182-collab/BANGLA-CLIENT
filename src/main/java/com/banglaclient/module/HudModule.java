package com.banglaclient.module;

import com.banglaclient.hud.HudElement;
import com.banglaclient.hud.HudPosition;
import com.banglaclient.hud.HudStyle;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.SettingGroup;
import com.banglaclient.util.Render2DUtil;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawContext;

/**
 * Base class for all HUD-capable modules.
 * Combines standard module lifecycle/settings with continuous, free-floating, non-snapping
 * screen coordinates and customizable HUD styling.
 */
public abstract class HudModule extends AbstractModule implements HudElement {

    private final float defaultX;
    private final float defaultY;
    protected final HudPosition hudPosition;
    protected final HudStyle hudStyle;
    protected final SettingGroup hudAppearanceGroup;
    protected float unscaledWidth = 76.0f;
    protected float unscaledHeight = 22.0f;

    protected HudModule(
            String id,
            String name,
            String description,
            ModuleCategory category,
            boolean defaultEnabled,
            float defaultX,
            float defaultY,
            float defaultWidth,
            float defaultHeight
    ) {
        super(id, name, description, category, defaultEnabled);
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.unscaledWidth = defaultWidth;
        this.unscaledHeight = defaultHeight;
        this.hudPosition = new HudPosition(defaultX, defaultY);
        this.hudAppearanceGroup = createGroup("hud_appearance", "HUD Appearance");
        this.hudStyle = new HudStyle(this.hudAppearanceGroup);

        registerSetting(this.hudAppearanceGroup, new ActionSetting(
                "reset_hud_pos",
                "Reset Position",
                "Restore this HUD element to its default screen coordinates",
                "Reset Coordinates",
                () -> this.hudPosition.setExactPosition(this.defaultX, this.defaultY, 1920, 1080)
        ));
    }

    @Override
    public boolean isHudModule() {
        return true;
    }

    @Override
    public HudPosition getHudPosition() {
        return hudPosition;
    }

    @Override
    public HudStyle getHudStyle() {
        return hudStyle;
    }

    @Override
    public float getUnscaledWidth() {
        return unscaledWidth;
    }

    @Override
    public float getUnscaledHeight() {
        return unscaledHeight;
    }

    @Override
    public void renderHud(DrawContext context, int screenWidth, int screenHeight, float tickDelta, boolean isEditing) {
        float x = hudPosition.getRenderX(screenWidth);
        float y = hudPosition.getRenderY(screenHeight);
        float scale = hudStyle.getScale();

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale(scale, scale);

        if (hudStyle.isBackgroundEnabled()) {
            Render2DUtil.drawRoundedRect(
                    context,
                    0.0f,
                    0.0f,
                    unscaledWidth,
                    unscaledHeight,
                    hudStyle.getCornerRadius(),
                    hudStyle.getBackgroundColor().getArgb()
            );
        }

        if (hudStyle.isOutlineEnabled()) {
            Render2DUtil.drawRoundedOutline(
                    context,
                    0.0f,
                    0.0f,
                    unscaledWidth,
                    unscaledHeight,
                    hudStyle.getCornerRadius(),
                    hudStyle.getOutlineThickness(),
                    hudStyle.getOutlineColor().getArgb()
            );
        }

        renderHudContent(context, tickDelta, isEditing);
        context.getMatrices().popMatrix();
    }

    /**
     * Subclasses render their text, graphs, or icons in local unscaled coordinates (0, 0 to unscaledWidth, unscaledHeight).
     */
    protected abstract void renderHudContent(DrawContext context, float tickDelta, boolean isEditing);

    protected void drawSimpleLabelValue(DrawContext context, String label, String value) {
        if (mc.textRenderer == null) {
            return;
        }
        String fullText = (label == null || label.isEmpty()) ? value : (label + ": " + value);
        int textWidth = mc.textRenderer.getWidth(fullText);
        int pad = hudStyle.getPadding();
        this.unscaledWidth = Math.max(48.0f, textWidth + (pad * 2.0f) + 4.0f);
        this.unscaledHeight = Math.max(18.0f, mc.textRenderer.fontHeight + (pad * 2.0f));

        int textX = Math.round((unscaledWidth - textWidth) / 2.0f);
        int textY = Math.round((unscaledHeight - mc.textRenderer.fontHeight) / 2.0f) + 1;

        context.drawText(
                mc.textRenderer,
                fullText,
                textX,
                textY,
                hudStyle.getTextColor().getArgb(),
                hudStyle.isTextShadow()
        );
    }

    @Override
    public void resetToDefaults() {
        super.resetToDefaults();
        hudPosition.setExactPosition(defaultX, defaultY, 1920, 1080);
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject root = super.saveToJson();
        root.add("hudPosition", hudPosition.toJson());
        return root;
    }

    @Override
    public void loadFromJson(JsonObject json) {
        super.loadFromJson(json);
        if (json != null && json.has("hudPosition") && json.get("hudPosition").isJsonObject()) {
            hudPosition.fromJson(json.getAsJsonObject("hudPosition"));
        }
    }
}
