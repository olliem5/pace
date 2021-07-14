package com.olliem5.pace.handler;

import com.olliem5.pace.annotation.PaceHandler;
import com.olliem5.pace.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author olliem5
 * @since 1.0
 */

public class EventHandler {
    private final boolean debugLogging;

    public EventHandler(boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    private final HashMap<Object, List<Method>> registeredMap = new HashMap<>();

    public void register(Object object) {
        registeredMap.put(object, Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PaceHandler.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(PaceHandler.class).priority()))
                .collect(Collectors.toList())
        );
        logDebug("Registered '" + object + "' to the event handler!");
    }

    public void unregister(Object object) {
        registeredMap.remove(object);
        logDebug("Unregistered '" + object + "' from the event handler.");
    }

    public <T extends Event> void dispatchPaceEvent(T event) {
        if (event.isCancelled()) return;
        registeredMap.forEach((object, methods) -> methods
                .forEach(method -> {
                    if (method.getParameterTypes()[0] != event.getClass()) return;
                    try {
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        logDebug("Something went wrong when dispatching the pace event '" + event + "' with the era '" + event.getEventEra() + "'.");
                        exception.printStackTrace();
                    }
                })
        );
        logDebug("Successfully dispatched '" + event + "' with the era '" + event.getEventEra() + "'!");
    }

    public <T> T dispatchEvent(T event) {
        registeredMap.forEach((object, methods) -> methods
                .forEach(method -> {
                    if (method.getParameterTypes()[0] != event.getClass()) return;
                    try {
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        logDebug("Something went wrong when dispatching an event.");
                        exception.printStackTrace();
                    }
                })
        );
        return event;
    }

    private void logDebug(String message) {
        if (this.debugLogging) {
            System.out.println("[Pace 1.3] " + message);
        }
    }
}
