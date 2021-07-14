package com.olliem5.pace.event;

import com.olliem5.pace.modifier.EventEra;

/**
 * @author olliem5
 * @since 1.0
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
