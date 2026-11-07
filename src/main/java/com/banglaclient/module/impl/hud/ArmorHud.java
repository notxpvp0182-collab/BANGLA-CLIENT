package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Module 142: Armor HUD
 * Customizable armor and durability display (Horizontal or Vertical layout) on a continuous,
 * non-snapping HUD element.
 */
public final class ArmorHud extends HudModule {

    private final ModeSetting orientation = registerSetting(new ModeSetting(
            "armor_orientation", "Orientation", "Layout direction for equipped armor slots",
            "Vertical", "Vertical", "Horizontal"
    ));
    private final BooleanSetting showDurabilityNumber = registerSetting(new BooleanSetting(
            "show_dura_number", "Show Durability Value", "Display remaining durability points or percentage", true
    ));
    private final BooleanSetting showPercentage = registerSetting(new BooleanSetting(
            "show_dura_pct", "Durability As %", "Display durability as percentage instead of raw points", false
    ));

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public ArmorHud() {
        super(
                "armor_hud",
                "Armor HUD",
                "Customizable armor and durability display.",
                ModuleCategory.HUD_INFORMATION,
                true,
                14.0f,
                260.0f,
                68.0f,
                76.0f
        );
        showPercentage.visibleWhen(showDurabilityNumber::isEnabled);
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;

        boolean vertical = orientation.isMode("Vertical");
        if (vertical) {
            this.unscaledWidth = showDurabilityNumber.isEnabled() ? 68.0f : 26.0f;
            this.unscaledHeight = 74.0f;
        } else {
            this.unscaledWidth = 84.0f;
            this.unscaledHeight = showDurabilityNumber.isEnabled() ? 32.0f : 24.0f;
        }

        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            ItemStack stack = (mc.player != null) ? mc.player.getEquippedStack(ARMOR_SLOTS[i]) : ItemStack.EMPTY;
            if (stack.isEmpty() && isEditing) {
                stack = switch (i) {
                    case 0 -> new ItemStack(Items.NETHERITE_HELMET);
                    case 1 -> new ItemStack(Items.NETHERITE_CHESTPLATE);
                    case 2 -> new ItemStack(Items.NETHERITE_LEGGINGS);
                    default -> new ItemStack(Items.NETHERITE_BOOTS);
                };
            }
            if (stack.isEmpty()) continue;

            int ix = vertical ? 4 : (4 + i * 20);
            int iy = vertical ? (3 + i * 17) : 4;
            context.drawItem(stack, ix, iy);

            if (showDurabilityNumber.isEnabled() && stack.isDamageable()) {
                int max = stack.getMaxDamage();
                int rem = Math.max(0, max - stack.getDamage());
                String text = showPercentage.isEnabled()
                        ? ((rem * 100 / Math.max(1, max)) + "%")
                        : String.valueOf(rem);
                if (vertical) {
                    context.drawText(mc.textRenderer, text, 24, iy + 4, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
                } else {
                    context.drawText(mc.textRenderer, text, ix + 1, iy + 17, hudStyle.getTextColor().getArgb(), false);
                }
            }
        }
    }
}
