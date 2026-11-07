package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;

/**
 * Module 197: Container Preview
 * Previews supported container contents (Bundles, Chests with NBT/Component items, Dispensers)
 * via Minecraft 1.21.11 DataComponentTypes.CONTAINER and DataComponentTypes.BUNDLE_CONTENTS.
 */
public final class ContainerPreview extends AbstractModule {

    private final BooleanSetting previewBundles = registerSetting(new BooleanSetting(
            "preview_bundles", "Preview Bundles", "Show enhanced bundle occupancy and contents", true
    ));
    private final BooleanSetting previewComponentChests = registerSetting(new BooleanSetting(
            "preview_component_chests", "Preview Pick-Block Chests", "Show contents of chests/barrels holding ContainerComponent data", true
    ));

    public ContainerPreview() {
        super(
                "container_preview",
                "Container Preview",
                "Previews supported Bundle and container item contents.",
                ModuleCategory.INVENTORY_ITEMS,
                true
        );
    }

    public boolean hasPreviewableContents(ItemStack stack) {
        if (!isEnabled() || stack == null || stack.isEmpty()) {
            return false;
        }
        if (previewBundles.isEnabled()) {
            BundleContentsComponent bundle = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
            if (bundle != null && !bundle.isEmpty()) {
                return true;
            }
        }
        if (previewComponentChests.isEnabled()) {
            ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
            return container != null;
        }
        return false;
    }
}
