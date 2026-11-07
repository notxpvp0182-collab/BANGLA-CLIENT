package com.banglaclient.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Logical grouping of settings inside a module's configuration panel
 * (e.g., "General", "Appearance", "Performance", "Background & Border").
 */
public final class SettingGroup {

    private final String id;
    private final String title;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean expanded = true;

    public SettingGroup(String id, String title) {
        this.id = Objects.requireNonNull(id, "Group ID cannot be null");
        this.title = Objects.requireNonNull(title, "Group title cannot be null");
    }

    public <S extends Setting<?>> S add(S setting) {
        if (setting != null && !settings.contains(setting)) {
            settings.add(setting);
        }
        return setting;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
