package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Module 208: Offhand HUD
 * Displays the currently equipped offhand item, stack count, and durability on a free-floating HUD.
 */
public final class OffhandHud extends HudModule {

    private final BooleanSetting showItemName = registerSetting(new BooleanSetting(
            "show_offhand_name", "Show Item Name", "Display the offhand item's name beside its icon", true
    ));

    public OffhandHud() {
        super(
                "offhand_hud",
                "Offhand HUD",
                "Displays your currently equipped offhand item.",
                ModuleCategory.INVENTORY_ITEMS,
                false,
                380.0f,
                238.0f,
                96.0f,
                24.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        ItemStack stack = (mc.player != null) ? mc.player.getOffHandStack() : ItemStack.EMPTY;
        if (stack.isEmpty() && isEditing) {
            stack = new ItemStack(Items.TOTEM_OF_UNDYING);
        }
        if (stack.isEmpty()) {
            drawSimpleLabelValue(context, "Offhand", "Empty");
            return;
        }

        this.unscaledHeight = 24.0f;
        context.drawItem(stack, 4, 4);
        context.drawStackOverlay(mc.textRenderer, stack, 4, 4);

        if (showItemName.isEnabled()) {
            String name = stack.getName().getString();
            int nw = mc.textRenderer.getWidth(name);
            this.unscaledWidth = Math.max(64.0f, 26.0f + nw + 6.0f);
            context.drawText(mc.textRenderer, name, 24, 8, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        } else {
            this.unscaledWidth = 26.0f;
        }
    }
}
