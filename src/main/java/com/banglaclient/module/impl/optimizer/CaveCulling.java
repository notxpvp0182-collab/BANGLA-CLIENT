package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.util.math.BlockPos;

/**
 * Module 004: Cave Culling
 * Optimizes underground rendering by reducing work for deep subterranean entities/block-entities
 * hidden far below the player when the player is on the surface.
 */
public final class CaveCulling extends AbstractModule {

    private final IntegerSetting depthThreshold = registerSetting(new IntegerSetting(
            "depth_threshold", "Vertical Depth Margin", "Vertical distance below surface player to cull sealed caves", 24, 12, 64, 2, "m"
    ));
    private final BooleanSetting onlyWhenSkyVisible = registerSetting(new BooleanSetting(
            "only_sky_visible", "Surface Only Check", "Only cull deep underground geometry when player has sky access", true
    ));

    public CaveCulling() {
        super(
                "cave_culling",
                "Cave Culling",
                "Optimizes underground rendering for hidden subterranean areas.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullUndergroundPos(BlockPos targetPos) {
        if (!isEnabled() || mc.player == null || mc.world == null || targetPos == null) {
            return false;
        }
        if (mc.player.isSpectator()) {
            return false;
        }
        BlockPos playerPos = mc.player.getBlockPos();
        if (onlyWhenSkyVisible.isEnabled() && !mc.world.isSkyVisible(playerPos)) {
            return false;
        }
        int dy = playerPos.getY() - targetPos.getY();
        if (dy < depthThreshold.getValue()) {
            return false;
        }
        return !mc.world.isSkyVisible(targetPos);
    }
}
