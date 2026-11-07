package com.banglaclient.module.impl.worldmap;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

/**
 * Module 249: Death Location
 * Records the exact coordinates and dimension of your most recent death and displays them on the HUD.
 */
public final class DeathLocation extends HudModule {

    private BlockPos lastDeathPos = null;
    private String lastDeathDimension = "overworld";
    private boolean wasDeadLastTick = false;

    public DeathLocation() {
        super(
                "death_location",
                "Death Location",
                "Records and displays your most recent death coordinates.",
                ModuleCategory.WORLD_MAP,
                false,
                460.0f,
                166.0f,
                148.0f,
                20.0f
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.player == null || mc.world == null) {
            return;
        }
        boolean deadNow = mc.player.isDead() || mc.player.getHealth() <= 0.0f;
        if (deadNow && !wasDeadLastTick) {
            this.lastDeathPos = mc.player.getBlockPos();
            this.lastDeathDimension = mc.world.getRegistryKey().getValue().getPath();
        }
        this.wasDeadLastTick = deadNow;
    }

    public BlockPos getLastDeathPos() {
        return lastDeathPos;
    }

    public String getLastDeathDimension() {
        return lastDeathDimension;
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (lastDeathPos != null) {
            String coords = String.format("%d, %d, %d (%s)", lastDeathPos.getX(), lastDeathPos.getY(), lastDeathPos.getZ(), lastDeathDimension);
            drawSimpleLabelValue(context, "Last Death", coords);
        } else {
            drawSimpleLabelValue(context, "Last Death", "None Recorded");
        }
    }
}
