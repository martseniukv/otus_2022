package ru.otus.homework.cache.listeners;

public enum Action {

    GET("Get"),
    PUT("Put"),
    REMOVE("Remove")
    ;

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}