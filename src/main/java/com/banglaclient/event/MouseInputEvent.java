package com.banglaclient.event;

/**
 * Fired on mouse button clicks and scroll wheel movements.
 * Used by CPS Counter, Keystrokes, Mid-Click binds, and Input Optimizer.
 */
public final class MouseInputEvent extends Event {

    private static final MouseInputEvent INSTANCE = new MouseInputEvent();

    private int button;
    private int action;
    private int modifiers;
    private double scrollHorizontal;
    private double scrollVertical;
    private boolean scrollEvent;

    private MouseInputEvent() {
    }

    public static MouseInputEvent forButton(int button, int action, int modifiers) {
        INSTANCE.setCancelled(false);
        INSTANCE.button = button;
        INSTANCE.action = action;
        INSTANCE.modifiers = modifiers;
        INSTANCE.scrollHorizontal = 0.0;
        INSTANCE.scrollVertical = 0.0;
        INSTANCE.scrollEvent = false;
        return INSTANCE;
    }

    public static MouseInputEvent forScroll(double horizontal, double vertical) {
        INSTANCE.setCancelled(false);
        INSTANCE.button = -1;
        INSTANCE.action = -1;
        INSTANCE.modifiers = 0;
        INSTANCE.scrollHorizontal = horizontal;
        INSTANCE.scrollVertical = vertical;
        INSTANCE.scrollEvent = true;
        return INSTANCE;
    }

    public int getButton() {
        return button;
    }

    public int getAction() {
        return action;
    }

    public int getModifiers() {
        return modifiers;
    }

    public double getScrollHorizontal() {
        return scrollHorizontal;
    }

    public double getScrollVertical() {
        return scrollVertical;
    }

    public boolean isScrollEvent() {
        return scrollEvent;
    }
}
