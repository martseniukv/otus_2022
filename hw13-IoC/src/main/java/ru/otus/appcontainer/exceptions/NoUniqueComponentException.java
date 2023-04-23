package ru.otus.appcontainer.exceptions;

public class NoUniqueComponentException extends RuntimeException{

    public NoUniqueComponentException(String message) {
        super(message);
    }
}