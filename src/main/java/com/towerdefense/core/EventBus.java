package com.towerdefense.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {

    private final Map<Class<? extends GameEvent>, List<Consumer<GameEvent>>> listeners = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add((Consumer<GameEvent>) listener);
    }

    public void publish(GameEvent event) {
        List<Consumer<GameEvent>> handlers = listeners.get(event.getClass());
        if (handlers != null) {
            for (Consumer<GameEvent> handler : handlers) {
                handler.accept(event);
            }
        }
    }
}