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
 * Module 093: Mid Click -> XP
 * Allows middle-click Experience Bottle hotbar selection and legitimate throw interaction.
 */
public final class MidClickXp extends AbstractModule {

    private final BooleanSetting throwOnSelect = registerSetting(new BooleanSetting(
            "throw_on_select", "Throw On Middle-Click", "Use the Experience Bottle after selecting its hotbar slot", true
    ));
    private final BooleanSetting restoreSlot = registerSetting(new BooleanSetting(
            "restore_slot", "Restore Previous Slot", "Return to previous weapon/tool slot after throwing", true
    ));

    public MidClickXp() {
        super(
                "mid_click_xp",
                "Mid Click -> XP",
                "Selects and uses a hotbar XP Bottle on middle mouse click.",
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
        int prevSlot = inv.getSelectedSlot();

        for (int slot = 0; slot < 9; slot++) {
            if (inv.getStack(slot).isOf(Items.EXPERIENCE_BOTTLE)) {
                inv.setSelectedSlot(slot);
                if (throwOnSelect.isEnabled()) {
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    if (restoreSlot.isEnabled()) {
                        inv.setSelectedSlot(prevSlot);
                    }
                }
                break;
            }
        }
    }
}
