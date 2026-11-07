package com.banglaclient.event;

/**
 * Fired when a keyboard key is pressed, released, or repeated.
 */
public final class KeyInputEvent extends Event {

    private static final KeyInputEvent INSTANCE = new KeyInputEvent();

    private int key;
    private int scanCode;
    private int action;
    private int modifiers;

    private KeyInputEvent() {
    }

    public static KeyInputEvent get(int key, int scanCode, int action, int modifiers) {
        INSTANCE.setCancelled(false);
        INSTANCE.key = key;
        INSTANCE.scanCode = scanCode;
        INSTANCE.action = action;
        INSTANCE.modifiers = modifiers;
        return INSTANCE;
    }

    public int getKey() {
        return key;
    }

    public int getScanCode() {
        return scanCode;
    }

    public int getAction() {
        return action;
    }

    public int getModifiers() {
        return modifiers;
    }
}
