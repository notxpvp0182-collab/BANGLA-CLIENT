package com.banglaclient.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.util.Arrays;

/**
 * Real-time client telemetry tracker for FPS, 1% Low FPS, Frame Time (ms),
 * CPS (Left/Right), Ping, Estimated Server TPS, and Packet Rate.
 * Uses pre-allocated ring buffers to guarantee zero per-frame heap allocations.
 */
public final class PerformanceTracker {

    private static final PerformanceTracker INSTANCE = new PerformanceTracker();
    private static final int SAMPLE_CAPACITY = 240;

    private final float[] frameTimeHistoryMs = new float[SAMPLE_CAPACITY];
    private final int[] fpsHistory = new int[SAMPLE_CAPACITY];
    private final int[] pingHistory = new int[SAMPLE_CAPACITY];
    private final float[] sortScratchBuffer = new float[SAMPLE_CAPACITY];

    private int frameSampleIndex = 0;
    private int frameSampleCount = 0;
    private int historyWriteIndex = 0;

    private long lastFrameNano = System.nanoTime();
    private float currentFrameTimeMs = 0.0f;
    private int currentFps = 0;
    private int onePercentLowFps = 0;

    // CPS Ring Buffers (timestamps in ms)
    private final long[] leftClicks = new long[128];
    private int leftClickHead = 0;
    private final long[] rightClicks = new long[128];
    private int rightClickHead = 0;

    // Network metrics
    private long lastWorldTimePacketMillis = 0L;
    private float estimatedServerTps = 20.0f;
    private int inboundPacketsThisSecond = 0;
    private int outboundPacketsThisSecond = 0;
    private int lastInboundPacketRate = 0;
    private int lastOutboundPacketRate = 0;
    private long lastPacketSecondMillis = System.currentTimeMillis();

    private PerformanceTracker() {
    }

    public static PerformanceTracker getInstance() {
        return INSTANCE;
    }

    /**
     * Called once per rendered frame.
     */
    public void onFrameRendered() {
        long now = System.nanoTime();
        long deltaNano = now - lastFrameNano;
        lastFrameNano = now;

        this.currentFrameTimeMs = Math.max(0.05f, deltaNano / 1_000_000.0f);
        this.frameTimeHistoryMs[frameSampleIndex] = this.currentFrameTimeMs;
        this.frameSampleIndex = (this.frameSampleIndex + 1) % SAMPLE_CAPACITY;
        if (this.frameSampleCount < SAMPLE_CAPACITY) {
            this.frameSampleCount++;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            this.currentFps = client.getCurrentFps();
        }
    }

    /**
     * Called once every client tick (50ms) to update rolling graphs and 1% low calculations.
     */
    public void onClientTick() {
        if (frameSampleCount > 0) {
            System.arraycopy(frameTimeHistoryMs, 0, sortScratchBuffer, 0, frameSampleCount);
            Arrays.sort(sortScratchBuffer, 0, frameSampleCount);
            int worstOnePercentIndex = Math.min(frameSampleCount - 1, Math.max(0, (int) (frameSampleCount * 0.99f)));
            float worstFrameMs = sortScratchBuffer[worstOnePercentIndex];
            this.onePercentLowFps = worstFrameMs > 0.0f ? Math.round(1000.0f / worstFrameMs) : currentFps;
        }

        fpsHistory[historyWriteIndex] = currentFps;
        pingHistory[historyWriteIndex] = getCurrentPing();
        historyWriteIndex = (historyWriteIndex + 1) % SAMPLE_CAPACITY;

        long nowMillis = System.currentTimeMillis();
        if (nowMillis - lastPacketSecondMillis >= 1000L) {
            lastInboundPacketRate = inboundPacketsThisSecond;
            lastOutboundPacketRate = outboundPacketsThisSecond;
            inboundPacketsThisSecond = 0;
            outboundPacketsThisSecond = 0;
            lastPacketSecondMillis = nowMillis;
        }
    }

    public void recordLeftClick() {
        leftClicks[leftClickHead] = System.currentTimeMillis();
        leftClickHead = (leftClickHead + 1) % leftClicks.length;
    }

    public void recordRightClick() {
        rightClicks[rightClickHead] = System.currentTimeMillis();
        rightClickHead = (rightClickHead + 1) % rightClicks.length;
    }

    public int getLeftCps() {
        long cutoff = System.currentTimeMillis() - 1000L;
        int count = 0;
        for (long ts : leftClicks) {
            if (ts >= cutoff) {
                count++;
            }
        }
        return count;
    }

    public int getRightCps() {
        long cutoff = System.currentTimeMillis() - 1000L;
        int count = 0;
        for (long ts : rightClicks) {
            if (ts >= cutoff) {
                count++;
            }
        }
        return count;
    }

    public void onWorldTimeUpdatePacket() {
        long now = System.currentTimeMillis();
        if (lastWorldTimePacketMillis != 0L) {
            long elapsed = Math.max(1L, now - lastWorldTimePacketMillis);
            float instantTps = Math.min(20.0f, Math.max(0.0f, (1000.0f / elapsed) * 20.0f));
            estimatedServerTps = (estimatedServerTps * 0.8f) + (instantTps * 0.2f);
        }
        lastWorldTimePacketMillis = now;
    }

    public void recordInboundPacket() {
        inboundPacketsThisSecond++;
    }

    public void recordOutboundPacket() {
        outboundPacketsThisSecond++;
    }

    public int getCurrentPing() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            return 0;
        }
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return entry != null ? entry.getLatency() : 0;
    }

    public int getCurrentFps() {
        return currentFps;
    }

    public int getOnePercentLowFps() {
        return onePercentLowFps;
    }

    public float getCurrentFrameTimeMs() {
        return currentFrameTimeMs;
    }

    public float getEstimatedServerTps() {
        return estimatedServerTps;
    }

    public int getLastInboundPacketRate() {
        return lastInboundPacketRate;
    }

    public int getLastOutboundPacketRate() {
        return lastOutboundPacketRate;
    }

    public float[] getFrameTimeHistoryMs() {
        return frameTimeHistoryMs;
    }

    public int[] getFpsHistory() {
        return fpsHistory;
    }

    public int[] getPingHistory() {
        return pingHistory;
    }

    public int getHistoryWriteIndex() {
        return historyWriteIndex;
    }

    public int getSampleCapacity() {
        return SAMPLE_CAPACITY;
    }
}
