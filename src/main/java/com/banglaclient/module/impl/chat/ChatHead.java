package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;

/**
 * Module 220: Chat Head
 * Shows 8x8 player skin heads beside chat messages when the sender matches an online player entry.
 */
public final class ChatHead extends AbstractModule {

    private final BooleanSetting renderHatLayer = registerSetting(new BooleanSetting(
            "render_hat_layer", "Render Skin Hat Layer", "Include the 3D outer hat layer on chat head icons", true
    ));

    public ChatHead() {
        super(
                "chat_head",
                "Chat Head",
                "Shows player skin heads beside chat messages.",
                ModuleCategory.CHAT,
                true
        );
    }

    public boolean drawSenderHead(DrawContext context, String rawLineText, int x, int y) {
        if (!isEnabled() || mc.getNetworkHandler() == null || rawLineText == null || rawLineText.isEmpty()) {
            return false;
        }
        for (PlayerListEntry entry : mc.getNetworkHandler().getPlayerList()) {
            String name = entry.getProfile().name();
            if (name != null && !name.isEmpty() && rawLineText.contains(name)) {
                PlayerSkinDrawer.draw(context, entry.getSkinTextures(), x, y, 8);
                return true;
            }
        }
        return false;
    }
}
