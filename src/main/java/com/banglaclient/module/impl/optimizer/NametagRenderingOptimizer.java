package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Module 020: Nametag Rendering Optimizer
 * Controls nametag rendering distance and skips nametags for crowded non-player entities.
 */
public final class NametagRenderingOptimizer extends AbstractModule {

    private final IntegerSetting maxNametagDistance = registerSetting(new IntegerSetting(
            "max_nametag_dist", "Max Nametag Distance", "Do not render entity nametags beyond this distance", 32, 8, 64, 4, "m"
    ));
    private final BooleanSetting cullNonPlayerNametags = registerSetting(new BooleanSetting(
            "cull_mob_nametags", "Cull Distant Mob Tags", "Hide custom-named mob tags beyond 16 blocks", true
    ));

    public NametagRenderingOptimizer() {
        super(
                "nametag_rendering_optimizer",
                "Nametag Rendering Optimizer",
                "Controls nametag rendering distance and font pass workload.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldSkipNametag(Entity entity, double squaredDistance) {
        if (!isEnabled() || entity == null) {
            return false;
        }
        if (!(entity instanceof PlayerEntity) && cullNonPlayerNametags.isEnabled() && squaredDistance > 256.0) {
            return true;
        }
        int max = maxNametagDistance.getValue();
        return squaredDistance > (double) (max * max);
    }
}
