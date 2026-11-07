package com.banglaclient.module.impl.network;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 240: Server Brand Display
 * Displays the server software brand (e.g., Paper, Velocity, Purpur, Fabric, Vanilla) reported via BrandCustomPayload.
 */
public final class ServerBrandDisplay extends HudModule {

    public ServerBrandDisplay() {
        super(
                "server_brand_display",
                "Server Brand Display",
                "Displays reported server software brand (Paper, Velocity, etc.).",
                ModuleCategory.NETWORK_SERVER,
                false,
                460.0f,
                62.0f,
                122.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        String brand = "Vanilla";
        if (mc.getNetworkHandler() != null && mc.getNetworkHandler().getBrand() != null) {
            brand = mc.getNetworkHandler().getBrand();
        }
        drawSimpleLabelValue(context, "Server", brand);
    }
}
