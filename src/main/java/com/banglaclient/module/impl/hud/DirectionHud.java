package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

/**
 * Module 128: Direction HUD
 * Displays cardinal facing direction (North/South/East/West), axis sign (+Z/-Z/+X/-X), and yaw angle.
 */
public final class DirectionHud extends HudModule {

    private final BooleanSetting showAxisIndicator = registerSetting(new BooleanSetting(
            "show_axis_indicator", "Show Axis (+X / -Z)", "Append coordinate axis direction beside cardinal name", true
    ));
    private final BooleanSetting showYawDegrees = registerSetting(new BooleanSetting(
            "show_yaw_degrees", "Show Yaw Angle", "Include exact horizontal yaw angle in degrees", false
    ));

    public DirectionHud() {
        super(
                "direction_hud",
                "Direction HUD",
                "Displays cardinal facing direction and axis.",
                ModuleCategory.HUD_INFORMATION,
                false,
                14.0f,
                628.0f,
                96.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null) {
            drawSimpleLabelValue(context, "Facing", "South (+Z)");
            return;
        }
        Direction dir = mc.player.getHorizontalFacing();
        String name = switch (dir) {
            case NORTH -> "North";
            case SOUTH -> "South";
            case WEST -> "West";
            case EAST -> "East";
            default -> "North";
        };
        String axis = switch (dir) {
            case NORTH -> "(-Z)";
            case SOUTH -> "(+Z)";
            case WEST -> "(-X)";
            case EAST -> "(+X)";
            default -> "";
        };

        StringBuilder sb = new StringBuilder(name);
        if (showAxisIndicator.isEnabled()) {
            sb.append(" ").append(axis);
        }
        if (showYawDegrees.isEnabled()) {
            float yaw = MathHelper.wrapDegrees(mc.player.getYaw(tickDelta));
            sb.append(String.format(" [%.0f°]", yaw));
        }
        drawSimpleLabelValue(context, "", sb.toString());
    }
}
