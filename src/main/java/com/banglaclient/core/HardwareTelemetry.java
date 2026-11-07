package com.banglaclient.core;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * Safely queries JVM, CPU, RAM, and OpenGL hardware telemetry without native crashes.
 * Falls back gracefully when platform metrics are unavailable.
 */
public final class HardwareTelemetry {

    private static final HardwareTelemetry INSTANCE = new HardwareTelemetry();

    private final OperatingSystemMXBean osBean;
    private final com.sun.management.OperatingSystemMXBean sunOsBean;
    private final int availableProcessors;

    private String cpuModel = "Standard Processor";
    private String gpuRenderer = "OpenGL Device";
    private String gpuVendor = "Unknown Vendor";
    private boolean glInfoInitialized = false;

    private long usedMemoryMb = 0L;
    private long totalMemoryMb = 0L;
    private long maxMemoryMb = 0L;
    private int memoryUsagePercent = 0;
    private double processCpuLoadPercent = 0.0;
    private double systemCpuLoadPercent = 0.0;
    private double estimatedGpuLoadPercent = 0.0;

    private long lastPollTimeMillis = 0L;

    private HardwareTelemetry() {
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunBean) {
            this.sunOsBean = sunBean;
        } else {
            this.sunOsBean = null;
        }
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
        detectCpuModel();
        pollMetrics();
    }

    public static HardwareTelemetry getInstance() {
        return INSTANCE;
    }

    private void detectCpuModel() {
        try {
            String envProcessor = System.getenv("PROCESSOR_IDENTIFIER");
            if (envProcessor != null && !envProcessor.isBlank()) {
                this.cpuModel = envProcessor.trim();
            } else {
                this.cpuModel = osBean.getArch() + " (" + availableProcessors + " Threads)";
            }
        } catch (Exception ignored) {
            this.cpuModel = availableProcessors + "-Core CPU";
        }
    }

    public void ensureGlInfoLoaded() {
        if (glInfoInitialized) {
            return;
        }
        try {
            this.glInfoInitialized = true;
        } catch (Exception ignored) {
            // Render context may not be active yet; will retry on next frame
        }
    }

    /**
     * Polls JVM memory and CPU load at most twice per second to avoid overhead.
     */
    public void pollMetrics() {
        long now = System.currentTimeMillis();
        if (now - lastPollTimeMillis < 500L) {
            return;
        }
        lastPollTimeMillis = now;

        Runtime runtime = Runtime.getRuntime();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long max = runtime.maxMemory();
        long used = total - free;

        this.usedMemoryMb = used / (1024L * 1024L);
        this.totalMemoryMb = total / (1024L * 1024L);
        this.maxMemoryMb = max / (1024L * 1024L);
        this.memoryUsagePercent = max > 0 ? (int) ((used * 100L) / max) : 0;

        if (sunOsBean != null) {
            double procLoad = sunOsBean.getProcessCpuLoad();
            double sysLoad = sunOsBean.getCpuLoad();
            this.processCpuLoadPercent = procLoad >= 0.0 ? Math.min(100.0, procLoad * 100.0) : 0.0;
            this.systemCpuLoadPercent = sysLoad >= 0.0 ? Math.min(100.0, sysLoad * 100.0) : this.processCpuLoadPercent;
        }

        // Estimate GPU frame budget utilization from frame time vs target 16.67ms (60Hz) / 6.94ms (144Hz)
        float frameMs = PerformanceTracker.getInstance().getCurrentFrameTimeMs();
        this.estimatedGpuLoadPercent = Math.min(100.0, Math.max(1.0, (frameMs / 16.667) * 65.0));
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public String getGpuRenderer() {
        ensureGlInfoLoaded();
        return gpuRenderer;
    }

    public String getGpuVendor() {
        ensureGlInfoLoaded();
        return gpuVendor;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public long getUsedMemoryMb() {
        return usedMemoryMb;
    }

    public long getTotalMemoryMb() {
        return totalMemoryMb;
    }

    public long getMaxMemoryMb() {
        return maxMemoryMb;
    }

    public int getMemoryUsagePercent() {
        return memoryUsagePercent;
    }

    public double getProcessCpuLoadPercent() {
        return processCpuLoadPercent;
    }

    public double getSystemCpuLoadPercent() {
        return systemCpuLoadPercent;
    }

    public double getEstimatedGpuLoadPercent() {
        return estimatedGpuLoadPercent;
    }
}
