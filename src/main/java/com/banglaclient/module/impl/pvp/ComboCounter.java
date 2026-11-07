package com.banglaclient.module.impl.pvp;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 112: Combo Counter
 * Counts consecutive landed hits on targets and resets when the player takes damage
 * or when the combo timeout expires.
 */
public final class ComboCounter extends HudModule {

    private final IntegerSetting timeoutSeconds = registerSetting(new IntegerSetting(
            "combo_timeout_sec", "Combo Timeout", "Seconds without landing a hit before combo resets", 4, 2, 10, 1, "s"
    ));

    private int currentCombo = 0;
    private long lastHitMillis = 0L;

    public ComboCounter() {
        super(
                "combo_counter",
                "Combo Counter",
                "Counts consecutive hits in combat.",
                ModuleCategory.PVP_COMBAT,
                false,
                14.0f,
                580.0f,
                84.0f,
                20.0f
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    public void onAttackLanded() {
        this.currentCombo++;
        this.lastHitMillis = System.currentTimeMillis();
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.player == null) {
            return;
        }
        if (mc.player.hurtTime == mc.player.maxHurtTime && mc.player.maxHurtTime > 0) {
            currentCombo = 0;
            return;
        }
        if (currentCombo > 0 && (System.currentTimeMillis() - lastHitMillis) > (timeoutSeconds.getValue() * 1000L)) {
            currentCombo = 0;
        }
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        String text = currentCombo > 0 ? (currentCombo + " Combo") : "No Combo";
        drawSimpleLabelValue(context, "", text);
    }
}
