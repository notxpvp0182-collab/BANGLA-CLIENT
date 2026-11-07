package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 111: Hit Counter
 * Counts total client-observable successful attacks during the current session.
 */
public final class HitCounter extends HudModule {

    private int sessionHits = 0;

    public HitCounter() {
        super(
                "hit_counter",
                "Hit Counter",
                "Counts successful client-observable hits during the session.",
                ModuleCategory.PVP_COMBAT,
                false,
                14.0f,
                556.0f,
                78.0f,
                20.0f
        );

        registerSetting(new ActionSetting(
                "reset_hits",
                "Reset Counter",
                "Reset the session hit counter to zero",
                "Reset Hits",
                () -> this.sessionHits = 0
        ));
    }

    public void recordHit() {
        this.sessionHits++;
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        drawSimpleLabelValue(context, "Hits", String.valueOf(sessionHits));
    }
}
