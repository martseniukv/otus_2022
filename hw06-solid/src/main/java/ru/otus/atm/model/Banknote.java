package ru.otus.atm.model;

import ru.otus.atm.model.enums.Denomination;

import java.util.Objects;

public class Banknote {

    private final int number;
    private final Denomination denomination;

    public Banknote(int number, Denomination denomination) {
        this.number = number;
        this.denomination = denomination;
    }

    public int getNumber() {
        return number;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return number == banknote.number && denomination == banknote.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, denomination);
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "number=" + number +
                ", denomination=" + denomination +
                '}';
    }
}