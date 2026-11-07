package com.banglaclient.module.impl.coremod;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 307: Feature Toggle System
 * Controls global module card description visibility and toggle confirmation behavior.
 */
public final class FeatureToggleSystem extends AbstractModule {

    private final BooleanSetting showModuleDescriptions = registerSetting(new BooleanSetting(
            "show_module_descriptions", "Show Module Descriptions", "Show module descriptions on Click GUI cards", true
    ));

    public FeatureToggleSystem() {
        super(
                "feature_toggle_system",
                "Feature Toggle System",
                "Controls global module enable/disable and description behavior.",
                ModuleCategory.BANGLACLIENT_CORE,
                true
        );
        showModuleDescriptions.onChange((oldV, newV) ->
                ClientCore.getInstance().getClientState().setShowModuleDescriptions(newV)
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getClientState().setShowModuleDescriptions(showModuleDescriptions.isEnabled());
    }
}
