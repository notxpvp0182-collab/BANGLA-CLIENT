package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.camera.ViewmodelEditor;
import com.banglaclient.module.impl.inventory.HeldItemScale;
import com.banglaclient.module.impl.inventory.SmallItems;
import com.banglaclient.module.impl.smallitems.SmallAxe;
import com.banglaclient.module.impl.smallitems.SmallBlock;
import com.banglaclient.module.impl.smallitems.SmallFood;
import com.banglaclient.module.impl.smallitems.SmallPickaxe;
import com.banglaclient.module.impl.smallitems.SmallShield;
import com.banglaclient.module.impl.smallitems.SmallShovel;
import com.banglaclient.module.impl.smallitems.SmallSword;
import com.banglaclient.module.impl.smallitems.SmallTotem;
import com.banglaclient.module.impl.smallitems.TotemResizer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Inject(method = "applyEquipOffset", at = @At("TAIL"), require = 0)
    private void banglaclient$applyViewmodelAndSmallItems(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        if (ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            return;
        }

        boolean isMainHand = (arm == mc.player.getMainArm());
        ItemStack item = isMainHand ? mc.player.getMainHandStack() : mc.player.getOffHandStack();

        ModuleRegistry reg = ModuleRegistry.getInstance();
        float totalScale = 1.0f;
        float translateY = 0.0f;

        var vmOpt = reg.getByClass(ViewmodelEditor.class);
        if (vmOpt.isPresent() && vmOpt.get().isEnabled()) {
            ViewmodelEditor vm = vmOpt.get();
            float handSign = isMainHand ? 1.0f : -1.0f;
            matrices.translate(vm.getOffsetX() * handSign, vm.getOffsetY(), vm.getOffsetZ());
            if (vm.getRotPitch() != 0.0f) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(vm.getRotPitch()));
            if (vm.getRotYaw() != 0.0f) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(vm.getRotYaw() * handSign));
            if (vm.getRotRoll() != 0.0f) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(vm.getRotRoll() * handSign));
            totalScale *= vm.getScale();
        }

        var heldScaleOpt = reg.getByClass(HeldItemScale.class);
        if (heldScaleOpt.isPresent() && heldScaleOpt.get().isEnabled()) {
            totalScale *= isMainHand ? heldScaleOpt.get().getMainHandScale() : heldScaleOpt.get().getOffHandScale();
        }

        var smallItemsOpt = reg.getByClass(SmallItems.class);
        if (smallItemsOpt.isPresent() && smallItemsOpt.get().isEnabled()) {
            totalScale *= smallItemsOpt.get().getGlobalItemScale();
        }

        if (item != null && !item.isEmpty()) {
            var sword = reg.getByClass(SmallSword.class);
            if (sword.isPresent() && sword.get().appliesTo(item)) totalScale *= sword.get().getSwordScale();

            var axe = reg.getByClass(SmallAxe.class);
            if (axe.isPresent() && axe.get().appliesTo(item)) totalScale *= axe.get().getAxeScale();

            var pick = reg.getByClass(SmallPickaxe.class);
            if (pick.isPresent() && pick.get().appliesTo(item)) totalScale *= pick.get().getPickaxeScale();

            var shovel = reg.getByClass(SmallShovel.class);
            if (shovel.isPresent() && shovel.get().appliesTo(item)) totalScale *= shovel.get().getShovelScale();

            var food = reg.getByClass(SmallFood.class);
            if (food.isPresent() && food.get().appliesTo(item)) totalScale *= food.get().getFoodScale();

            var block = reg.getByClass(SmallBlock.class);
            if (block.isPresent() && block.get().appliesTo(item)) totalScale *= block.get().getBlockScale();

            var shield = reg.getByClass(SmallShield.class);
            if (shield.isPresent() && shield.get().appliesTo(item)) {
                totalScale *= shield.get().getShieldScale();
                translateY += shield.get().getVerticalDrop();
            }

            var totem = reg.getByClass(SmallTotem.class);
            if (totem.isPresent() && totem.get().appliesTo(item)) {
                totalScale *= totem.get().getTotemScale();
                translateY += totem.get().getVerticalDrop();
            }

            if (item.isOf(Items.TOTEM_OF_UNDYING)) {
                var resizer = reg.getByClass(TotemResizer.class);
                if (resizer.isPresent() && resizer.get().isEnabled()) {
                    totalScale *= resizer.get().getHeldScaleMultiplier();
                }
            }
        }

        if (translateY != 0.0f) {
            matrices.translate(0.0f, translateY, 0.0f);
        }
        if (totalScale != 1.0f) {
            matrices.scale(totalScale, totalScale, totalScale);
        }
    }
}
