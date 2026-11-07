package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.theme.ThemeManager;
import com.banglaclient.theme.ThemePalette;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * Module 196: Shulker Box Preview
 * Shows Shulker Box contents in a clean 9x3 grid overlay when hovering over a Shulker Box
 * in any inventory screen using Minecraft 1.21.11 DataComponentTypes.CONTAINER.
 */
public final class ShulkerBoxPreview extends AbstractModule {

    private final BooleanSetting requireShiftKey = registerSetting(new BooleanSetting(
            "require_shift_key", "Require Holding Shift", "Only render the 27-slot visual grid while holding Shift", false
    ));

    public ShulkerBoxPreview() {
        super(
                "shulker_box_preview",
                "Shulker Box Preview",
                "Shows Shulker Box contents visually without opening it.",
                ModuleCategory.INVENTORY_ITEMS,
                true
        );
    }

    public void renderShulkerGrid(DrawContext context, ItemStack shulkerStack, int mouseX, int mouseY) {
        if (!isEnabled() || shulkerStack == null || shulkerStack.isEmpty() || mc.textRenderer == null) {
            return;
        }
        ContainerComponent container = shulkerStack.get(DataComponentTypes.CONTAINER);
        if (container == null) {
            return;
        }

        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
        container.copyTo(stacks);

        ThemePalette palette = ThemeManager.getInstance().getPalette();
        int boxW = 9 * 18 + 10;
        int boxH = 3 * 18 + 22;
        int px = Math.max(6, mouseX + 12);
        int py = Math.max(6, mouseY - boxH - 6);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0.0f, 0.0f);

        Render2DUtil.drawRoundedRect(context, px, py, boxW, boxH, 5.0f, 0xF2121218);
        Render2DUtil.drawRoundedOutline(context, px, py, boxW, boxH, 5.0f, 1.0f, palette.getAccent().getArgb());
        context.drawText(mc.textRenderer, shulkerStack.getName().getString(), px + 6, py + 5, palette.getPrimaryText().getArgb(), false);

        for (int i = 0; i < 27; i++) {
            int col = i % 9;
            int row = i / 9;
            int sx = px + 5 + col * 18;
            int sy = py + 17 + row * 18;
            Render2DUtil.drawRoundedRect(context, sx, sy, 16.0f, 16.0f, 2.0f, 0x6622222C);
            ItemStack item = stacks.get(i);
            if (!item.isEmpty()) {
                context.drawItem(item, sx, sy);
                context.drawStackOverlay(mc.textRenderer, item, sx, sy);
            }
        }

        context.getMatrices().popMatrix();
    }

    public boolean isRequireShiftKey() {
        return requireShiftKey.isEnabled();
    }
}
