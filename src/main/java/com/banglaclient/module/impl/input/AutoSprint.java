package com.banglaclient.module.impl.input;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 096: Auto Sprint
 * Automatically maintains sprint while moving forward when legitimate vanilla sprint rules are met
 * (hunger > 6, not sneaking, not blinded, not colliding with walls).
 */
public final class AutoSprint extends AbstractModule {

    private final BooleanSetting respectHungerCheck = registerSetting(new BooleanSetting(
            "respect_hunger", "Respect Hunger Rules", "Only sprint when food level > 6 or in Creative", true
    ));
    private final BooleanSetting stopInWater = registerSetting(new BooleanSetting(
            "stop_in_water", "Pause When Submerged", "Avoid forcing sprint when wading in shallow water", false
    ));

    public AutoSprint() {
        super(
                "auto_sprint",
                "Auto Sprint",
                "Automatically maintains sprint while moving forward.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.START || mc.player == null) {
            return;
        }
        if (stopInWater.isEnabled() && mc.player.isTouchingWater() && !mc.player.isSubmergedInWater()) {
            return;
        }
        boolean canSprintFood = !respectHungerCheck.isEnabled()
                || mc.player.getHungerManager().getFoodLevel() > 6.0f
                || mc.player.getAbilities().allowFlying;

        if (canSprintFood
                && mc.player.forwardSpeed > 0.0f
                && !mc.player.isSneaking()
                && !mc.player.isUsingItem()
                && !mc.player.horizontalCollision) {
            mc.player.setSprinting(true);
        }
    }
}
