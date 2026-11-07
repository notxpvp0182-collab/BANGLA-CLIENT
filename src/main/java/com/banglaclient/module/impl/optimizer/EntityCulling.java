package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Module 003: Entity Culling
 * Avoids rendering entities that cannot be seen from the camera when safe visibility checks
 * confirm they are occluded by solid terrain. Caches raycast results across ticks to avoid per-frame overhead.
 */
public final class EntityCulling extends AbstractModule {

    private final IntegerSetting maxRenderDistance = registerSetting(new IntegerSetting(
            "max_entity_dist", "Max Entity Distance", "Cull non-boss entities beyond this distance", 64, 24, 128, 4, "m"
    ));
    private final BooleanSetting raycastOcclusion = registerSetting(new BooleanSetting(
            "raycast_occlusion", "Terrain Occlusion Check", "Skip entities completely hidden behind solid blocks", true
    ));
    private final BooleanSetting alwaysRenderPlayers = registerSetting(new BooleanSetting(
            "always_render_players", "Always Render Nearby Players", "Never occlusion-cull players within 24 blocks", true
    ));

    private final Map<Integer, Long> occlusionCache = new ConcurrentHashMap<>();
    private static final long CACHE_VALIDITY_MS = 150L;

    public EntityCulling() {
        super(
                "entity_culling",
                "Entity Culling",
                "Skips rendering entities hidden behind solid walls or too far away.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullEntity(Entity entity) {
        if (!isEnabled() || entity == null || mc.player == null || mc.world == null) {
            return false;
        }
        if (entity == mc.player || entity instanceof EnderDragonEntity || entity.isGlowing()) {
            return false;
        }

        double sqDist = mc.player.squaredDistanceTo(entity);
        int maxDist = maxRenderDistance.getValue();
        if (sqDist > (double) (maxDist * maxDist)) {
            return true;
        }

        if (entity instanceof PlayerEntity && alwaysRenderPlayers.isEnabled() && sqDist <= 576.0) {
            return false;
        }

        if (!raycastOcclusion.isEnabled() || sqDist < 36.0) {
            return false;
        }

        long now = System.currentTimeMillis();
        Long cachedValue = occlusionCache.get(entity.getId());
        if (cachedValue != null) {
            long timestamp = cachedValue >> 1;
            boolean occluded = (cachedValue & 1L) == 1L;
            if (now - timestamp < CACHE_VALIDITY_MS) {
                return occluded;
            }
        }

        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        Vec3d targetCenter = entity.getBoundingBox().getCenter();
        BlockHitResult hit = mc.world.raycast(new RaycastContext(
                cameraPos,
                targetCenter,
                RaycastContext.ShapeType.VISUAL,
                RaycastContext.FluidHandling.NONE,
                mc.player
        ));

        boolean isOccluded = hit.getType() == HitResult.Type.BLOCK;
        occlusionCache.put(entity.getId(), (now << 1) | (isOccluded ? 1L : 0L));
        return isOccluded;
    }

    @Override
    public void onDisable() {
        occlusionCache.clear();
    }
}
