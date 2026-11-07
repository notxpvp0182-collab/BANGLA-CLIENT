package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 189: Cloud Distance
 * Controls cloud rendering height offset and maximum cloud draw distance.
 */
public final class CloudDistance extends AbstractModule {

    private final IntegerSetting cloudRadiusChunks = registerSetting(new IntegerSetting(
            "cloud_radius_chunks", "Cloud Distance", "Cloud mesh render radius in chunks", 8, 2, 32, 2, " chunks"
    ));
    private final FloatSetting cloudHeightOffset = registerSetting(new FloatSetting(
            "cloud_height_offset", "Cloud Altitude Offset", "Vertical Y-offset added to vanilla cloud altitude", 0.0f, -64.0f, 128.0f, 4.0f, "m"
    ));

    public CloudDistance() {
        super(
                "cloud_distance",
                "Cloud Distance",
                "Controls cloud rendering distance and altitude.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
    }

    public int getCloudRadiusChunks() { return cloudRadiusChunks.getValue(); }
    public float getCloudHeightOffset() { return isEnabled() ? cloudHeightOffset.getValue() : 0.0f; }
}
