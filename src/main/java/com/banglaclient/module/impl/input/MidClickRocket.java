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
 * Module 092: Mid Click -> Rocket
 * Allows middle-click Firework Rocket selection and legitimate boost activation while gliding with an Elytra.
 */
public final class MidClickRocket extends AbstractModule {

    private final BooleanSetting onlyWhenGliding = registerSetting(new BooleanSetting(
            "only_when_gliding", "Only While Gliding", "Only trigger rocket selection when actively flying with an Elytra", true
    ));
    private final BooleanSetting autoUseRocket = registerSetting(new BooleanSetting(
            "auto_use_rocket", "Use Selected Rocket", "Trigger standard item use after selecting the rocket slot", true
    ));
    private final BooleanSetting restoreSlot = registerSetting(new BooleanSetting(
            "restore_slot", "Restore Previous Slot", "Return to previous hotbar slot after boosting", true
    ));

    public MidClickRocket() {
        super(
                "mid_click_rocket",
                "Mid Click -> Rocket",
                "Selects and uses a hotbar Firework Rocket on middle mouse click.",
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
        if (onlyWhenGliding.isEnabled() && !mc.player.isGliding()) {
            return;
        }

        PlayerInventory inv = mc.player.getInventory();
        int prevSlot = inv.getSelectedSlot();

        for (int slot = 0; slot < 9; slot++) {
            if (inv.getStack(slot).isOf(Items.FIREWORK_ROCKET)) {
                inv.setSelectedSlot(slot);
                if (autoUseRocket.isEnabled()) {
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
