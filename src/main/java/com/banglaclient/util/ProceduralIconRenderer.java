package com.banglaclient.util;

import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import net.minecraft.client.gui.DrawContext;

import java.util.Locale;

/**
 * Pure-code Procedural Vector/Canvas Icon Renderer for all 315 BANGLACLIENT modules.
 *
 * Renders crisp, custom geometric icons (matching Screenshot 1: White primary geometry +
 * Red secondary accents inside a rounded dark icon badge) directly via code without requiring
 * 315 external .png files.
 */
public final class ProceduralIconRenderer {

    public enum IconGlyph {
        MONITOR_GRAPH,      // FPS Display, Graphs, Profiler
        KEYBOARD_KEYS,      // Keystrokes, Keybinds
        CURSOR_CLICK,       // CPS Counter, Mouse clicks
        SHIELD_ARMOR,       // Armor HUD, Shield, Defense
        COMPASS_CROSSHAIR,  // Coordinates, Crosshair, Direction, Waypoints
        RULER_REACH,        // Reach Display, Distance
        HOURGLASS_TIMER,    // Effect Timer, Potion timers, Cooldowns
        MOUSE_BODY,         // Mouse Tweaks, Input
        RUNNER_SPRINT,      // ToggleSprint, AutoSprint, Movement
        SUN_BRIGHTNESS,     // Fullbright, Brightness, Sky, Weather
        WING_FLIGHT,        // Fly Speed, Elytra
        SPINNER_RING,       // Loading Indicator, Startup, Reload
        EYE_CAMERA,         // Freelook, Camera, Viewmodel, Culling
        GRID_INVENTORY,     // Inventory, Shulker, Containers, Items
        SWORD_COMBAT,       // PvP, Small Sword, Hit, Combo, Crystal
        CHIP_HARDWARE,      // RAM, CPU, GPU, Threads, Memory
        CHAT_BUBBLE,        // Chat modules
        SIGNAL_NETWORK,     // Ping, TPS, Network, Server
        CUBE_WORLD,         // Chunk, Block, World, Cave
        GEAR_SETTINGS       // Settings, Profiles, Core, Optimizer
    }

    private ProceduralIconRenderer() {
    }

    /**
     * Renders the complete module icon badge (dark rounded square + dual-tone white/red procedural glyph).
     */
    public static void drawModuleIcon(DrawContext context, Module module, int x, int y, int size) {
        ThemePalette palette = ThemeManager.getInstance().getPalette();
        int bg = 0xFF121216;
        int border = 0xFF282832;
        int white = palette.getIcon().getArgb();
        int red = palette.getAccent().getArgb();

        Render2DUtil.drawRoundedRect(context, x, y, size, size, 4.0f, bg);
        Render2DUtil.drawRoundedOutline(context, x, y, size, size, 4.0f, 1.0f, border);

        IconGlyph glyph = resolveGlyph(module);
        drawGlyph(context, glyph, x, y, size, white, red);
    }

    public static IconGlyph resolveGlyph(Module module) {
        if (module == null) {
            return IconGlyph.GEAR_SETTINGS;
        }
        String id = module.getId().toLowerCase(Locale.ROOT);

        if (id.contains("fps") || id.contains("graph") || id.contains("frame") || id.contains("dashboard") || id.contains("profiler")) {
            return IconGlyph.MONITOR_GRAPH;
        }
        if (id.contains("key") || id.contains("bind")) {
            return IconGlyph.KEYBOARD_KEYS;
        }
        if (id.contains("cps") || id.contains("click")) {
            return IconGlyph.CURSOR_CLICK;
        }
        if (id.contains("armor") || id.contains("shield") || id.contains("totem") || id.contains("protection")) {
            return IconGlyph.SHIELD_ARMOR;
        }
        if (id.contains("coord") || id.contains("crosshair") || id.contains("direction") || id.contains("waypoint") || id.contains("compass")) {
            return IconGlyph.COMPASS_CROSSHAIR;
        }
        if (id.contains("reach") || id.contains("distance") || id.contains("range")) {
            return IconGlyph.RULER_REACH;
        }
        if (id.contains("timer") || id.contains("effect") || id.contains("potion") || id.contains("cooldown") || id.contains("clock") || id.contains("time") || id.contains("day")) {
            return IconGlyph.HOURGLASS_TIMER;
        }
        if (id.contains("mouse")) {
            return IconGlyph.MOUSE_BODY;
        }
        if (id.contains("sprint") || id.contains("sneak") || id.contains("move") || id.contains("afk")) {
            return IconGlyph.RUNNER_SPRINT;
        }
        if (id.contains("bright") || id.contains("sun") || id.contains("sky") || id.contains("weather") || id.contains("cloud") || id.contains("light") || id.contains("moon") || id.contains("fog")) {
            return IconGlyph.SUN_BRIGHTNESS;
        }
        if (id.contains("fly") || id.contains("elytra") || id.contains("wind") || id.contains("feather")) {
            return IconGlyph.WING_FLIGHT;
        }
        if (id.contains("loading") || id.contains("reload") || id.contains("startup") || id.contains("fast_world") || id.contains("recovery")) {
            return IconGlyph.SPINNER_RING;
        }
        if (id.contains("freelook") || id.contains("camera") || id.contains("fov") || id.contains("culling") || id.contains("view") || id.contains("f5")) {
            return IconGlyph.EYE_CAMERA;
        }
        if (id.contains("inventory") || id.contains("shulker") || id.contains("container") || id.contains("item") || id.contains("hotbar") || id.contains("stack") || id.contains("tooltip") || id.contains("offhand")) {
            return IconGlyph.GRID_INVENTORY;
        }
        if (id.contains("sword") || id.contains("axe") || id.contains("pvp") || id.contains("hit") || id.contains("combo") || id.contains("weapon") || id.contains("attack") || id.contains("crystal") || id.contains("mace") || id.contains("bow")) {
            return IconGlyph.SWORD_COMBAT;
        }
        if (id.contains("ram") || id.contains("cpu") || id.contains("gpu") || id.contains("memory") || id.contains("thread") || id.contains("hardware") || id.contains("thermal") || id.contains("battery")) {
            return IconGlyph.CHIP_HARDWARE;
        }
        if (id.contains("chat") || id.contains("mention") || id.contains("gg") || id.contains("keyword")) {
            return IconGlyph.CHAT_BUBBLE;
        }
        if (id.contains("ping") || id.contains("network") || id.contains("server") || id.contains("packet") || id.contains("tps") || id.contains("reconnect") || id.contains("voice")) {
            return IconGlyph.SIGNAL_NETWORK;
        }
        if (id.contains("chunk") || id.contains("block") || id.contains("cave") || id.contains("water") || id.contains("lava") || id.contains("world") || id.contains("biome") || id.contains("slime")) {
            return IconGlyph.CUBE_WORLD;
        }

        // Category fallback
        ModuleCategory cat = module.getCategory();
        return switch (cat) {
            case OPTIMIZER, ADVANCED_PERFORMANCE -> IconGlyph.CUBE_WORLD;
            case PERFORMANCE_FPS -> IconGlyph.MONITOR_GRAPH;
            case RAM_CPU_GPU -> IconGlyph.CHIP_HARDWARE;
            case KEYBINDS_INPUT_MOVEMENT -> IconGlyph.KEYBOARD_KEYS;
            case PVP_COMBAT, SMALL_ITEM_SYSTEM -> IconGlyph.SWORD_COMBAT;
            case HUD_INFORMATION, SCOREBOARD_TABLIST_NAMETAG -> IconGlyph.MONITOR_GRAPH;
            case CAMERA_VIEWMODEL -> IconGlyph.EYE_CAMERA;
            case VISUAL_GRAPHICS, WORLD_VISUAL -> IconGlyph.SUN_BRIGHTNESS;
            case INVENTORY_ITEMS -> IconGlyph.GRID_INVENTORY;
            case CHAT -> IconGlyph.CHAT_BUBBLE;
            case NETWORK_SERVER -> IconGlyph.SIGNAL_NETWORK;
            case WORLD_MAP -> IconGlyph.COMPASS_CROSSHAIR;
            default -> IconGlyph.GEAR_SETTINGS;
        };
    }

    public static void drawGlyph(DrawContext context, IconGlyph glyph, int x, int y, int size, int white, int red) {
        int cx = x + size / 2;
        int cy = y + size / 2;

        switch (glyph) {
            case MONITOR_GRAPH -> {
                // Monitor screen (Screenshot 1 FPS Display style)
                Render2DUtil.drawRoundedOutline(context, cx - 8, cy - 7, 16, 11, 2.0f, 1.0f, white);
                context.fill(cx - 3, cy + 4, cx + 3, cy + 6, white);
                context.fill(cx - 5, cy + 6, cx + 5, cy + 7, white);
                // Red & white waveform line inside monitor
                Render2DUtil.drawLine(context, cx - 5, cy + 1, cx - 2, cy - 3, red);
                Render2DUtil.drawLine(context, cx - 2, cy - 3, cx + 1, cy, white);
                Render2DUtil.drawLine(context, cx + 1, cy, cx + 5, cy - 4, red);
            }
            case KEYBOARD_KEYS -> {
                // Keystrokes grid (Screenshot 1 Keystrokes style)
                Render2DUtil.drawRoundedOutline(context, cx - 8, cy - 5, 16, 11, 2.0f, 1.0f, white);
                context.fill(cx - 6, cy - 3, cx - 4, cy - 1, red);
                context.fill(cx - 2, cy - 3, cx, cy - 1, white);
                context.fill(cx + 2, cy - 3, cx + 4, cy - 1, white);
                context.fill(cx - 5, cy + 1, cx + 5, cy + 3, red);
            }
            case CURSOR_CLICK -> {
                // Mouse pointer arrow + red click sparks (Screenshot 1 CPS Counter style)
                Render2DUtil.drawLine(context, cx - 4, cy - 6, cx - 4, cy + 5, white);
                Render2DUtil.drawLine(context, cx - 4, cy - 6, cx + 4, cy + 1, white);
                Render2DUtil.drawLine(context, cx - 4, cy + 5, cx, cy + 2, white);
                Render2DUtil.drawLine(context, cx, cy + 2, cx + 3, cy + 7, white);
                // Red click rays
                context.fill(cx - 7, cy - 7, cx - 5, cy - 5, red);
                context.fill(cx - 3, cy - 9, cx - 2, cy - 7, red);
                context.fill(cx - 8, cy - 3, cx - 6, cy - 2, red);
            }
            case SHIELD_ARMOR -> {
                // Shield crest (Screenshot 1 Armor HUD style)
                Render2DUtil.drawLine(context, cx, cy - 7, cx - 6, cy - 4, white);
                Render2DUtil.drawLine(context, cx, cy - 7, cx + 6, cy - 4, white);
                Render2DUtil.drawLine(context, cx - 6, cy - 4, cx - 5, cy + 2, white);
                Render2DUtil.drawLine(context, cx + 6, cy - 4, cx + 5, cy + 2, white);
                Render2DUtil.drawLine(context, cx - 5, cy + 2, cx, cy + 7, white);
                Render2DUtil.drawLine(context, cx + 5, cy + 2, cx, cy + 7, white);
                // Inner red emblem
                context.fill(cx - 2, cy - 3, cx + 2, cy + 3, red);
            }
            case COMPASS_CROSSHAIR -> {
                // Target reticle (Screenshot 1 Coordinates / Crosshair style)
                Render2DUtil.drawCircleOutline(context, cx, cy, 5, white);
                context.fill(cx - 1, cy - 8, cx + 1, cy - 4, white);
                context.fill(cx - 1, cy + 4, cx + 1, cy + 8, white);
                context.fill(cx - 8, cy - 1, cx - 4, cy + 1, white);
                context.fill(cx + 4, cy - 1, cx + 8, cy + 1, white);
                context.fill(cx - 1, cy - 1, cx + 2, cy + 2, red);
            }
            case RULER_REACH -> {
                // Diagonal ruler (Screenshot 1 Reach Display style)
                Render2DUtil.drawLine(context, cx - 6, cy + 4, cx + 4, cy - 6, white);
                Render2DUtil.drawLine(context, cx - 3, cy + 7, cx + 7, cy - 3, white);
                Render2DUtil.drawLine(context, cx - 6, cy + 4, cx - 3, cy + 7, white);
                Render2DUtil.drawLine(context, cx + 4, cy - 6, cx + 7, cy - 3, white);
                context.fill(cx - 2, cy + 1, cx, cy + 3, red);
                context.fill(cx + 1, cy - 2, cx + 3, cy, red);
            }
            case HOURGLASS_TIMER -> {
                // Hourglass timer (Screenshot 1 Effect Timer style)
                context.fill(cx - 5, cy - 7, cx + 6, cy - 6, white);
                context.fill(cx - 5, cy + 6, cx + 6, cy + 7, white);
                Render2DUtil.drawLine(context, cx - 4, cy - 6, cx + 4, cy + 6, white);
                Render2DUtil.drawLine(context, cx + 4, cy - 6, cx - 4, cy + 6, white);
                context.fill(cx - 2, cy + 3, cx + 3, cy + 5, red);
                context.fill(cx, cy - 2, cx + 1, cy + 2, red);
            }
            case MOUSE_BODY -> {
                // Mouse body (Screenshot 1 Mouse Tweaks style)
                Render2DUtil.drawRoundedOutline(context, cx - 5, cy - 7, 10, 14, 3.0f, 1.0f, white);
                context.fill(cx - 5, cy - 1, cx + 5, cy, white);
                context.fill(cx - 1, cy - 5, cx + 1, cy - 2, red);
            }
            case RUNNER_SPRINT -> {
                // Sprinting figure with motion lines (Screenshot 1 ToggleSprint style)
                Render2DUtil.drawFilledCircle(context, cx + 2, cy - 5, 2, white);
                Render2DUtil.drawLine(context, cx + 1, cy - 2, cx - 2, cy + 2, white);
                Render2DUtil.drawLine(context, cx - 2, cy + 2, cx - 5, cy + 6, white);
                Render2DUtil.drawLine(context, cx - 1, cy + 1, cx + 4, cy + 5, white);
                Render2DUtil.drawLine(context, cx - 3, cy - 1, cx + 4, cy - 1, white);
                // Red speed trails
                context.fill(cx - 8, cy - 3, cx - 4, cy - 2, red);
                context.fill(cx - 9, cy + 1, cx - 5, cy + 2, red);
            }
            case SUN_BRIGHTNESS -> {
                // Sun with rays (Screenshot 1 Fullbright style)
                Render2DUtil.drawCircleOutline(context, cx, cy, 4, red);
                context.fill(cx - 1, cy - 1, cx + 2, cy + 2, white);
                context.fill(cx, cy - 8, cx + 1, cy - 5, red);
                context.fill(cx, cy + 6, cx + 1, cy + 9, red);
                context.fill(cx - 8, cy, cx - 5, cy + 1, red);
                context.fill(cx + 6, cy, cx + 9, cy + 1, red);
                Render2DUtil.plotPixel(context, cx - 5, cy - 5, red);
                Render2DUtil.plotPixel(context, cx + 5, cy - 5, red);
                Render2DUtil.plotPixel(context, cx - 5, cy + 5, red);
                Render2DUtil.plotPixel(context, cx + 5, cy + 5, red);
            }
            case WING_FLIGHT -> {
                // Wing / Feather icon (Screenshot 1 Fly Speed style)
                Render2DUtil.drawLine(context, cx - 6, cy + 5, cx + 6, cy - 6, white);
                Render2DUtil.drawLine(context, cx - 3, cy + 5, cx + 6, cy - 3, white);
                Render2DUtil.drawLine(context, cx - 5, cy + 2, cx + 3, cy - 6, red);
                context.fill(cx - 7, cy + 5, cx - 4, cy + 7, red);
            }
            case SPINNER_RING -> {
                // Segmented loading ring (Screenshot 1 Loading Indicator style)
                Render2DUtil.drawCircleOutline(context, cx, cy, 6, white);
                Render2DUtil.drawCircleOutline(context, cx, cy, 5, white);
                context.fill(cx + 2, cy - 7, cx + 7, cy - 1, red);
            }
            case EYE_CAMERA -> {
                // Camera / Eye lens (Screenshot 1 Freelook style)
                Render2DUtil.drawRoundedOutline(context, cx - 7, cy - 5, 14, 10, 3.0f, 1.0f, white);
                Render2DUtil.drawCircleOutline(context, cx, cy, 3, red);
                context.fill(cx - 1, cy - 1, cx + 1, cy + 1, white);
            }
            case GRID_INVENTORY -> {
                // Inventory slot grid (Screenshot 1 Inventory style)
                Render2DUtil.drawRoundedOutline(context, cx - 7, cy - 6, 14, 12, 2.0f, 1.0f, white);
                context.fill(cx - 7, cy - 2, cx + 7, cy - 1, white);
                context.fill(cx - 5, cy, cx - 2, cy + 3, red);
                context.fill(cx - 1, cy, cx + 2, cy + 3, white);
                context.fill(cx + 3, cy, cx + 5, cy + 3, white);
            }
            case SWORD_COMBAT -> {
                // Crossed combat blade
                Render2DUtil.drawLine(context, cx - 6, cy + 6, cx + 6, cy - 6, white);
                Render2DUtil.drawLine(context, cx - 5, cy + 6, cx + 6, cy - 5, white);
                Render2DUtil.drawLine(context, cx - 5, cy + 1, cx - 1, cy + 5, red);
                context.fill(cx - 7, cy + 5, cx - 5, cy + 7, red);
            }
            case CHIP_HARDWARE -> {
                // Processor die + pins
                Render2DUtil.drawRoundedOutline(context, cx - 5, cy - 5, 10, 10, 1.0f, 1.0f, white);
                context.fill(cx - 2, cy - 2, cx + 2, cy + 2, red);
                for (int i = -3; i <= 3; i += 3) {
                    context.fill(cx + i, cy - 8, cx + i + 1, cy - 5, white);
                    context.fill(cx + i, cy + 5, cx + i + 1, cy + 8, white);
                    context.fill(cx - 8, cy + i, cx - 5, cy + i + 1, white);
                    context.fill(cx + 5, cy + i, cx + 8, cy + i + 1, white);
                }
            }
            case CHAT_BUBBLE -> {
                Render2DUtil.drawRoundedOutline(context, cx - 7, cy - 6, 14, 10, 2.0f, 1.0f, white);
                Render2DUtil.drawLine(context, cx - 3, cy + 4, cx - 5, cy + 7, white);
                context.fill(cx - 4, cy - 3, cx + 4, cy - 2, red);
                context.fill(cx - 4, cy, cx + 2, cy + 1, white);
            }
            case SIGNAL_NETWORK -> {
                // Ascending signal bars
                context.fill(cx - 6, cy + 2, cx - 4, cy + 6, white);
                context.fill(cx - 2, cy - 1, cx, cy + 6, white);
                context.fill(cx + 2, cy - 4, cx + 4, cy + 6, red);
                context.fill(cx + 5, cy - 7, cx + 7, cy + 6, red);
            }
            case CUBE_WORLD -> {
                // Isometric 3D block outline
                Render2DUtil.drawRoundedOutline(context, cx - 6, cy - 6, 12, 12, 2.0f, 1.0f, white);
                context.fill(cx - 3, cy - 3, cx + 3, cy + 3, red);
            }
            case GEAR_SETTINGS -> {
                Render2DUtil.drawCircleOutline(context, cx, cy, 5, white);
                Render2DUtil.drawFilledCircle(context, cx, cy, 2, red);
                context.fill(cx - 1, cy - 7, cx + 1, cy - 5, white);
                context.fill(cx - 1, cy + 5, cx + 1, cy + 7, white);
                context.fill(cx - 7, cy - 1, cx - 5, cy + 1, white);
                context.fill(cx + 5, cy - 1, cx + 7, cy + 1, white);
            }
        }
    }
}
