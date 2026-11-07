package com.banglaclient.event;

import net.minecraft.client.MinecraftClient;

/**
 * Fired at the start and end of every client game tick (20 times per second).
 * Uses a reusable instance to avoid per-tick garbage collection pressure.
 */
public final class ClientTickEvent extends Event {

    public enum Phase {
        START,
        END
    }

    private static final ClientTickEvent START_INSTANCE = new ClientTickEvent(Phase.START);
    private static final ClientTickEvent END_INSTANCE = new ClientTickEvent(Phase.END);

    private final Phase phase;
    private MinecraftClient client;

    private ClientTickEvent(Phase phase) {
        this.phase = phase;
    }

    public static ClientTickEvent getStart(MinecraftClient client) {
        START_INSTANCE.setCancelled(false);
        START_INSTANCE.client = client;
        return START_INSTANCE;
    }

    public static ClientTickEvent getEnd(MinecraftClient client) {
        END_INSTANCE.setCancelled(false);
        END_INSTANCE.client = client;
        return END_INSTANCE;
    }

    public Phase getPhase() {
        return phase;
    }

    public MinecraftClient getClient() {
        return client;
    }
}
