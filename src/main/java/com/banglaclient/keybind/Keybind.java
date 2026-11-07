package com.banglaclient.keybind;

import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

/**
 * Value object representing a keyboard or mouse binding with optional hold/toggle behavior.
 */
public final class Keybind {

    public enum TriggerMode {
        TOGGLE,
        HOLD
    }

    public static final Keybind UNBOUND = new Keybind(InputDeviceType.NONE, GLFW.GLFW_KEY_UNKNOWN, TriggerMode.TOGGLE);

    private final InputDeviceType deviceType;
    private final int keyCode;
    private final TriggerMode triggerMode;

    public Keybind(InputDeviceType deviceType, int keyCode, TriggerMode triggerMode) {
        this.deviceType = deviceType != null ? deviceType : InputDeviceType.NONE;
        this.keyCode = keyCode;
        this.triggerMode = triggerMode != null ? triggerMode : TriggerMode.TOGGLE;
    }

    public static Keybind ofKey(int glfwKeyCode) {
        if (glfwKeyCode == GLFW.GLFW_KEY_UNKNOWN) {
            return UNBOUND;
        }
        return new Keybind(InputDeviceType.KEYBOARD, glfwKeyCode, TriggerMode.TOGGLE);
    }

    public static Keybind ofKey(int glfwKeyCode, TriggerMode mode) {
        if (glfwKeyCode == GLFW.GLFW_KEY_UNKNOWN) {
            return UNBOUND;
        }
        return new Keybind(InputDeviceType.KEYBOARD, glfwKeyCode, mode);
    }

    public static Keybind ofMouse(int mouseButton) {
        return new Keybind(InputDeviceType.MOUSE, mouseButton, TriggerMode.TOGGLE);
    }

    public boolean isBound() {
        return deviceType != InputDeviceType.NONE && keyCode != GLFW.GLFW_KEY_UNKNOWN;
    }

    public boolean matchesKey(int key) {
        return isBound() && deviceType == InputDeviceType.KEYBOARD && this.keyCode == key;
    }

    public boolean matchesMouse(int button) {
        return isBound() && deviceType == InputDeviceType.MOUSE && this.keyCode == button;
    }

    public InputDeviceType getDeviceType() {
        return deviceType;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public TriggerMode getTriggerMode() {
        return triggerMode;
    }

    public Keybind withTriggerMode(TriggerMode newMode) {
        return new Keybind(this.deviceType, this.keyCode, newMode);
    }

    public String getDisplayName() {
        if (!isBound()) {
            return "NONE";
        }
        if (deviceType == InputDeviceType.MOUSE) {
            return switch (keyCode) {
                case GLFW.GLFW_MOUSE_BUTTON_LEFT -> "MOUSE 1";
                case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> "MOUSE 2";
                case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> "MOUSE 3";
                default -> "MOUSE " + (keyCode + 1);
            };
        }
        try {
            String glfwName = GLFW.glfwGetKeyName(keyCode, 0);
            if (glfwName != null && !glfwName.isBlank()) {
                return glfwName.toUpperCase();
            }
            return InputUtil.fromKeyCode(new net.minecraft.client.input.KeyInput(keyCode, 0, 0)).getLocalizedText().getString().toUpperCase();
        } catch (Exception ignored) {
            return "KEY " + keyCode;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Keybind other)) return false;
        return keyCode == other.keyCode && deviceType == other.deviceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceType, keyCode);
    }
}
