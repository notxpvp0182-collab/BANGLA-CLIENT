package com.banglaclient.module.impl.input;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.item.BlockItem;

/**
 * Module 101: Fast Block Placement
 * Improves block-placement right-click cooldown responsiveness when holding building blocks
 * without bypassing server-side placement validation. Includes a Server-Safe Mode lock enabled by default.
 */
public final class FastBlockPlacement extends AbstractModule {

    private final IntegerSetting placeDelayTicks = registerSetting(new IntegerSetting(
            "place_delay_ticks", "Placement Interval", "Client right-click cooldown ticks when holding blocks (Vanilla = 4)", 2, 1, 4, 1, " ticks"
    ));
    private final BooleanSetting serverSafeLock = registerSetting(new BooleanSetting(
            "server_safe_lock", "Server-Safe Mode", "Enforce a minimum 2-tick interval on multiplayer servers", true
    ));
    private final BooleanSetting onlyBuildingBlocks = registerSetting(new BooleanSetting(
            "only_building_blocks", "Only Block Items", "Only apply reduced delay when holding placeable BlockItems", true
    ));

    public FastBlockPlacement() {
        super(
                "fast_block_placement",
                "Fast Block Placement",
                "Improves block-placement input responsiveness safely.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
    }

    public boolean shouldApplyFastPlace() {
        if (!isEnabled() || mc.player == null) {
            return false;
        }
        if (onlyBuildingBlocks.isEnabled()) {
            return mc.player.getMainHandStack().getItem() instanceof BlockItem
                    || mc.player.getOffHandStack().getItem() instanceof BlockItem;
        }
        return true;
    }

    public int getTargetPlaceCooldown() {
        int val = placeDelayTicks.getValue();
        if (serverSafeLock.isEnabled() && !mc.isInSingleplayer()) {
            return Math.max(2, val);
        }
        return val;
    }
}
