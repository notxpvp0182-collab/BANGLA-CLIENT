package com.banglaclient.module.impl.input;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 103: Fly Speed Control
 * Controls the built-in Creative/Spectator fly speed multiplier ONLY in legitimate environments
 * where the game/server permits flight (Creative mode, Spectator mode, or Singleplayer with flight allowed).
 *
 * FAIR-PLAY GUARANTEE: Never grants unauthorized flight in survival multiplayer.
 */
public final class FlySpeedControl extends AbstractModule {

    private final FloatSetting speedMultiplier = registerSetting(new FloatSetting(
            "fly_speed_multiplier", "Fly Speed Multiplier", "Multiplier for built-in Creative/Spectator fly speed (1.0x = Vanilla)", 2.0f, 0.5f, 6.0f, 0.25f, "x"
    ));

    private static final float VANILLA_FLY_SPEED = 0.05f;

    public FlySpeedControl() {
        super(
                "fly_speed_control",
                "Fly Speed",
                "Controls the built-in Fly Speed multiplier in permitted modes.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    @Override
    public void onDisable() {
        if (mc.player != null && (mc.player.isCreative() || mc.player.isSpectator())) {
            mc.player.getAbilities().setFlySpeed(VANILLA_FLY_SPEED);
        }
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.START || mc.player == null) {
            return;
        }
        // Strictly verify legitimate Creative / Spectator permission
        boolean permitted = mc.player.getAbilities().allowFlying
                && (mc.player.isCreative() || mc.player.isSpectator() || mc.isInSingleplayer());

        if (permitted) {
            mc.player.getAbilities().setFlySpeed(VANILLA_FLY_SPEED * speedMultiplier.getValue());
        } else if (mc.player.getAbilities().getFlySpeed() != VANILLA_FLY_SPEED) {
            mc.player.getAbilities().setFlySpeed(VANILLA_FLY_SPEED);
        }
    }
}
