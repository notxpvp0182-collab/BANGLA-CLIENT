package com.banglaclient.module.impl.hardware;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import org.lwjgl.glfw.GLFW;

/**
 * Module 076: Manual Memory Cleanup Keybind
 * Allows a configurable keybind to trigger supported memory cleanup routines on demand.
 */
public final class ManualMemoryCleanupKeybind extends AbstractModule {

    public ManualMemoryCleanupKeybind() {
        super(
                "manual_memory_cleanup_keybind",
                "Manual Memory Cleanup Keybind",
                "Allows a keybind to trigger safe memory cleanup routines.",
                ModuleCategory.RAM_CPU_GPU,
                false
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_UNKNOWN));
    }

    @Override
    public void onEnable() {
        MemoryCleanup.performSafeCleanup();
        // Immediately reset toggle state after executing the one-shot action
        setEnabled(false);
    }
}
