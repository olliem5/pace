package com.olliem5.pace.handler;

import com.olliem5.pace.annotation.PaceHandler;
import com.olliem5.pace.Pace;
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
 * @since 9/02/21
 */

public class EventHandler {
    private final HashMap<Object, List<Method>> registeredMap = new HashMap<>();

    private boolean debugLogging = false;

    public void register(Object object) {
        registeredMap.put(object, Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PaceHandler.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(PaceHandler.class).priority()))
                .collect(Collectors.toList())
        );

        if (debugLogging) {
            Pace.log("Registered '" + object + "' to the event handler!");
        }
    }

    public void unregister(Object object) {
        registeredMap.remove(object);

        if (debugLogging) {
            Pace.log("Unregistered '" + object + "' from the event handler.");
        }
    }

    public <T extends Event> void dispatchPaceEvent(T event) {
        if (event.isCancelled()) return;

        registeredMap.forEach((object, methods) -> methods
                .forEach(method -> {
                    if (method.getParameterTypes()[0] != event.getClass()) return;

                    try {
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        if (debugLogging) {
                            Pace.log("Something went wrong when dispatching the pace event '" + event + "' with the era '" + event.getEventEra());
                        }

                        e.printStackTrace();
                    }
                })
        );

        if (debugLogging) {
            Pace.log("Successfully dispatched '" + event + "' with the era '" + event.getEventEra() + "'!");
        }
    }

    public <T> T dispatchEvent(T event) {
        registeredMap.forEach((object, methods) -> methods
                .forEach(method -> {
                    if (method.getParameterTypes()[0] != event.getClass()) return;

                    try {
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        if (debugLogging) {
                            Pace.log("Something went wrong when dispatching an event.");
                        }

                        exception.printStackTrace();
                    }
                })
        );

        return event;
    }
}
