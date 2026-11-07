package com.banglaclient.module;

/**
 * Complete enumeration of all 19 module categories in the exact sidebar order specified.
 */
public enum ModuleCategory {
    OPTIMIZER("optimizer", "Optimizer", "Core client rendering, culling, and entity optimizations"),
    PERFORMANCE_FPS("performance_fps", "Performance & FPS", "FPS counters, frame pacing, graphs, and limiters"),
    RAM_CPU_GPU("ram_cpu_gpu", "RAM / CPU / GPU", "Memory management, thread distribution, and caches"),
    KEYBINDS_INPUT_MOVEMENT("keybinds_input_movement", "Keybinds / Input / Movement", "Toggle sprint, mouse tweaks, and responsive input controls"),
    PVP_COMBAT("pvp_combat", "PvP / Combat", "CPS counters, reach display, combo tracking, and combat HUDs"),
    HUD_INFORMATION("hud_information", "HUD / Information", "Draggable HUD overlays, coordinates, armor, and network info"),
    CAMERA_VIEWMODEL("camera_viewmodel", "Camera / Viewmodel", "Freelook, FOV controls, viewmodel customization, and camera tweaks"),
    VISUAL_GRAPHICS("visual_graphics", "Visual / Graphics", "Fullbright, fog control, clear liquids, and custom crosshairs"),
    WORLD_VISUAL("world_visual", "World Visual", "Weather, clouds, liquid quality, and particle visibility"),
    INVENTORY_ITEMS("inventory_items", "Inventory / Items", "Shulker preview, durability info, and tooltip customizers"),
    SMALL_ITEM_SYSTEM("small_item_system", "Small Item System", "Individual held-item scale controls for weapons, shields, and totems"),
    CHAT("chat", "Chat", "Chat heads, timestamps, filters, search, and AutoGG"),
    NETWORK_SERVER("network_server", "Network / Server", "Ping tools, auto-reconnect, and server brand displays"),
    WORLD_MAP("world_map", "World / Map", "Waypoints, death locations, chunk borders, and slime chunk info"),
    SCOREBOARD_TABLIST_NAMETAG("scoreboard_tablist_nametag", "Scoreboard / Tablist / Nametag", "Custom tablist, scoreboard, and BANGLACLIENT nametags"),
    CLIENT_SETTINGS("client_settings", "Client / Settings", "Profiles, hardware detection, backups, and safe modes"),
    ADVANCED_PERFORMANCE("advanced_performance", "Advanced Performance", "Auto tuning, battery/thermal modes, and AFK optimizations"),
    CLIENT_QUALITY_FEATURES("client_quality_features", "Client Quality Features", "Custom Main Menu, Loading Screen, and resource reload optimizations"),
    BANGLACLIENT_CORE("banglaclient_core", "BANGLACLIENT Core", "Core HUD framework, theme engine, presets, and global managers");

    private final String id;
    private final String displayName;
    private final String description;

    ModuleCategory(String id, String displayName, String description) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
