package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.StringSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;

/**
 * Module 252: Slime Chunk Info
 * Displays whether the current chunk is a Slime Chunk using the legitimate integrated singleplayer seed
 * or a user-supplied known public world seed.
 */
public final class SlimeChunkInfo extends HudModule {

    private final StringSetting knownWorldSeed = registerSetting(new StringSetting(
            "known_world_seed", "Known World Seed", "Enter known world seed for multiplayer Slime Chunk calculation", "0", 32
    ));

    public SlimeChunkInfo() {
        super(
                "slime_chunk_info",
                "Slime Chunk Info",
                "Displays slime chunk status using known world seed.",
                ModuleCategory.WORLD_MAP,
                false,
                460.0f,
                190.0f,
                118.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null) {
            drawSimpleLabelValue(context, "Slime Chunk", "No");
            return;
        }
        long seed = resolveSeed();
        if (seed == 0L && !mc.isInSingleplayer()) {
            drawSimpleLabelValue(context, "Slime Chunk", "Seed Unknown");
            return;
        }
        ChunkPos cp = mc.player.getChunkPos();
        boolean isSlime = ChunkRandom.getSlimeRandom(cp.x, cp.z, seed, 987234911L).nextInt(10) == 0;
        drawSimpleLabelValue(context, "Slime Chunk", isSlime ? "YES" : "No");
    }

    private long resolveSeed() {
        if (mc.isInSingleplayer() && mc.getServer() != null && mc.getServer().getOverworld() != null) {
            return mc.getServer().getOverworld().getSeed();
        }
        try {
            return Long.parseLong(knownWorldSeed.getValue().trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
