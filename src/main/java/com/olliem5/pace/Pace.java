package com.olliem5.pace;

public class Pace {
    public static final String NAME = "Pace";
    public static final String VERSION = "1.3";
    public static final String PREFIX = "[" + NAME + " " + VERSION + "]";

    public static void log(String message) {
        System.out.println(PREFIX + " " + message);
    }
}
