package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 002: Render Optimizer
 * Reduces unnecessary client rendering work and avoids redundant render calculations.
 */
public final class RenderOptimizer extends AbstractModule {

    private final BooleanSetting skipInvisibleArmorStands = registerSetting(new BooleanSetting(
            "skip_invisible_armor_stands", "Skip Empty Invisible Stands", "Avoid full model pass for empty invisible marker armor stands", true
    ));
    private final BooleanSetting cullDistantBlockEntities = registerSetting(new BooleanSetting(
            "cull_distant_block_entities", "Cull Distant Block Entities", "Skip rendering chests and skulls beyond threshold distance", true
    ));
    private final IntegerSetting blockEntityMaxDistance = registerSetting(new IntegerSetting(
            "block_entity_max_dist", "Block Entity Distance", "Maximum distance in blocks to render block entities", 48, 16, 128, 4, "m"
    ));

    public RenderOptimizer() {
        super(
                "render_optimizer",
                "Render Optimizer",
                "Reduces unnecessary client rendering work and redundant passes.",
                ModuleCategory.OPTIMIZER,
                true
        );
        blockEntityMaxDistance.visibleWhen(cullDistantBlockEntities::isEnabled);
    }

    public boolean shouldSkipEmptyInvisibleArmorStand() {
        return isEnabled() && skipInvisibleArmorStands.isEnabled();
    }

    public boolean shouldCullBlockEntity(double squaredDistance) {
        if (!isEnabled() || !cullDistantBlockEntities.isEnabled()) {
            return false;
        }
        double max = blockEntityMaxDistance.getValue();
        return squaredDistance > (max * max);
    }
}
