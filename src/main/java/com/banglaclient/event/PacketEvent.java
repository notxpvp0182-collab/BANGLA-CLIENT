package com.banglaclient.event;

import net.minecraft.network.packet.Packet;

/**
 * Read-only observational event for incoming/outgoing packets.
 * Used strictly for legitimate telemetry: Packet Rate Display, Server TPS estimation,
 * and Ping Graph. Does NOT spoof or manipulate gameplay packets.
 */
public final class PacketEvent extends Event {

    public enum Direction {
        INBOUND,
        OUTBOUND
    }

    private final Direction direction;
    private final Packet<?> packet;
    private final long timestampMillis;

    public PacketEvent(Direction direction, Packet<?> packet) {
        this.direction = direction;
        this.packet = packet;
        this.timestampMillis = System.currentTimeMillis();
    }

    public Direction getDirection() {
        return direction;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }
}
