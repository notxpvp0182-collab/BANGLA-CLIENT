package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.StringSetting;

import java.util.Locale;

/**
 * Module 223: Chat Search
 * Searches and filters visible chat history by keyword.
 */
public final class ChatSearch extends AbstractModule {

    private final StringSetting searchQuery = registerSetting(new StringSetting(
            "chat_search_query", "Search Query", "Filter visible chat lines containing this text (empty = show all)", "", 128
    ));
    private final BooleanSetting caseSensitive = registerSetting(new BooleanSetting(
            "chat_search_case", "Case Sensitive", "Match exact uppercase/lowercase letters", false
    ));

    public ChatSearch() {
        super(
                "chat_search",
                "Chat Search",
                "Searches and filters chat history by keyword.",
                ModuleCategory.CHAT,
                false
        );
    }

    public boolean matchesSearch(String chatLine) {
        if (!isEnabled() || searchQuery.getValue().isBlank()) {
            return true;
        }
        if (chatLine == null) {
            return false;
        }
        String q = searchQuery.getValue().trim();
        if (caseSensitive.isEnabled()) {
            return chatLine.contains(q);
        }
        return chatLine.toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT));
    }
}
