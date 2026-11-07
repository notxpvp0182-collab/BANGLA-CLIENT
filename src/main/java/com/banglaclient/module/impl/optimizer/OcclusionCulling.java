package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 016: Occlusion Culling
 * Enforces hardware/graph chunk occlusion culling and skips rendering objects hidden behind solid geometry.
 */
public final class OcclusionCulling extends AbstractModule {

    private final BooleanSetting enforceChunkCulling = registerSetting(new BooleanSetting(
            "enforce_chunk_culling", "Enforce Graph Culling", "Ensure Minecraft chunk visibility graph culling is always active", true
    ));
    private final IntegerSetting frustumPadding = registerSetting(new IntegerSetting(
            "frustum_padding", "Frustum Margin", "Conservative bounding box margin in blocks", 2, 0, 8, 1, "m"
    ));

    public OcclusionCulling() {
        super(
                "occlusion_culling",
                "Occlusion Culling",
                "Reduces rendering of geometry hidden behind solid terrain.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    @Override
    public void onEnable() {
        if (enforceChunkCulling.isEnabled()) {
            mc.chunkCullingEnabled = true;
        }
    }

    public int getFrustumPadding() {
        return frustumPadding.getValue();
    }
}
