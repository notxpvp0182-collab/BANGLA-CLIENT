package com.banglaclient.module.impl.input;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Module 102: Accurate Block Placement
 * Improves aiming/interaction precision when holding right-click to place a line of blocks
 * (prevents accidentally placing blocks onto the face of the block you just placed while bridging).
 */
public final class AccurateBlockPlacement extends AbstractModule {

    private final BooleanSetting lockPlaneWhileBridging = registerSetting(new BooleanSetting(
            "lock_plane_bridging", "Plane Alignment Guard", "Prevent accidental double-stacking on the same block face while moving", true
    ));
    private final BooleanSetting preventSelfTrapClick = registerSetting(new BooleanSetting(
            "prevent_self_trap", "Head-Space Guard", "Avoid accidental right-click placement directly inside eye-level movement path", false
    ));

    private BlockPos lastPlacedPos = null;
    private Direction lastPlacedSide = null;
    private long lastPlaceTimeMillis = 0L;

    public AccurateBlockPlacement() {
        super(
                "accurate_block_placement",
                "Accurate Block Placement",
                "Improves aiming and face-alignment precision when placing blocks.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
    }

    public boolean shouldAllowPlacement(HitResult crosshairTarget) {
        if (!isEnabled() || !(crosshairTarget instanceof BlockHitResult bhr) || mc.player == null) {
            return true;
        }
        BlockPos targetPos = bhr.getBlockPos();
        Direction side = bhr.getSide();
        long now = System.currentTimeMillis();

        if (lockPlaneWhileBridging.isEnabled() && lastPlacedPos != null && (now - lastPlaceTimeMillis) < 250L) {
            if (targetPos.equals(lastPlacedPos) && side == lastPlacedSide && side.getAxis().isHorizontal()) {
                return false;
            }
        }

        if (preventSelfTrapClick.isEnabled()) {
            BlockPos resultBlock = targetPos.offset(side);
            if (resultBlock.equals(mc.player.getBlockPos().up())) {
                return false;
            }
        }

        lastPlacedPos = targetPos.offset(side);
        lastPlacedSide = side;
        lastPlaceTimeMillis = now;
        return true;
    }
}
