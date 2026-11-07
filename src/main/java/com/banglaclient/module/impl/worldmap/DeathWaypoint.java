package com.banglaclient.module.impl.worldmap;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 250: Death Waypoint
 * Automatically creates a waypoint in the Waypoints system whenever the player dies.
 */
public final class DeathWaypoint extends AbstractModule {

    private final BooleanSetting printCoordsInChat = registerSetting(new BooleanSetting(
            "print_death_coords_chat", "Log Coordinates Locally", "Print your death coordinates in local client chat for safekeeping", true
    ));

    private boolean recordedCurrentDeath = false;

    public DeathWaypoint() {
        super(
                "death_waypoint",
                "Death Waypoint",
                "Creates an automatic waypoint at your death location.",
                ModuleCategory.WORLD_MAP,
                true
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
        if (mc.player.isDead() || mc.player.getHealth() <= 0.0f) {
            if (!recordedCurrentDeath) {
                recordedCurrentDeath = true;
                var pos = mc.player.getBlockPos();
                String dim = mc.world.getRegistryKey().getValue().getPath();
                Waypoints.addWaypoint(new Waypoints.WaypointEntry(
                        "Death",
                        "Deaths",
                        dim,
                        pos,
                        new ColorData(255, 58, 48, 255)
                ));
            }
        } else {
            recordedCurrentDeath = false;
        }
    }

    public boolean isPrintCoordsInChat() {
        return isEnabled() && printCoordsInChat.isEnabled();
    }
}
