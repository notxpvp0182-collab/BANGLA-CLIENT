package com.banglaclient.module.impl.camera;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

/**
 * Module 151: Freelook
 * Allows independent 360-degree camera rotation while preserving the player's actual server-side
 * movement direction and yaw/pitch.
 */
public final class Freelook extends AbstractModule {

    private final FloatSetting sensitivityMultiplier = registerSetting(new FloatSetting(
            "freelook_sensitivity", "Sensitivity Multiplier", "Mouse sensitivity scale while in Freelook", 1.0f, 0.2f, 2.5f, 0.05f, "x"
    ));
    private final BooleanSetting autoThirdPerson = registerSetting(new BooleanSetting(
            "auto_third_person", "Auto Third-Person", "Automatically switch to third-person perspective while Freelook is active", true
    ));
    private final BooleanSetting invertPitch = registerSetting(new BooleanSetting(
            "invert_pitch", "Invert Vertical Pitch", "Invert vertical mouse look while in Freelook", false
    ));

    private float cameraYaw = 0.0f;
    private float cameraPitch = 0.0f;
    private Perspective previousPerspective = Perspective.FIRST_PERSON;

    public Freelook() {
        super(
                "freelook",
                "BANGLA Freelook",
                "Smooth 360 freelook camera while preserving movement direction.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_V, Keybind.TriggerMode.HOLD));
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            this.cameraYaw = mc.player.getYaw();
            this.cameraPitch = mc.player.getPitch();
        }
        if (mc.options != null && autoThirdPerson.isEnabled()) {
            this.previousPerspective = mc.options.getPerspective();
            mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null && autoThirdPerson.isEnabled()) {
            mc.options.setPerspective(previousPerspective);
        }
    }

    public void updateCameraAngles(double deltaYaw, double deltaPitch) {
        float sens = sensitivityMultiplier.getValue() * 0.15f;
        this.cameraYaw += (float) deltaYaw * sens;
        float pitchStep = (float) deltaPitch * sens * (invertPitch.isEnabled() ? -1.0f : 1.0f);
        this.cameraPitch = MathHelper.clamp(this.cameraPitch + pitchStep, -90.0f, 90.0f);
    }

    public float getCameraYaw() {
        return cameraYaw;
    }

    public float getCameraPitch() {
        return cameraPitch;
    }
}
