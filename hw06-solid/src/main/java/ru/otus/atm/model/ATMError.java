package ru.otus.atm.model;

public class ATMError {

    private final long code;
    private String message;

    public ATMError(long code, String message) {
        this.code = code;
        this.message = message;
    }
    public ATMError(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ATMError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}