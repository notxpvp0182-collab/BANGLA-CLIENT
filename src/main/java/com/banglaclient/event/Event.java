package com.banglaclient.event;

/**
 * Base class for all client events dispatched through the BANGLACLIENT EventBus.
 * Supports optional cancellation for interceptable events without per-frame object allocation.
 */
public abstract class Event {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
