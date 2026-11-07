package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/**
 * Module 131: Target Block HUD
 * Displays the icon, name, and coordinates of the block currently targeted by the player's crosshair.
 */
public final class TargetBlockHud extends HudModule {

    private final BooleanSetting showBlockCoords = registerSetting(new BooleanSetting(
            "show_block_coords", "Show Coordinates", "Display the X/Y/Z coordinates of the targeted block", true
    ));

    public TargetBlockHud() {
        super(
                "target_block_hud",
                "Target Block HUD",
                "Displays information about the targeted block.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                14.0f,
                138.0f,
                28.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 138.0f;
        this.unscaledHeight = 28.0f;

        if (mc.world != null && mc.crosshairTarget instanceof BlockHitResult bhr && bhr.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = bhr.getBlockPos();
            BlockState state = mc.world.getBlockState(pos);
            ItemStack iconStack = new ItemStack(state.getBlock().asItem());
            if (!iconStack.isEmpty()) {
                context.drawItem(iconStack, 5, 6);
            }
            String name = mc.textRenderer.trimToWidth(state.getBlock().getName().getString(), 106);
            context.drawText(mc.textRenderer, name, 25, 5, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());

            if (showBlockCoords.isEnabled()) {
                String coords = pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
                context.drawText(mc.textRenderer, coords, 25, 16, 0xFF9E9EA8, false);
            }
        } else if (isEditing) {
            context.drawText(mc.textRenderer, "Target Block: Grass Block", 8, 10, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        } else {
            drawSimpleLabelValue(context, "Block", "Air");
        }
    }
}
