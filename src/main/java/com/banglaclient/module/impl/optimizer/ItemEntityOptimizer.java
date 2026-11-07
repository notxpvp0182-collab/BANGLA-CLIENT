package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 030: Item Entity Optimizer
 * Optimizes large numbers of ground item entities (ItemEntity) by culling distant drops
 * and disabling ground shadows on dropped items.
 */
public final class ItemEntityOptimizer extends AbstractModule {

    private final IntegerSetting itemCullDistance = registerSetting(new IntegerSetting(
            "item_cull_dist", "Ground Item Distance", "Do not render dropped item entities beyond this distance", 36, 12, 64, 4, "m"
    ));
    private final BooleanSetting disableItemShadows = registerSetting(new BooleanSetting(
            "disable_item_shadows", "Disable Item Shadows", "Skip rendering circular ground shadows underneath dropped items", true
    ));

    public ItemEntityOptimizer() {
        super(
                "item_entity_optimizer",
                "Item Entity Optimizer",
                "Optimizes large clusters of ground item entities and shadows.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullItemEntity(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = itemCullDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean isDisableItemShadows() {
        return isEnabled() && disableItemShadows.isEnabled();
    }
}
