package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 078: Texture Memory Optimizer
 * Reduces unnecessary texture memory bandwidth pressure by controlling mipmap levels and skipping
 * off-screen animated atlas tick uploads.
 */
public final class TextureMemoryOptimizer extends AbstractModule {

    private final BooleanSetting skipOffscreenAtlasAnimations = registerSetting(new BooleanSetting(
            "skip_offscreen_atlas", "Smart Animated Textures", "Skip ticking animated block textures when their dimension is not active", true
    ));
    private final IntegerSetting recommendedMipmapLevel = registerSetting(new IntegerSetting(
            "recommended_mipmap", "Target Mipmap Level", "Mipmap level for balanced VRAM usage and distant clarity", 2, 0, 4, 1, ""
    ));

    public TextureMemoryOptimizer() {
        super(
                "texture_memory_optimizer",
                "Texture Memory Optimizer",
                "Reduces unnecessary VRAM texture memory bandwidth pressure.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public boolean isSkipOffscreenAtlasAnimations() {
        return isEnabled() && skipOffscreenAtlasAnimations.isEnabled();
    }

    public int getRecommendedMipmapLevel() {
        return recommendedMipmapLevel.getValue();
    }
}
