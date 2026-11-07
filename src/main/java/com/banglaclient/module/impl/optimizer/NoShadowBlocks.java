package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 048: No Shadow Blocks
 * Reduces or disables ambient occlusion shadow gradients and entity ground shadows on blocks
 * for cleaner visuals and reduced vertex lighting calculation overhead.
 */
public final class NoShadowBlocks extends AbstractModule {

    private final BooleanSetting disableEntityGroundShadows = registerSetting(new BooleanSetting(
            "disable_entity_shadows", "Disable Entity Ground Shadows", "Turn off circular entity shadows projected onto blocks", true
    ));
    private final BooleanSetting flatBlockLighting = registerSetting(new BooleanSetting(
            "flat_block_lighting", "Reduce Ambient Occlusion", "Minimize heavy smooth lighting shadow corners on low-end GPUs", false
    ));

    public NoShadowBlocks() {
        super(
                "no_shadow_blocks",
                "No Shadow Blocks",
                "Reduces or disables unnecessary block and entity ground shadows.",
                ModuleCategory.OPTIMIZER,
                false
        );
    }

    @Override
    public void onEnable() {
        if (disableEntityGroundShadows.isEnabled() && mc.options != null) {
            mc.options.getEntityShadows().setValue(false);
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null) {
            mc.options.getEntityShadows().setValue(true);
        }
    }

    public boolean isFlatBlockLighting() {
        return isEnabled() && flatBlockLighting.isEnabled();
    }
}
