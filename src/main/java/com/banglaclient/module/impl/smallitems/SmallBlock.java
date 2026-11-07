package com.banglaclient.module.impl.smallitems;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

/**
 * Module 216: Small Block
 * Reduces held 3D block size (such as Obsidian, Cobblestone, or Anchors) in first-person view.
 */
public final class SmallBlock extends AbstractModule {

    private final FloatSetting blockScale = registerSetting(new FloatSetting(
            "small_block_scale", "Block Scale", "Size multiplier when holding placeable blocks", 0.70f, 0.25f, 1.25f, 0.05f, "x"
    ));

    public SmallBlock() {
        super(
                "small_block",
                "Small Block",
                "Reduces held block size in first-person view.",
                ModuleCategory.SMALL_ITEM_SYSTEM,
                false
        );
    }

    public boolean appliesTo(ItemStack stack) {
        return isEnabled() && stack != null && stack.getItem() instanceof BlockItem;
    }

    public float getBlockScale() {
        return blockScale.getValue();
    }
}
