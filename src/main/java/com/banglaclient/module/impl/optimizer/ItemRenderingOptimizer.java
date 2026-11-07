package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 006: Item Rendering Optimizer
 * Optimizes dropped-item and held-item rendering by reducing redundant stack models and bobbing math at distance.
 */
public final class ItemRenderingOptimizer extends AbstractModule {

    private final BooleanSetting simplifyDistantItems = registerSetting(new BooleanSetting(
            "simplify_distant_items", "Simplify Distant Drops", "Render single item model for distant ground stacks", true
    ));
    private final IntegerSetting simplifyDistance = registerSetting(new IntegerSetting(
            "simplify_dist", "Simplify Distance", "Distance beyond which ground item stacks render as a single quad", 16, 6, 48, 2, "m"
    ));
    private final BooleanSetting staticDistantRotation = registerSetting(new BooleanSetting(
            "static_distant_rot", "Freeze Distant Spin", "Pause item spinning animation when far away", true
    ));

    public ItemRenderingOptimizer() {
        super(
                "item_rendering_optimizer",
                "Item Rendering Optimizer",
                "Optimizes dropped-item and held-item rendering performance.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldSimplifyGroundStack(double squaredDistance) {
        if (!isEnabled() || !simplifyDistantItems.isEnabled()) {
            return false;
        }
        int d = simplifyDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean shouldPauseSpin(double squaredDistance) {
        if (!isEnabled() || !staticDistantRotation.isEnabled()) {
            return false;
        }
        int d = simplifyDistance.getValue();
        return squaredDistance > (double) (d * d);
    }
}
