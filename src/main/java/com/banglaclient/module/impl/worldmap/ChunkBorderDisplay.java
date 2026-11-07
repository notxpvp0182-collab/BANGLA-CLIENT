package com.banglaclient.module.impl.worldmap;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import org.lwjgl.glfw.GLFW;

/**
 * Module 251: Chunk Border Display
 * Toggles Minecraft's chunk boundary wireframe grid without requiring F3+G chord combinations.
 */
public final class ChunkBorderDisplay extends AbstractModule {

    public ChunkBorderDisplay() {
        super(
                "chunk_border_display",
                "Chunk Border Display",
                "Displays 16x16 chunk boundaries.",
                ModuleCategory.WORLD_MAP,
                false
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_F9));
    }

    @Override
    public void onEnable() {
        if (mc.debugRenderer != null) {
            mc.debugRenderer.toggleShowChunkBorder();
        }
    }

    @Override
    public void onDisable() {
        if (mc.debugRenderer != null) {
            mc.debugRenderer.toggleShowChunkBorder();
        }
    }
}
