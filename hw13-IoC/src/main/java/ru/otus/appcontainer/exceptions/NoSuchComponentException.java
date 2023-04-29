package ru.otus.appcontainer.exceptions;

public class NoSuchComponentException extends RuntimeException {

    public NoSuchComponentException(String message) {
        super(message);
    }
}