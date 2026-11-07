package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

/**
 * Module 130: Light Level Display
 * Displays current Block and Sky light levels and warns if block light is 0 (hostile mob spawn threshold).
 */
public final class LightLevelDisplay extends HudModule {

    private final BooleanSetting showSkyLight = registerSetting(new BooleanSetting(
            "show_sky_light", "Show Sky Light", "Display both Block and Sky light values", true
    ));

    public LightLevelDisplay() {
        super(
                "light_level_display",
                "Light Level Display",
                "Displays current block and sky light levels.",
                ModuleCategory.HUD_INFORMATION,
                false,
                14.0f,
                676.0f,
                108.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int blockLight = 15;
        int skyLight = 15;
        if (mc.world != null && mc.player != null) {
            BlockPos pos = mc.player.getBlockPos();
            blockLight = mc.world.getLightLevel(LightType.BLOCK, pos);
            skyLight = mc.world.getLightLevel(LightType.SKY, pos);
        }
        String val = showSkyLight.isEnabled()
                ? (blockLight + " (Sky: " + skyLight + ")")
                : String.valueOf(blockLight);
        drawSimpleLabelValue(context, "Light", val);
    }
}
