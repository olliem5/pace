package me.olliem5.pace.event;

import me.olliem5.pace.modifier.EventEra;

/**
 * @author olliem5
 * @since 9/02/21
 */

public abstract class Event {
    private final EventEra eventEra;

    public Event(EventEra eventEra) {
        this.eventEra = eventEra;
    }

    public Event() {
        this.eventEra = EventEra.PRE;
    }

    private boolean cancelled;

    public EventEra getEventEra() {
        return eventEra;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
