package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Module 145: Totem Counter
 * Displays total Totem of Undying count across your inventory, hotbar, and offhand.
 */
public final class TotemCounter extends HudModule {

    private final BooleanSetting renderTotemIcon = registerSetting(new BooleanSetting(
            "render_totem_icon", "Show Totem Item Icon", "Render a Totem of Undying item icon beside the count", true
    ));

    private static final ItemStack TOTEM_ICON_STACK = new ItemStack(Items.TOTEM_OF_UNDYING);

    public TotemCounter() {
        super(
                "totem_counter",
                "Totem Counter",
                "Displays available Totem count from inventory and offhand.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                358.0f,
                64.0f,
                22.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int count = 0;
        if (mc.player != null) {
            PlayerInventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.size(); i++) {
                ItemStack s = inv.getStack(i);
                if (s.isOf(Items.TOTEM_OF_UNDYING)) {
                    count += s.getCount();
                }
            }
            if (mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
                count += mc.player.getOffHandStack().getCount();
            }
        }

        if (renderTotemIcon.isEnabled() && mc.textRenderer != null) {
            this.unscaledWidth = 56.0f;
            this.unscaledHeight = 22.0f;
            context.drawItem(TOTEM_ICON_STACK, 4, 3);
            context.drawText(mc.textRenderer, "x" + count, 23, 7, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        } else {
            drawSimpleLabelValue(context, "Totems", String.valueOf(count));
        }
    }
}
