package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

/**
 * Module 146: Inventory HUD
 * Displays your 27-slot main inventory (3 rows x 9 columns) in a compact, free-floating HUD grid.
 */
public final class InventoryHud extends HudModule {

    private final BooleanSetting showSlotGridLines = registerSetting(new BooleanSetting(
            "show_slot_grid", "Show Slot Grid", "Render subtle slot borders behind inventory items", true
    ));

    public InventoryHud() {
        super(
                "inventory_hud",
                "BANGLA Inventory",
                "Displays your 27-slot inventory in a compact HUD.",
                ModuleCategory.HUD_INFORMATION,
                true,
                320.0f,
                384.0f,
                168.0f,
                58.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 168.0f;
        this.unscaledHeight = 58.0f;

        if (mc.player == null || mc.textRenderer == null) {
            return;
        }
        PlayerInventory inv = mc.player.getInventory();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = 9 + row * 9 + col;
                int sx = 4 + col * 18;
                int sy = 3 + row * 18;

                if (showSlotGridLines.isEnabled()) {
                    Render2DUtil.drawRoundedOutline(context, sx, sy, 16.0f, 16.0f, 2.0f, 1.0f, 0x33FFFFFF);
                }

                ItemStack stack = inv.getStack(slotIndex);
                if (!stack.isEmpty()) {
                    context.drawItem(stack, sx, sy);
                    context.drawStackOverlay(mc.textRenderer, stack, sx, sy);
                }
            }
        }
    }
}
