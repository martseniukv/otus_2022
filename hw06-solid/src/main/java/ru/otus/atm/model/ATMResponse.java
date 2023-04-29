package ru.otus.atm.model;

import java.util.List;
import java.util.Objects;

public class ATMResponse<T> {

    private T data;
    private List<ATMError> errors;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<ATMError> getErrors() {
        return errors;
    }

    public void setErrors(List<ATMError> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATMResponse<?> that = (ATMResponse<?>) o;
        return Objects.equals(data, that.data) && Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, errors);
    }

    @Override
    public String toString() {
        return "ATMResponse{" +
                "data=" + data +
                ", errors=" + errors +
                '}';
    }
}