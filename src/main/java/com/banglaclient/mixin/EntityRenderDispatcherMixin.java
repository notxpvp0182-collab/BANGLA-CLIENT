package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.optimizer.CartOptimizer;
import com.banglaclient.module.impl.optimizer.CrystalOptimizer;
import com.banglaclient.module.impl.optimizer.EntityCulling;
import com.banglaclient.module.impl.optimizer.ItemEntityOptimizer;
import com.banglaclient.module.impl.optimizer.ProjectileOptimizer;
import com.banglaclient.module.impl.optimizer.RenderOptimizer;
import com.banglaclient.module.impl.optimizer.TntOptimizer;
import com.banglaclient.module.impl.optimizer.WindChargeOptimizer;
import com.banglaclient.module.impl.optimizer.XpOptimizer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks EntityRenderDispatcher#shouldRender to execute safe client-side entity culling
 * for EntityCulling, ItemEntityOptimizer, XpOptimizer, TntOptimizer, CrystalOptimizer,
 * ProjectileOptimizer, WindChargeOptimizer, CartOptimizer, and RenderOptimizer.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    private <E extends Entity> void banglaclient$onShouldRenderEntity(
            E entity,
            Frustum frustum,
            double x,
            double y,
            double z,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!cir.getReturnValue() || ClientCore.getInstance().getClientState().isVanillaMode()) {
            return;
        }
        if (ClientCore.getInstance().getClientState().isSafeMode()) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || entity == null) {
            return;
        }

        double sqDist = mc.player.squaredDistanceTo(entity);
        ModuleRegistry reg = ModuleRegistry.getInstance();

        // 1. Empty invisible marker ArmorStand check
        if (entity instanceof ArmorStandEntity stand && stand.isInvisible()) {
            var renderOpt = reg.getByClass(RenderOptimizer.class);
            if (renderOpt.isPresent() && renderOpt.get().shouldSkipEmptyInvisibleArmorStand()) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 2. Dropped Item Entity culling
        if (entity instanceof ItemEntity) {
            var itemOpt = reg.getByClass(ItemEntityOptimizer.class);
            if (itemOpt.isPresent() && itemOpt.get().shouldCullItemEntity(sqDist)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 3. Experience Orb culling
        if (entity instanceof ExperienceOrbEntity) {
            var xpOpt = reg.getByClass(XpOptimizer.class);
            if (xpOpt.isPresent() && xpOpt.get().shouldCullXpOrb(sqDist)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 4. Primed TNT culling
        if (entity instanceof TntEntity) {
            var tntOpt = reg.getByClass(TntOptimizer.class);
            if (tntOpt.isPresent() && tntOpt.get().shouldCullTnt(sqDist)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 5. End Crystal culling
        if (entity instanceof EndCrystalEntity) {
            var crystalOpt = reg.getByClass(CrystalOptimizer.class);
            if (crystalOpt.isPresent() && crystalOpt.get().shouldCullCrystal(sqDist)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 6. Grounded Arrow / Projectile culling
        if (entity instanceof PersistentProjectileEntity proj && proj.isOnGround()) {
            var projOpt = reg.getByClass(ProjectileOptimizer.class);
            if (projOpt.isPresent() && projOpt.get().shouldCullGroundedArrow(sqDist)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 7. Wind Charge culling
        if (entity instanceof WindChargeEntity) {
            var windOpt = reg.getByClass(WindChargeOptimizer.class);
            if (windOpt.isPresent() && windOpt.get().shouldCullWindCharge(sqDist)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 8. Minecart culling
        if (entity instanceof AbstractMinecartEntity) {
            var cartOpt = reg.getByClass(CartOptimizer.class);
            if (cartOpt.isPresent() && cartOpt.get().shouldCullCart(sqDist)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 9. General Terrain Occlusion & Distance Entity Culling
        var entityCulling = reg.getByClass(EntityCulling.class);
        if (entityCulling.isPresent() && entityCulling.get().shouldCullEntity(entity)) {
            cir.setReturnValue(false);
        }
    }
}
