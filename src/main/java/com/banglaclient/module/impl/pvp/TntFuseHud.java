package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;

/**
 * Module 119: TNT Fuse HUD
 * Displays the shortest remaining fuse time in seconds for any primed TNT within 16 blocks.
 */
public final class TntFuseHud extends HudModule {

    public TntFuseHud() {
        super(
                "tnt_fuse_hud",
                "TNT Fuse HUD",
                "Displays nearby primed TNT fuse countdown.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                364.0f,
                96.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int minFuseTicks = Integer.MAX_VALUE;
        if (mc.world != null && mc.player != null) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof TntEntity tnt && mc.player.squaredDistanceTo(tnt) <= 256.0) {
                    if (tnt.getFuse() < minFuseTicks) {
                        minFuseTicks = tnt.getFuse();
                    }
                }
            }
        }
        if (minFuseTicks < Integer.MAX_VALUE) {
            float seconds = Math.max(0.0f, (minFuseTicks - tickDelta) / 20.0f);
            drawSimpleLabelValue(context, "TNT Fuse", String.format("%.2fs", seconds));
        } else {
            drawSimpleLabelValue(context, "TNT Fuse", "Clear");
        }
    }
}
