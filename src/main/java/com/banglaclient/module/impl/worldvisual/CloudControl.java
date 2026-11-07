package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.option.CloudRenderMode;

/**
 * Module 188: Cloud Control
 * Enables, disables, or sets Fast/Fancy cloud rendering directly from BANGLACLIENT.
 */
public final class CloudControl extends AbstractModule {

    private final ModeSetting cloudMode = registerSetting(new ModeSetting(
            "cloud_render_mode", "Cloud Mode", "Cloud rendering mode", "OFF", "OFF", "FAST", "FANCY"
    ));

    public CloudControl() {
        super(
                "cloud_control",
                "Cloud Control",
                "Enables, disables, or configures cloud rendering.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
        cloudMode.onChange((oldV, newV) -> syncWithVanilla(newV));
    }

    @Override
    public void onEnable() {
        syncWithVanilla(cloudMode.getValue());
    }

    private void syncWithVanilla(String mode) {
        if (mc.options == null) return;
        CloudRenderMode target = switch (mode) {
            case "FAST" -> CloudRenderMode.FAST;
            case "FANCY" -> CloudRenderMode.FANCY;
            default -> CloudRenderMode.OFF;
        };
        mc.options.getCloudRenderMode().setValue(target);
    }
}
