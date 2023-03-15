package ru.otus.model;

import java.util.List;
import java.util.Optional;

public class ObjectForMessage implements Copyable<ObjectForMessage>{

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage copy() {
        final ObjectForMessage objectForMessage = new ObjectForMessage();
        Optional.ofNullable(data).ifPresent(f -> objectForMessage.setData(List.copyOf(data)));
        return objectForMessage;
    }
}
