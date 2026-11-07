package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.StringSetting;
import com.banglaclient.theme.ColorData;

import java.util.Locale;

/**
 * Module 227: Keyword Highlight
 * Highlights chat lines containing configured keywords (e.g., "Bangla", "trade", "event", "koth").
 */
public final class KeywordHighlight extends AbstractModule {

    private final StringSetting keywordsCsv = registerSetting(new StringSetting(
            "keywords_csv", "Keywords (CSV)", "Comma-separated keywords to highlight in chat", "bangla, event, trade", 180
    ));
    private final ColorSetting lineHighlightColor = registerSetting(new ColorSetting(
            "keyword_line_color", "Line Highlight Color", "Background tint for matching chat lines", new ColorData(229, 37, 33, 65), true
    ));

    public KeywordHighlight() {
        super(
                "keyword_highlight",
                "Keyword Highlight",
                "Highlights configured keywords in chat messages.",
                ModuleCategory.CHAT,
                false
        );
    }

    public boolean containsKeyword(String text) {
        if (!isEnabled() || text == null || text.isEmpty()) {
            return false;
        }
        String lower = text.toLowerCase(Locale.ROOT);
        for (String part : keywordsCsv.getValue().split(",")) {
            String k = part.trim().toLowerCase(Locale.ROOT);
            if (!k.isEmpty() && lower.contains(k)) {
                return true;
            }
        }
        return false;
    }

    public ColorData getLineHighlightColor() {
        return lineHighlightColor.getValue();
    }
}
