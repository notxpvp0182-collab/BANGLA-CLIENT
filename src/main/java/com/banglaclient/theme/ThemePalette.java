package com.banglaclient.theme;

import com.google.gson.JsonObject;

/**
 * Holds all 15 semantic colors for a BANGLACLIENT visual theme.
 * Default palette adheres strictly to the BLACK + WHITE + RED visual identity.
 */
public final class ThemePalette {

    private ColorData background;
    private ColorData panel;
    private ColorData card;
    private ColorData border;
    private ColorData accent;
    private ColorData hover;
    private ColorData enabled;
    private ColorData disabled;
    private ColorData primaryText;
    private ColorData secondaryText;
    private ColorData scrollbar;
    private ColorData icon;
    private ColorData graph;
    private ColorData warning;
    private ColorData success;

    public ThemePalette(
            ColorData background,
            ColorData panel,
            ColorData card,
            ColorData border,
            ColorData accent,
            ColorData hover,
            ColorData enabled,
            ColorData disabled,
            ColorData primaryText,
            ColorData secondaryText,
            ColorData scrollbar,
            ColorData icon,
            ColorData graph,
            ColorData warning,
            ColorData success
    ) {
        this.background = background;
        this.panel = panel;
        this.card = card;
        this.border = border;
        this.accent = accent;
        this.hover = hover;
        this.enabled = enabled;
        this.disabled = disabled;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.scrollbar = scrollbar;
        this.icon = icon;
        this.graph = graph;
        this.warning = warning;
        this.success = success;
    }

    public ThemePalette copy() {
        return new ThemePalette(
                background, panel, card, border, accent, hover,
                enabled, disabled, primaryText, secondaryText,
                scrollbar, icon, graph, warning, success
        );
    }

    public ColorData getBackground() { return background; }
    public void setBackground(ColorData background) { this.background = background; }

    public ColorData getPanel() { return panel; }
    public void setPanel(ColorData panel) { this.panel = panel; }

    public ColorData getCard() { return card; }
    public void setCard(ColorData card) { this.card = card; }

    public ColorData getBorder() { return border; }
    public void setBorder(ColorData border) { this.border = border; }

    public ColorData getAccent() { return accent; }
    public void setAccent(ColorData accent) { this.accent = accent; }

    public ColorData getHover() { return hover; }
    public void setHover(ColorData hover) { this.hover = hover; }

    public ColorData getEnabled() { return enabled; }
    public void setEnabled(ColorData enabled) { this.enabled = enabled; }

    public ColorData getDisabled() { return disabled; }
    public void setDisabled(ColorData disabled) { this.disabled = disabled; }

    public ColorData getPrimaryText() { return primaryText; }
    public void setPrimaryText(ColorData primaryText) { this.primaryText = primaryText; }

    public ColorData getSecondaryText() { return secondaryText; }
    public void setSecondaryText(ColorData secondaryText) { this.secondaryText = secondaryText; }

    public ColorData getScrollbar() { return scrollbar; }
    public void setScrollbar(ColorData scrollbar) { this.scrollbar = scrollbar; }

    public ColorData getIcon() { return icon; }
    public void setIcon(ColorData icon) { this.icon = icon; }

    public ColorData getGraph() { return graph; }
    public void setGraph(ColorData graph) { this.graph = graph; }

    public ColorData getWarning() { return warning; }
    public void setWarning(ColorData warning) { this.warning = warning; }

    public ColorData getSuccess() { return success; }
    public void setSuccess(ColorData success) { this.success = success; }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("background", background.toHex(true));
        obj.addProperty("panel", panel.toHex(true));
        obj.addProperty("card", card.toHex(true));
        obj.addProperty("border", border.toHex(true));
        obj.addProperty("accent", accent.toHex(true));
        obj.addProperty("hover", hover.toHex(true));
        obj.addProperty("enabled", enabled.toHex(true));
        obj.addProperty("disabled", disabled.toHex(true));
        obj.addProperty("primaryText", primaryText.toHex(true));
        obj.addProperty("secondaryText", secondaryText.toHex(true));
        obj.addProperty("scrollbar", scrollbar.toHex(true));
        obj.addProperty("icon", icon.toHex(true));
        obj.addProperty("graph", graph.toHex(true));
        obj.addProperty("warning", warning.toHex(true));
        obj.addProperty("success", success.toHex(true));
        return obj;
    }

    public void fromJson(JsonObject obj) {
        if (obj == null) return;
        if (obj.has("background")) background = ColorData.fromHex(obj.get("background").getAsString());
        if (obj.has("panel")) panel = ColorData.fromHex(obj.get("panel").getAsString());
        if (obj.has("card")) card = ColorData.fromHex(obj.get("card").getAsString());
        if (obj.has("border")) border = ColorData.fromHex(obj.get("border").getAsString());
        if (obj.has("accent")) accent = ColorData.fromHex(obj.get("accent").getAsString());
        if (obj.has("hover")) hover = ColorData.fromHex(obj.get("hover").getAsString());
        if (obj.has("enabled")) enabled = ColorData.fromHex(obj.get("enabled").getAsString());
        if (obj.has("disabled")) disabled = ColorData.fromHex(obj.get("disabled").getAsString());
        if (obj.has("primaryText")) primaryText = ColorData.fromHex(obj.get("primaryText").getAsString());
        if (obj.has("secondaryText")) secondaryText = ColorData.fromHex(obj.get("secondaryText").getAsString());
        if (obj.has("scrollbar")) scrollbar = ColorData.fromHex(obj.get("scrollbar").getAsString());
        if (obj.has("icon")) icon = ColorData.fromHex(obj.get("icon").getAsString());
        if (obj.has("graph")) graph = ColorData.fromHex(obj.get("graph").getAsString());
        if (obj.has("warning")) warning = ColorData.fromHex(obj.get("warning").getAsString());
        if (obj.has("success")) success = ColorData.fromHex(obj.get("success").getAsString());
    }
}
