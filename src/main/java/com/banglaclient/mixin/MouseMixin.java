package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.MouseInputEvent;
import com.banglaclient.keybind.KeybindManager;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.camera.Freelook;
import com.banglaclient.module.impl.hardware.IdleTaskSuspension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$onMouseButton(long window, MouseInput input, int action, CallbackInfo ci) {
        if (window != this.client.getWindow().getHandle() || input == null) {
            return;
        }
        int button = input.button();
        int mods = input.modifiers();

        ModuleRegistry.getInstance().getByClass(IdleTaskSuspension.class).ifPresent(IdleTaskSuspension::notifyUserActivity);

        if (action == GLFW.GLFW_PRESS) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                PerformanceTracker.getInstance().recordLeftClick();
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                PerformanceTracker.getInstance().recordRightClick();
            }
        }

        KeybindManager.getInstance().onMouseInput(button, action);

        MouseInputEvent event = ClientCore.getInstance().getEventBus().post(
                MouseInputEvent.forButton(button, action, mods)
        );
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window != this.client.getWindow().getHandle()) {
            return;
        }
        MouseInputEvent event = ClientCore.getInstance().getEventBus().post(
                MouseInputEvent.forScroll(horizontal, vertical)
        );
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$handleFreelookRotation(double timeDelta, CallbackInfo ci) {
        var freelookOpt = ModuleRegistry.getInstance().getByClass(Freelook.class);
        if (freelookOpt.isPresent() && freelookOpt.get().isEnabled() && this.client.currentScreen == null) {
            freelookOpt.get().updateCameraAngles(this.cursorDeltaX, this.cursorDeltaY);
            this.cursorDeltaX = 0.0;
            this.cursorDeltaY = 0.0;
            ci.cancel();
        }
    }
}
