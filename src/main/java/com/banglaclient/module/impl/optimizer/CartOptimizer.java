package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 047: Cart Optimizer
 * Optimizes minecart, TNT minecart, and chest minecart rendering and sound channel saturation.
 */
public final class CartOptimizer extends AbstractModule {

    private final IntegerSetting cartCullDistance = registerSetting(new IntegerSetting(
            "cart_cull_dist", "Minecart Render Distance", "Cull minecart entities beyond this distance", 48, 16, 96, 4, "m"
    ));
    private final BooleanSetting skipDistantInnerBlock = registerSetting(new BooleanSetting(
            "skip_distant_inner_block", "Cull Distant Cart Contents", "Skip rendering interior chest/TNT/hopper block inside carts beyond 24m", true
    ));

    public CartOptimizer() {
        super(
                "cart_optimizer",
                "Cart Optimizer",
                "Optimizes minecart and TNT-cart rendering workload.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullCart(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = cartCullDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean shouldSkipInnerBlock(double squaredDistance) {
        return isEnabled() && skipDistantInnerBlock.isEnabled() && squaredDistance > 576.0;
    }
}
