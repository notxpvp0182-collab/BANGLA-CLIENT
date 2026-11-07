package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Module 200: Stack Counter
 * Clearly displays the total combined count of the currently held item across your entire inventory.
 */
public final class StackCounter extends AbstractModule {

    private final BooleanSetting includeOffhand = registerSetting(new BooleanSetting(
            "include_offhand_total", "Include Offhand", "Include offhand items when calculating total item count", true
    ));

    public StackCounter() {
        super(
                "stack_counter",
                "Stack Counter",
                "Calculates total combined count of the held item across inventory.",
                ModuleCategory.INVENTORY_ITEMS,
                false
        );
    }

    public int getTotalMatchingItemCount(Item targetItem) {
        if (mc.player == null || targetItem == null) {
            return 0;
        }
        PlayerInventory inv = mc.player.getInventory();
        int total = 0;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack s = inv.getStack(i);
            if (s.isOf(targetItem)) {
                total += s.getCount();
            }
        }
        if (includeOffhand.isEnabled() && mc.player.getOffHandStack().isOf(targetItem)) {
            total += mc.player.getOffHandStack().getCount();
        }
        return total;
    }
}
