package ru.otus.atm.model.enums;

public enum Denomination {

    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}