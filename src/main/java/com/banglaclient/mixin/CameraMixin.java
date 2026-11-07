package com.banglaclient.mixin;

import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.camera.CameraDistance;
import com.banglaclient.module.impl.camera.Freelook;
import com.banglaclient.module.impl.camera.ThreeDF5View;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private boolean thirdPerson;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Shadow
    protected abstract void moveBy(float f, float g, float h);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", shift = At.Shift.AFTER), require = 0)
    private void banglaclient$applyFreelookAndShoulder(CallbackInfo ci) {
        ModuleRegistry.getInstance().getByClass(Freelook.class).ifPresent(freelook -> {
            if (freelook.isEnabled()) {
                this.setRotation(freelook.getCameraYaw(), freelook.getCameraPitch());
            }
        });

        if (this.thirdPerson) {
            ModuleRegistry.getInstance().getByClass(ThreeDF5View.class).ifPresent(f5 -> {
                if (f5.isEnabled()) {
                    this.moveBy(0.0f, f5.getVerticalOffsetY(), f5.getShoulderOffsetX());
                }
            });
        }
    }

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"), index = 0, require = 0)
    private float banglaclient$modifyThirdPersonDistance(float vanillaDistance) {
        var camDist = ModuleRegistry.getInstance().getByClass(CameraDistance.class);
        if (camDist.isPresent() && camDist.get().isEnabled()) {
            return camDist.get().getTargetDistance();
        }
        return vanillaDistance;
    }
}
