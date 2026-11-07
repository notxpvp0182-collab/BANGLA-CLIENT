package com.banglaclient.module.impl.input;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.MouseInputEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

/**
 * Module 091: Mid Click -> Elytra
 * Allows configurable middle-click selection (and optional legitimate right-click equip) of an Elytra
 * present in the player's hotbar.
 */
public final class MidClickElytra extends AbstractModule {

    private final BooleanSetting interactToEquip = registerSetting(new BooleanSetting(
            "interact_to_equip", "Right-Click Equip", "Automatically use the selected hotbar Elytra to equip it", true
    ));
    private final BooleanSetting restorePreviousSlot = registerSetting(new BooleanSetting(
            "restore_previous_slot", "Restore Hotbar Slot", "Switch back to your previous hotbar slot after equipping", true
    ));

    public MidClickElytra() {
        super(
                "mid_click_elytra",
                "Mid Click -> Elytra",
                "Selects or equips a hotbar Elytra on middle mouse click.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, MouseInputEvent.class, this::onMouse);
    }

    private void onMouse(MouseInputEvent event) {
        if (event.isScrollEvent() || event.getButton() != GLFW.GLFW_MOUSE_BUTTON_MIDDLE || event.getAction() != GLFW.GLFW_PRESS) {
            return;
        }
        if (mc.player == null || mc.interactionManager == null || mc.currentScreen != null) {
            return;
        }

        PlayerInventory inv = mc.player.getInventory();
        int previousSlot = inv.getSelectedSlot();

        for (int slot = 0; slot < 9; slot++) {
            if (inv.getStack(slot).isOf(Items.ELYTRA)) {
                inv.setSelectedSlot(slot);
                if (interactToEquip.isEnabled()) {
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    if (restorePreviousSlot.isEnabled()) {
                        inv.setSelectedSlot(previousSlot);
                    }
                }
                break;
            }
        }
    }
}
