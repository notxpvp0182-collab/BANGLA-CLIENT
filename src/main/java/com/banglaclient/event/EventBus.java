package com.banglaclient.event;

import com.banglaclient.core.BanglaLogger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * High-performance, reflection-free, thread-safe EventBus using typed functional consumers.
 * Avoids reflection overhead during high-frequency render and tick loops.
 */
public final class EventBus {

    private final Map<Class<? extends Event>, CopyOnWriteArrayList<ListenerEntry<? extends Event>>> listeners = new ConcurrentHashMap<>();

    public static final class ListenerEntry<T extends Event> implements Comparable<ListenerEntry<T>> {
        private final Object owner;
        private final Consumer<T> consumer;
        private final int priority;

        public ListenerEntry(Object owner, Consumer<T> consumer, int priority) {
            this.owner = owner;
            this.consumer = consumer;
            this.priority = priority;
        }

        public Object getOwner() {
            return owner;
        }

        public Consumer<T> getConsumer() {
            return consumer;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(ListenerEntry<T> other) {
            return Integer.compare(other.priority, this.priority);
        }
    }

    public <T extends Event> void subscribe(Object owner, Class<T> eventClass, Consumer<T> listener) {
        subscribe(owner, eventClass, listener, 0);
    }

    public <T extends Event> void subscribe(Object owner, Class<T> eventClass, Consumer<T> listener, int priority) {
        CopyOnWriteArrayList<ListenerEntry<? extends Event>> list = listeners.computeIfAbsent(
                eventClass, k -> new CopyOnWriteArrayList<>()
        );
        ListenerEntry<T> entry = new ListenerEntry<>(owner, listener, priority);
        list.add(entry);
        list.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
    }

    public void unsubscribeAll(Object owner) {
        for (CopyOnWriteArrayList<ListenerEntry<? extends Event>> list : listeners.values()) {
            list.removeIf(entry -> entry.getOwner() == owner);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> T post(T event) {
        List<ListenerEntry<? extends Event>> list = listeners.get(event.getClass());
        if (list == null || list.isEmpty()) {
            return event;
        }

        for (int i = 0, size = list.size(); i < size; i++) {
            ListenerEntry<T> entry = (ListenerEntry<T>) list.get(i);
            try {
                entry.getConsumer().accept(event);
            } catch (Exception e) {
                BanglaLogger.warnRateLimited("Exception while dispatching event {} to {}: {}",
                        event.getClass().getSimpleName(),
                        entry.getOwner().getClass().getSimpleName(),
                        e.getMessage());
            }
        }
        return event;
    }

    public void clear() {
        listeners.clear();
    }
}
