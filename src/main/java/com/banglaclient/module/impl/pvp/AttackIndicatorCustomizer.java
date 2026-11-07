package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.option.AttackIndicator;

/**
 * Module 109: Attack Indicator Customizer
 * Customizes the vanilla attack indicator style (Crosshair, Hotbar, or Off) and full-charge alert.
 */
public final class AttackIndicatorCustomizer extends AbstractModule {

    private final ModeSetting indicatorStyle = registerSetting(new ModeSetting(
            "indicator_style", "Indicator Placement", "Where to show the vanilla attack indicator",
            "Crosshair", "Crosshair", "Hotbar", "Off"
    ));
    private final BooleanSetting highlightFullCharge = registerSetting(new BooleanSetting(
            "highlight_full_charge", "Highlight Full Charge", "Tint crosshair indicator when weapon reaches 100% readiness", true
    ));

    public AttackIndicatorCustomizer() {
        super(
                "attack_indicator_customizer",
                "Attack Indicator Customizer",
                "Customizes attack indicator position and style.",
                ModuleCategory.PVP_COMBAT,
                false
        );
        indicatorStyle.onChange((oldVal, newVal) -> applyOption(newVal));
    }

    @Override
    public void onEnable() {
        applyOption(indicatorStyle.getValue());
    }

    private void applyOption(String mode) {
        if (mc.options == null) return;
        AttackIndicator target = switch (mode) {
            case "Hotbar" -> AttackIndicator.HOTBAR;
            case "Off" -> AttackIndicator.OFF;
            default -> AttackIndicator.CROSSHAIR;
        };
        mc.options.getAttackIndicator().setValue(target);
    }

    public boolean isHighlightFullCharge() {
        return isEnabled() && highlightFullCharge.isEnabled();
    }
}
