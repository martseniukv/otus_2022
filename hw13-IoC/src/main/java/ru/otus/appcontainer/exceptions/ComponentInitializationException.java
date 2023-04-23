package ru.otus.appcontainer.exceptions;

public class ComponentInitializationException extends RuntimeException{

    public ComponentInitializationException(String message) {
        super(message);
    }
}