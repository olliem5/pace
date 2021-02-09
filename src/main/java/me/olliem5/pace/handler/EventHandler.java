package me.olliem5.pace.handler;

import me.olliem5.pace.Pace;
import me.olliem5.pace.annotation.PaceHandler;
import me.olliem5.pace.event.Event;

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

    //Set this to true if you want to see the outputs.
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

        registeredMap.forEach((object, methods) -> registeredMap.get(object).stream()
                .filter(method -> method.getParameterTypes()[0] == event.getClass())
                .forEach(method -> {
                    try {
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        if (debugLogging) {
                            Pace.log("Something went wrong when dispatching the pace event '" + event + "' with the era '" + event.getEventEra() + "'.");
                        }

                        exception.printStackTrace();
                    }
                }));

        if (debugLogging) {
            Pace.log("Finished dispatching '" + event + "' with the era '" + event.getEventEra() + "'!");
        }
    }

    public <T> T dispatchEvent(T event) {
        registeredMap.forEach((object, methods) -> registeredMap.get(object).stream()
                .filter(method -> method.getParameterTypes()[0] == event.getClass())
                .forEach(method -> {
                    try {
                        method.invoke(object, event);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        if (debugLogging) {
                            Pace.log("Something went wrong when dispatching an event.");
                        }

                        exception.printStackTrace();
                    }
                }));

        return event;
    }
}
