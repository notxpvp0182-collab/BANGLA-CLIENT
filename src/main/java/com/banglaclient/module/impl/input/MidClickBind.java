package com.banglaclient.module.impl.input;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.MouseInputEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

/**
 * Module 090: Mid Click Bind
 * Allows middle mouse button quick hotbar selection for legitimate utility items (Ender Pearl,
 * Golden Apple, Wind Charge, or Firework Rocket) already present in the player's hotbar.
 */
public final class MidClickBind extends AbstractModule {

    private final ModeSetting targetUtility = registerSetting(new ModeSetting(
            "target_utility", "Middle-Click Target", "Hotbar item to select on middle mouse click",
            "Ender Pearl", "Ender Pearl", "Golden Apple", "Wind Charge", "Firework Rocket"
    ));

    public MidClickBind() {
        super(
                "mid_click_bind",
                "Mid Click Bind",
                "Quickly switches to a configured hotbar utility item on middle click.",
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
        if (mc.player == null || mc.currentScreen != null) {
            return;
        }

        Item desired = switch (targetUtility.getValue()) {
            case "Golden Apple" -> Items.GOLDEN_APPLE;
            case "Wind Charge" -> Items.WIND_CHARGE;
            case "Firework Rocket" -> Items.FIREWORK_ROCKET;
            default -> Items.ENDER_PEARL;
        };

        PlayerInventory inv = mc.player.getInventory();
        for (int slot = 0; slot < 9; slot++) {
            if (inv.getStack(slot).isOf(desired)) {
                inv.setSelectedSlot(slot);
                break;
            }
        }
    }
}
