package com.banglaclient.module.impl.worldmap;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.theme.ColorData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Module 243: Waypoints
 * Creates and manages saved locations across dimensions and servers, and renders active waypoints on the HUD.
 */
public final class Waypoints extends HudModule {

    public static final class WaypointEntry {
        private final String name;
        private final String group;
        private final String dimension;
        private final BlockPos pos;
        private final ColorData color;

        public WaypointEntry(String name, String group, String dimension, BlockPos pos, ColorData color) {
            this.name = name;
            this.group = group;
            this.dimension = dimension;
            this.pos = pos;
            this.color = color;
        }

        public String getName() { return name; }
        public String getGroup() { return group; }
        public String getDimension() { return dimension; }
        public BlockPos getPos() { return pos; }
        public ColorData getColor() { return color; }
    }

    private static final List<WaypointEntry> WAYPOINTS = new ArrayList<>();
    private int autoIndex = 1;

    public Waypoints() {
        super(
                "waypoints",
                "Waypoints",
                "Creates and displays saved locations.",
                ModuleCategory.WORLD_MAP,
                false,
                460.0f,
                114.0f,
                156.0f,
                46.0f
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_B));

        registerSetting(new ActionSetting(
                "add_waypoint_here",
                "Save Current Position",
                "Create a waypoint at your current block coordinates",
                "+ Add Waypoint Here",
                this::addWaypointAtPlayer
        ));
        registerSetting(new ActionSetting(
                "clear_waypoints",
                "Clear All Waypoints",
                "Remove all saved session waypoints",
                "Clear Waypoints",
                WAYPOINTS::clear
        ));
    }

    public void addWaypointAtPlayer() {
        if (mc.player == null || mc.world == null) return;
        String dim = mc.world.getRegistryKey().getValue().getPath();
        addWaypoint(new WaypointEntry(
                "Point #" + (autoIndex++),
                "General",
                dim,
                mc.player.getBlockPos(),
                ColorData.RED_ACCENT
        ));
    }

    public static void addWaypoint(WaypointEntry entry) {
        if (entry != null) {
            WAYPOINTS.add(entry);
        }
    }

    public static List<WaypointEntry> getWaypoints() {
        return Collections.unmodifiableList(WAYPOINTS);
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        if (WAYPOINTS.isEmpty()) {
            drawSimpleLabelValue(context, "Waypoints", isEditing ? "Base [120, 64, -340] (45m)" : "0 Saved");
            return;
        }
        String currentDim = (mc.world != null) ? mc.world.getRegistryKey().getValue().getPath() : "overworld";
        int row = 0;
        int maxW = 120;
        for (int i = Math.max(0, WAYPOINTS.size() - 4); i < WAYPOINTS.size(); i++) {
            WaypointEntry wp = WAYPOINTS.get(i);
            if (!wp.getDimension().equals(currentDim) && !isEditing) continue;
            double dist = (mc.player != null) ? Math.sqrt(mc.player.squaredDistanceTo(wp.getPos().getX(), wp.getPos().getY(), wp.getPos().getZ())) : 0.0;
            String line = String.format("%s [%d, %d, %d] (%.0fm)", wp.getName(), wp.getPos().getX(), wp.getPos().getY(), wp.getPos().getZ(), dist);
            int lw = mc.textRenderer.getWidth(line);
            if (lw > maxW) maxW = lw;
            context.drawText(mc.textRenderer, line, 6, 5 + row * 11, wp.getColor().getArgb(), hudStyle.isTextShadow());
            row++;
        }
        if (row == 0) {
            drawSimpleLabelValue(context, "Waypoints", "None in Dimension");
        } else {
            this.unscaledWidth = maxW + 14.0f;
            this.unscaledHeight = 8.0f + row * 11.0f;
        }
    }
}
