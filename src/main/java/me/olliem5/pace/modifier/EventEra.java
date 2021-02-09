package me.olliem5.pace.modifier;

/**
 * @author olliem5
 * @since 9/02/21
 */

public enum EventEra {
    PRE("Pre"),
    POST("Post");

    private String name;

    EventEra(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
