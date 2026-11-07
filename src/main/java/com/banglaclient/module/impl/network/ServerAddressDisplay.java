package com.banglaclient.module.impl.network;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ServerInfo;

/**
 * Module 239: Server Address Display
 * Displays the current server IP/domain on a free-floating HUD element.
 */
public final class ServerAddressDisplay extends HudModule {

    private final BooleanSetting hideNumericPort = registerSetting(new BooleanSetting(
            "hide_numeric_port", "Hide :25565 Port", "Strip standard port suffixes from the displayed address", true
    ));

    public ServerAddressDisplay() {
        super(
                "server_address_display",
                "Server Address Display",
                "Displays the current connected server address.",
                ModuleCategory.NETWORK_SERVER,
                false,
                460.0f,
                38.0f,
                138.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        ServerInfo info = (mc != null) ? mc.getCurrentServerEntry() : null;
        String address = (info != null && info.address != null) ? info.address : (mc.isInSingleplayer() ? "Singleplayer" : ClientCore.OFFICIAL_SERVER);
        if (hideNumericPort.isEnabled() && address.endsWith(":25565")) {
            address = address.substring(0, address.length() - 6);
        }
        drawSimpleLabelValue(context, "IP", address);
    }
}
