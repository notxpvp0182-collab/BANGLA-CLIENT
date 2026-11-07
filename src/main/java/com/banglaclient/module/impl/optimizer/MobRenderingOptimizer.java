package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;

/**
 * Module 007: Mob Rendering Optimizer
 * Reduces unnecessary mob/entity rendering workload (shadows, feature layers, and distant passive mobs).
 */
public final class MobRenderingOptimizer extends AbstractModule {

    private final IntegerSetting mobShadowDistance = registerSetting(new IntegerSetting(
            "mob_shadow_dist", "Mob Shadow Distance", "Disable circular mob ground shadows beyond this distance", 20, 4, 64, 2, "m"
    ));
    private final BooleanSetting skipDistantMobArmorLayers = registerSetting(new BooleanSetting(
            "skip_distant_mob_layers", "Cull Distant Mob Layers", "Skip secondary armor/item layers on mobs beyond 36 blocks", true
    ));

    public MobRenderingOptimizer() {
        super(
                "mob_rendering_optimizer",
                "Mob Rendering Optimizer",
                "Reduces unnecessary mob rendering, shadows, and feature layers.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldRenderMobShadow(Entity entity, double squaredDistance) {
        if (!isEnabled() || !(entity instanceof MobEntity)) {
            return true;
        }
        int d = mobShadowDistance.getValue();
        return squaredDistance <= (double) (d * d);
    }

    public boolean shouldSkipMobFeatureLayers(double squaredDistance) {
        return isEnabled() && skipDistantMobArmorLayers.isEnabled() && squaredDistance > 1296.0;
    }
}
