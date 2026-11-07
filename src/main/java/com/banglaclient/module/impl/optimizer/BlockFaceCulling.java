package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

/**
 * Module 015: Block Face Culling
 * Avoids rendering interior block faces that are guaranteed not to be visible
 * (such as adjacent identical translucent/foliage blocks or buried full cubes).
 */
public final class BlockFaceCulling extends AbstractModule {

    private final BooleanSetting cullIdenticalTranslucent = registerSetting(new BooleanSetting(
            "cull_identical_translucent", "Cull Matching Glass/Ice", "Hide inner faces between identical adjacent glass or ice blocks", true
    ));
    private final BooleanSetting aggressiveLeafInterior = registerSetting(new BooleanSetting(
            "aggressive_leaf_interior", "Cull Buried Leaf Faces", "Cull interior leaf faces surrounded on all 6 sides", true
    ));

    public BlockFaceCulling() {
        super(
                "block_face_culling",
                "Block Face Culling",
                "Avoids rendering block faces guaranteed not to be visible.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullFace(BlockRenderView world, BlockState state, BlockPos pos, Direction direction) {
        if (!isEnabled() || world == null || state == null || pos == null || direction == null) {
            return false;
        }
        BlockPos neighborPos = pos.offset(direction);
        BlockState neighborState = world.getBlockState(neighborPos);
        if (cullIdenticalTranslucent.isEnabled() && state.getBlock() == neighborState.getBlock()) {
            if (!state.isOpaque()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAggressiveLeafInterior() {
        return isEnabled() && aggressiveLeafInterior.isEnabled();
    }
}
