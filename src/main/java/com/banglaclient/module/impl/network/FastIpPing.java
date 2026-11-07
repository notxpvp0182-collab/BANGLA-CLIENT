package com.banglaclient.module.impl.network;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.StringSetting;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

/**
 * Module 235: Fast IP Ping
 * Asynchronously measures TCP handshake latency to a configured server address (defaults to play.banglaSMP.net:25565).
 */
public final class FastIpPing extends AbstractModule {

    private final StringSetting targetHost = registerSetting(new StringSetting(
            "ping_target_host", "Server Host", "Server address to test TCP reachability", ClientCore.OFFICIAL_SERVER, 128
    ));

    private volatile int lastMeasuredMs = -1;

    public FastIpPing() {
        super(
                "fast_ip_ping",
                "Fast IP Ping",
                "Checks server TCP reachability and handshake latency.",
                ModuleCategory.NETWORK_SERVER,
                false
        );

        registerSetting(new ActionSetting(
                "run_ping_check",
                "Ping Server Now",
                "Perform an asynchronous TCP ping to the configured server",
                "Test Ping",
                this::pingAsync
        ));
    }

    public void pingAsync() {
        String host = targetHost.getValue().trim();
        CompletableFuture.runAsync(() -> {
            long start = System.nanoTime();
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, 25565), 2500);
                lastMeasuredMs = Math.round((System.nanoTime() - start) / 1_000_000.0f);
            } catch (Exception e) {
                lastMeasuredMs = -1;
            }
        });
    }

    public int getLastMeasuredMs() {
        return lastMeasuredMs;
    }
}
