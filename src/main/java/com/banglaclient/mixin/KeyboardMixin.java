package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.KeyInputEvent;
import com.banglaclient.keybind.KeybindManager;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.hardware.IdleTaskSuspension;
import com.banglaclient.module.impl.optimizer.InputOptimizer;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true, require = 0)
    private void banglaclient$onKeyInput(long window, int action, KeyInput input, CallbackInfo ci) {
        if (window != this.client.getWindow().getHandle() || input == null) {
            return;
        }
        ModuleRegistry.getInstance().getByClass(IdleTaskSuspension.class).ifPresent(IdleTaskSuspension::notifyUserActivity);

        var inputOpt = ModuleRegistry.getInstance().getByClass(InputOptimizer.class);
        if (inputOpt.isPresent() && inputOpt.get().shouldSkipGameplayRepeat(action)) {
            ci.cancel();
            return;
        }

        KeybindManager.getInstance().onKeyInput(input.key(), action);
        KeyInputEvent event = ClientCore.getInstance().getEventBus().post(
                KeyInputEvent.get(input.key(), input.scancode(), action, input.modifiers())
        );
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
