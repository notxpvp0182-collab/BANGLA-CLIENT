package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Module 291: Optimization Log
 * Records automatic and manual optimization state changes during the session.
 */
public final class OptimizationLog extends AbstractModule {

    private static final List<String> LOG_ENTRIES = new ArrayList<>();
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public OptimizationLog() {
        super(
                "optimization_log",
                "Optimization Log",
                "Records optimization changes during the session.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                true
        );

        registerSetting(new ActionSetting(
                "clear_opt_log",
                "Clear Optimization Log",
                "Clear recorded session optimization log entries",
                "Clear Log",
                LOG_ENTRIES::clear
        ));
    }

    public static synchronized void recordEntry(String description) {
        if (description == null || description.isBlank()) {
            return;
        }
        String entry = "[" + LocalTime.now().format(TIME_FMT) + "] " + description;
        LOG_ENTRIES.add(entry);
        if (LOG_ENTRIES.size() > 100) {
            LOG_ENTRIES.remove(0);
        }
        BanglaLogger.debug("OptimizationLog: {}", description);
    }

    public static synchronized List<String> getEntries() {
        return Collections.unmodifiableList(new ArrayList<>(LOG_ENTRIES));
    }
}
